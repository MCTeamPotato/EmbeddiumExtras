package com.teampotato.embeddiumextras.mixins.frame_counter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.features.framecounter.FpsBarInfoProvider;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public abstract class FrameCounterMixin {

    @Unique private int ee$lastMeasuredFPS;
    @Unique private String ee$runningAverageFPS;
    @Unique private final Queue<Integer> ee$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(at = @At("TAIL"), method = "render")
    public void ee$render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        FpsBarInfoProvider.render(matrixStack, ee$getUsedMemoryPercent(), ee$getAdvancedFPSString(MinecraftAccessor.ee$getFps()));
    }


    @Unique
    private @NotNull String ee$getAdvancedFPSString(int fps) {
        FpsBarInfoProvider.recalculateLastMinFrame();

        if (this.ee$lastMeasuredFPS != fps) {
            this.ee$lastMeasuredFPS = fps;
            if (this.ee$fpsRunningAverageQueue.size() > 14) this.ee$fpsRunningAverageQueue.poll();
            this.ee$fpsRunningAverageQueue.offer(fps);
            int totalFps = 0;
            int frameCount = 0;
            for (int frameTime : this.ee$fpsRunningAverageQueue) {
                totalFps += frameTime;
                frameCount++;
            }
            ee$runningAverageFPS = String.valueOf(totalFps / frameCount);
        }

        if (FpsBarInfoProvider.fpsNow == null) FpsBarInfoProvider.fpsNow = I18n.get("extras.fps.now");
        if (FpsBarInfoProvider.fpsMin == null) FpsBarInfoProvider.fpsMin = I18n.get("extras.fps.min");
        if (FpsBarInfoProvider.fpsAvg == null) FpsBarInfoProvider.fpsAvg = I18n.get("extras.fps.avg");
        if (FpsBarInfoProvider.splitChar == null) FpsBarInfoProvider.splitChar = I18n.get("extras.split");

        return FpsBarInfoProvider.fpsNow + fps + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.fpsMin + FpsBarInfoProvider.lastMinFrame + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.fpsAvg + this.ee$runningAverageFPS;
    }

    @Unique
    private static int ee$getUsedMemoryPercent() {
        Runtime runtime = Runtime.getRuntime();
        return (int) ((runtime.totalMemory() - runtime.freeMemory()) * 100 / (double) runtime.maxMemory());
    }
}
