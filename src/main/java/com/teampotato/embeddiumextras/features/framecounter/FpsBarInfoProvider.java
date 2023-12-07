package com.teampotato.embeddiumextras.features.framecounter;

import com.teampotato.embeddiumextras.mixins.frame_counter.MinecraftAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FrameTimer;
import net.minecraft.util.math.MathHelper;

import java.time.LocalDateTime;

public class FpsBarInfoProvider {
    public static int lastMinFrame = 0;
    public static LocalDateTime dateStarted = null;
    public static String splitChar = null;
    public static String fpsNow = null;
    public static String fpsMin = null;
    public static String fpsAvg = null;
    public static String playTime = null;
    public static String usedMemory = null;

    public static void recalculate() {
        FrameTimer ft = Minecraft.getInstance().getFrameTimer();
        int logStart = ft.getLogStart();
        int logEnd = ft.getLogEnd();
        if (logEnd == logStart) return;
        int fps = MinecraftAccessor.ee$getFps();
        if (fps <= 0) fps = 1;
        long[] frames = ft.getLog();
        long maxNS = (long) (1 / (double) fps * 1000000000);
        long totalNS = 0;

        int index = MathHelper.positiveModulo(logEnd - 1, frames.length);
        while (index != logStart && (double) totalNS < 1000000000) {
            long timeNs = frames[index];
            if (timeNs > maxNS) maxNS = timeNs;
            totalNS += timeNs;
            index = MathHelper.positiveModulo(index - 1, frames.length);
        }

        lastMinFrame = (int) (1 / ((double) maxNS / 1000000000));
    }
}