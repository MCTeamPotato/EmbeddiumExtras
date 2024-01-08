package com.teampotato.embeddiumextras.features.framecounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.mixins.frame_counter.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.FrameTimer;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.lwjgl.opengl.GL11;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class FpsBarInfoProvider {
    public static int lastMinFrame = 0;
    public static LocalDateTime dateStarted = null;
    public static String splitChar = null;
    public static String fpsNow = null;
    public static String fpsMin = null;
    public static String fpsAvg = null;
    public static String playTime = null;
    public static String usedMemory = null;

    public static void recalculateLastMinFrame() {
        FrameTimer ft = Minecraft.getInstance().getFrameTimer();
        int logStart = ft.getLogStart();
        int logEnd = ft.getLogEnd();
        if (logEnd == logStart) return;
        int fps = MinecraftAccessor.ee$getFps();
        if (fps <= 0) fps = 1;
        long[] frames = ft.getLog();
        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        int index = Math.floorMod(logEnd - 1, frames.length);
        while (index != logStart && (double) totalNS < 1000000000) {
            long timeNs = frames[index];
            if (timeNs > maxNS) maxNS = timeNs;
            totalNS += timeNs;
            index = Math.floorMod(index - 1, frames.length);
        }

        lastMinFrame = (int) (1 / ((double) maxNS / 1000000000));
    }

    public static void render(MatrixStack matrixStack, int usedMemoryPercent, String advencedFpsString) {
        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "OFF")) return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart) return;

        String displayString;
        int fps = MinecraftAccessor.ee$getFps();

        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "ADVANCED")) {
            displayString = advencedFpsString;
        } else {
            if (FpsBarInfoProvider.fpsNow == null) FpsBarInfoProvider.fpsNow = I18n.get("extras.fps.now");
            displayString = FpsBarInfoProvider.fpsNow + fps;
        }

        if (FpsBarInfoProvider.dateStarted == null) FpsBarInfoProvider.dateStarted = LocalDateTime.now();
        if (FpsBarInfoProvider.splitChar == null) FpsBarInfoProvider.splitChar = I18n.get("extras.split");
        if (EmbeddiumExtrasConfig.showPlayTime.get()) {
            if (FpsBarInfoProvider.playTime == null) FpsBarInfoProvider.playTime = I18n.get("extras.gameTime");
            displayString = displayString + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.playTime + DurationFormatUtils.formatDuration(Duration.between(FpsBarInfoProvider.dateStarted, LocalDateTime.now()).toMillis(), "H:mm:ss", true);
        }
        if (EmbeddiumExtrasConfig.showMemoryPercentage.get()) {
            if (FpsBarInfoProvider.usedMemory == null) FpsBarInfoProvider.usedMemory = I18n.get("extras.memoryUsed");
            displayString = displayString + FpsBarInfoProvider.splitChar + FpsBarInfoProvider.usedMemory + usedMemoryPercent + "%";
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
}