package me.kall.embeddiumextras.mixin.fps;

import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.fps.FpsBarInfoProvider;
import me.kall.embeddiumextras.features.fps.gpu.GpuUser;
import me.kall.embeddiumextras.features.fps.gpu.TimerQuery;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Minecraft.class)
public class MinecraftMixin implements GpuUser {
    @Shadow private static int fps;
    @Shadow @Nullable public ClientLevel level;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow private long lastNanoTime;
    @Shadow @Final public Options options;
    @Shadow public String fpsString;

    @Unique private TimerQuery.FrameProfile extras$currentFrameProfile;
    @Unique private boolean extras$beginProfile = false;
    @Unique private double extras$gpuCooldownUsage = 0;
    @Unique private double extras$gpuUsage = 0;
    @Unique private long extras$savedCpuDuration = 0;

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Ljava/lang/String;format(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;", remap = false, shift = At.Shift.AFTER))
    private void onFpsUpdate(CallbackInfo ci) {
        FpsBarInfoProvider.updateFps(fps);
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onPlayStart(CallbackInfo ci) {
        if (this.level != null && this.player != null && FpsBarInfoProvider.playStart == null) {
            FpsBarInfoProvider.startPlayTime();
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 3))
    private void onProfilerPush(boolean renderLevel, CallbackInfo ci) {
        if (this.level == null || this.player == null) return;
        if (!ExtrasConfig.SHOW_GPU_INFO.get() && !this.options.renderDebug) {
            extras$beginProfile = false;
        } else {
            extras$beginProfile = this.extras$currentFrameProfile == null || this.extras$currentFrameProfile.isDone();
            if (extras$beginProfile) {
                TimerQuery.getInstance().ifPresent(TimerQuery::beginProfile);
            }
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;blitToScreen(II)V", shift = At.Shift.AFTER))
    private void onBlitToScreen(boolean renderLevel, CallbackInfo ci) {
        if (!extras$beginProfile) return;
        TimerQuery.getInstance().ifPresent(timerQuery -> this.extras$currentFrameProfile = timerQuery.endProfile());
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FrameTimer;logFrameDuration(J)V"))
    private void onSaveCpuDuration(boolean renderLevel, CallbackInfo ci) {
        if (!extras$beginProfile) return;
        long now = Util.getNanos();
        this.extras$savedCpuDuration = now - this.lastNanoTime;
        extras$beginProfile = false;
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V", ordinal = 7, shift = At.Shift.AFTER))
    private void onFpsProfilerPush(CallbackInfo ci) {
        if (this.extras$currentFrameProfile != null && this.extras$currentFrameProfile.isDone()) {
            this.extras$gpuUsage = (double) this.extras$currentFrameProfile.get() * 100.0 / Math.max(1.0, this.extras$savedCpuDuration);
        }
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fpsString:Ljava/lang/String;", shift = At.Shift.AFTER))
    private void modifyFpsString(boolean renderLevel, CallbackInfo ci) {
        if (this.extras$gpuUsage > 0.0) {
            this.fpsString += " GPU: " + (this.extras$gpuUsage > 100.0 ? "100%" : Math.round(this.extras$gpuUsage) + "%");
            this.extras$gpuCooldownUsage = this.extras$gpuUsage;
        }
    }

    @Override
    public double extras$getGpuCooldownUsage() {
        return extras$gpuCooldownUsage;
    }
}
