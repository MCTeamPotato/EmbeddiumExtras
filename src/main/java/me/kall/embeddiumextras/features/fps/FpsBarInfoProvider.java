package me.kall.embeddiumextras.features.fps;

import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.fps.gpu.GpuUser;
import net.minecraft.client.Minecraft;
import net.minecraft.util.FrameTimer;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

public class FpsBarInfoProvider {
    private static final int[] FPS_LIST = new int[10];
    private static final String EMPTY = "";
    private static int index = 0;
    private static int count = 0;
    private static int totalFps = 0;
    private static int currentFps = 0;
    private static int minFps = 0;
    private static int memory = 0;

    public static Instant playStart;

    public static void updateFps(int fps) {
        currentFps = fps;

        minFps = minFps();
        memory = memory();

        totalFps -= FPS_LIST[index];
        FPS_LIST[index] = fps;
        totalFps += fps;

        index = (index + 1) % FPS_LIST.length;
        if (count < FPS_LIST.length) count++;
    }

    private static int getMinFps() {
        return count == 0 ? getCurrentFps() : minFps;
    }

    public static int getAvgFps() {
        return count == 0 ? getCurrentFps() : totalFps / count;
    }

    private static int getCurrentFps() {
        return currentFps;
    }

    private static int memory() {
        Runtime runtime = Runtime.getRuntime();
        return (int) ((runtime.totalMemory() - runtime.freeMemory()) * 100L / runtime.maxMemory());
    }

    public static @NotNull String getPlayTime() {
        if (playStart == null) return "0:00:00";
        return DurationFormatUtils.formatDuration(Duration.between(playStart, Instant.now()).toMillis(), "H:mm:ss", true);
    }

    public static void startPlayTime() {
        if (playStart == null) playStart = Instant.now();
    }

    private static int getGpuInfo() {
        return (int) (((GpuUser) Minecraft.getInstance()).extras$getGpuCooldownUsage());
    }

    private static String getFpsBar(Minecraft client) {
        ExtrasConfig.FpsMode fpsMode = ExtrasConfig.FPS_DISPLAY_MODE.get();
        if (fpsMode.equals(ExtrasConfig.FpsMode.OFF)) return EMPTY;

        if (client.options.renderDebug) return EMPTY;

        boolean isAdvanced = fpsMode.equals(ExtrasConfig.FpsMode.ADVANCED);
        boolean showMem = ExtrasConfig.SHOW_MEMORY_INFO.get();
        boolean showPlayTime = ExtrasConfig.SHOW_GAME_TIME.get();
        boolean showGpuInfo = ExtrasConfig.SHOW_GPU_INFO.get();

        String currentFps = String.valueOf(getCurrentFps());
        String lowFps = isAdvanced ? String.valueOf(getMinFps()) : EMPTY;
        String avgFps = isAdvanced ? String.valueOf(getAvgFps()) : EMPTY;
        String memory = showMem ? String.valueOf(FpsBarInfoProvider.memory) : EMPTY;
        String playTime = showPlayTime ? getPlayTime() : EMPTY;
        String gpu = showGpuInfo ? String.valueOf(getGpuInfo()) : EMPTY;

        StringBuilder fpsBar = new StringBuilder("FPS: ").append(currentFps);
        if (!lowFps.isEmpty()) fpsBar.append(" | MIN: ").append(lowFps);
        if (!avgFps.isEmpty()) fpsBar.append(" | AVG: ").append(avgFps);
        if (!memory.isEmpty()) fpsBar.append(" | MEM: ").append(memory).append("%");
        if (!playTime.isEmpty()) fpsBar.append(" | TIME: ").append(playTime);
        if (!gpu.isEmpty()) fpsBar.append(" | GPU: ").append(gpu).append("%");
        return fpsBar.toString();
    }

    public static void render(PoseStack stack) {
        Minecraft client = Minecraft.getInstance();
        String fpsBar = getFpsBar(client);
        if (fpsBar.isEmpty()) return;

        int xOffset = ExtrasConfig.FPS_POS_X_OFFSET.get();
        int yOffset = ExtrasConfig.FPS_POS_Y_OFFSET.get();
        boolean showAtRightSide = ExtrasConfig.FPS_ALIGN_RIGHT.get();

        int screenWidth = client.getWindow().getGuiScaledWidth();

        int color = 0xFFFFFF;

        if (showAtRightSide) {
            int textWidth = client.font.width(fpsBar);
            client.font.draw(stack, fpsBar, screenWidth - textWidth - xOffset, yOffset, color);
        } else {
            client.font.draw(stack, fpsBar, xOffset, yOffset, color);
        }
    }

    private static int minFps() {
        final FrameTimer timer = Minecraft.getInstance().getFrameTimer();
        final int start = timer.getLogStart();
        final int end = timer.getLogEnd();
        final long[] frames = timer.getLog();

        if (end == start) return minFps;

        int index = Math.floorMod(end - 1, frames.length);
        long maxNS = frames[index];
        long totalNS = 0;

        do {
            long frameTime = frames[index];
            if (frameTime > maxNS) maxNS = frameTime;
            totalNS += frameTime;

            index = Math.floorMod(index - 1, frames.length);

        } while (index != start && totalNS < 1_000_000_000L);

        return (int) (1_000_000_000.0 / maxNS);
    }
}
