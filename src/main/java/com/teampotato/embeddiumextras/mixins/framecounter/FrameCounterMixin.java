package com.teampotato.embeddiumextras.mixins.framecounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.framecounter.FpsBarInfoProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public abstract class FrameCounterMixin {

    @Unique private int ee$lastMeasuredFPS;
    @Unique private String ee$runningAverageFPS;
    @Unique private final Queue<Integer> ee$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(at = @At("TAIL"), method = "render")
    public void ee$render(MatrixStack matrixStack, float tickDelta, CallbackInfo info) {
        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "OFF")) return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart) return;

        String displayString;
        int fps = MinecraftAccessor.ee$getFps();

        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "ADVANCED")) {
            displayString = this.ee$getAdvancedFPSString(fps);
        } else {
            displayString = String.valueOf(fps);
        }

        if (FpsBarInfoProvider.dateStarted == null) FpsBarInfoProvider.dateStarted = LocalDateTime.now();
        if (EmbeddiumExtrasConfig.showPlayTime.get()) {
            if (FpsBarInfoProvider.splitChar == null) FpsBarInfoProvider.splitChar = I18n.get("extras.split");
            displayString = displayString + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.playTime + DurationFormatUtils.formatDuration(Duration.between(FpsBarInfoProvider.dateStarted, LocalDateTime.now()).toMillis(), "H:mm:ss", true);
        }

        boolean textAlignRight = EmbeddiumExtrasConfig.fpsCounterAlignRight.get();

        float textPos = EmbeddiumExtrasConfig.fpsCounterPosition.get().floatValue();

        int textAlpha = 200;
        int textColor = 0xFFFFFF;
        float fontScale = 0.75F;

        double guiScale = client.getWindow().getGuiScale();
        if (guiScale > 0) {
            textPos /= (float) guiScale;
        }

        // Prevent FPS-Display to render outside screenspace
        float maxTextPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString);
        float maxTextPosY = client.getWindow().getGuiScaledHeight() - client.font.lineHeight;
        float textPosX, textPosY;
        if (textAlignRight) {
            textPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString) - textPos;
        } else {
            textPosX = Math.min(textPos, maxTextPosX);
        }
        textPosX = Math.min(Math.max(textPosX, 0), maxTextPosX);
        textPosY = Math.min(textPos, maxTextPosY);

        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

        if (client.getWindow().getGuiScale() > 3) {
            GL11.glPushMatrix();
            GL11.glScalef(fontScale, fontScale, fontScale);
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
            GL11.glPopMatrix();
        } else {
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
        }
    }


    @Unique
    private @NotNull String ee$getAdvancedFPSString(int fps) {
        FpsBarInfoProvider.recalculate();

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
        if (FpsBarInfoProvider.playTime == null) FpsBarInfoProvider.playTime = I18n.get("extras.gameTime");
        if (FpsBarInfoProvider.splitChar == null) FpsBarInfoProvider.splitChar = I18n.get("extras.split");

        return FpsBarInfoProvider.fpsNow + fps + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.fpsMin + " " + FpsBarInfoProvider.lastMinFrame + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.fpsAvg + " " + this.ee$runningAverageFPS;
    }
}

