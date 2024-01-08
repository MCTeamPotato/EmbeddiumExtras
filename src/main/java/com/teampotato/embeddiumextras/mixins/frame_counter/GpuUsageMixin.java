package com.teampotato.embeddiumextras.mixins.frame_counter;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.framecounter.IGpuUsage;
import com.teampotato.embeddiumextras.features.gpu.TimerQuery;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import net.minecraft.util.text.TextFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class GpuUsageMixin implements IGpuUsage {
    @Shadow
    private long lastNanoTime;
    @Shadow public String fpsString;

    @Shadow @Final public GameSettings options;
    @Unique
    private TimerQuery.FrameProfile embPlus$currentFrameProfile;
    @Unique private boolean embPlus$begin = false;
    @Unique private double embPlus$gpuUsage = 0;
    @Unique private double embPlus$gpuCooldownUsage = 0;
    @Unique private long embPlus$savedCpuDuration = 0;

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;push(Ljava/lang/String;)V", shift = At.Shift.AFTER, ordinal = 3))
    private void inject$posePush(boolean pRenderLevel, CallbackInfo ci) {
        if (!EmbeddiumExtrasConfig.showGpuPercentage.get() && !options.renderDebug) {
            embPlus$begin = false;
        } else {
            embPlus$begin = this.embPlus$currentFrameProfile == null || this.embPlus$currentFrameProfile.isDone();
            if (embPlus$begin) {
                TimerQuery.getInstance().ifPresent(TimerQuery::beginProfile);
            }
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/shader/Framebuffer;blitToScreen(II)V", shift = At.Shift.AFTER))
    private void inject$blitToScreen(boolean pRenderLevel, CallbackInfo ci) {
        if (embPlus$begin) {
            TimerQuery.getInstance().ifPresent((timerQuery) -> this.embPlus$currentFrameProfile = timerQuery.endProfile());
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/FrameTimer;logFrameDuration(J)V", shift = At.Shift.BEFORE))
    private void inject$saveCPUDuration(boolean pRenderLevel, CallbackInfo ci) {
        if (embPlus$begin) {
            long l = Util.getNanos();
            this.embPlus$savedCpuDuration = l - this.lastNanoTime;
            embPlus$begin = false;
        }
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/profiler/IProfiler;push(Ljava/lang/String;)V", ordinal = 7, shift = At.Shift.AFTER))
    private void inject$fpsPush(boolean pRenderLevel, CallbackInfo ci) {
        if (this.embPlus$currentFrameProfile != null && this.embPlus$currentFrameProfile.isDone()) {
            this.embPlus$gpuUsage = (double)this.embPlus$currentFrameProfile.get() * 100.0 / (double)this.embPlus$savedCpuDuration;
        }
    }

    @Inject(method = "runTick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;fpsString:Ljava/lang/String;", shift = At.Shift.AFTER))
    private void modify$fpsString(boolean pRenderLevel, CallbackInfo ci) {
        if (this.embPlus$gpuUsage > 0.0) {
            String var10000 = this.embPlus$gpuUsage > 100.0 ? TextFormatting.RED + "100%" : Math.round(this.embPlus$gpuUsage) + "%";
            this.fpsString += " GPU: " + var10000;
            embPlus$gpuCooldownUsage = embPlus$gpuUsage;
        }
    }


    @Override
    public double embExtra$getGPU() {
        return embPlus$gpuUsage;
    }

    @Override
    public double embExtra$getCooldownGPU() {
        return embPlus$gpuCooldownUsage;
    }
}
