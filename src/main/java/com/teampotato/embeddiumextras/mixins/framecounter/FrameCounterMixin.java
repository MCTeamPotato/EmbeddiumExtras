package com.teampotato.embeddiumextras.mixins.framecounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.framecounter.MinFrameProvider;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public abstract class FrameCounterMixin {

    @Unique
    private int ee$lastMeasuredFPS;
    @Unique
    private String ee$runningAverageFPS;
    @Unique
    private final Queue<Integer> ee$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(at = @At("TAIL"), method = "render")
    public void ee$render(MatrixStack matrixStack, float tickDelta, CallbackInfo info)
    {
        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "OFF"))
            return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart)
            return;

        String displayString;
        int fps = MinecraftAccessor.ee$getFps();

        if (Objects.equals(EmbeddiumExtrasConfig.fpsCounterMode.get(), "ADVANCED"))
            displayString = ee$getAdvancedFPSString(fps);
        else
            displayString = String.valueOf(fps);

        boolean textAlignRight = EmbeddiumExtrasConfig.fpsCounterAlignRight.get();

        @SuppressWarnings("RedundantCast") float textPos = (int) EmbeddiumExtrasConfig.fpsCounterPosition.get();

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
        if (textAlignRight)
            textPosX = client.getWindow().getGuiScaledWidth() - client.font.width(displayString) - textPos;
        else
            textPosX = Math.min(textPos, maxTextPosX);
        textPosX = Math.min(Math.max(textPosX, 0), maxTextPosX);
        textPosY = Math.min(textPos, maxTextPosY);

        int drawColor = ((textAlpha & 0xFF) << 24) | textColor;

        if (client.getWindow().getGuiScale() > 3)
        {
            GL11.glPushMatrix();
            GL11.glScalef(fontScale, fontScale, fontScale);
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
            GL11.glPopMatrix();
        }
        else
        {
            client.font.drawShadow(matrixStack, displayString, textPosX, textPosY, drawColor);
        }
    }


    @Unique
    private @NotNull String ee$getAdvancedFPSString(int fps)
    {
        MinFrameProvider.recalculate();

        if (ee$lastMeasuredFPS != fps)
        {
            ee$lastMeasuredFPS = fps;

            if (ee$fpsRunningAverageQueue.size() > 14)
                ee$fpsRunningAverageQueue.poll();

            ee$fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (int frameTime : ee$fpsRunningAverageQueue)
            {
                totalFps += frameTime;
                frameCount++;
            }

            @SuppressWarnings("RedundantCast") int average = (int) (totalFps / frameCount);
            ee$runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + MinFrameProvider.getLastMinFrame() + " | AVG " + ee$runningAverageFPS;
    }
}

