package vice.magnesium_extras.mixins.framecounter;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

@Mixin(ForgeIngameGui.class)
public abstract class FrameCounterMixin {

    @Unique
    private int repe$lastMeasuredFPS;
    @Unique
    private String repe$runningAverageFPS;
    @Unique
    private final Queue<Integer> repe$fpsRunningAverageQueue = new LinkedList<>();

    @Inject(at = @At("TAIL"), method = "render")
    public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info)
    {
        if (Objects.equals(MagnesiumExtrasConfig.fpsCounterMode.get(), "OFF"))
            return;

        Minecraft client = Minecraft.getInstance();

        // return if F3 menu open and graph not displayed
        if (client.options.renderDebug && !client.options.renderFpsChart)
            return;

        String displayString;
        int fps = vice.magnesium_extras.mixins.framecounter.FpsAccessorMixin.getFPS();

        if (Objects.equals(MagnesiumExtrasConfig.fpsCounterMode.get(), "ADVANCED"))
            displayString = rb$getAdvancedFPSString(fps);
        else
            displayString = String.valueOf(fps);

        boolean textAlignRight = MagnesiumExtrasConfig.fpsCounterAlignRight.get();

        @SuppressWarnings("RedundantCast") float textPos = (int)MagnesiumExtrasConfig.fpsCounterPosition.get();

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
    private @NotNull String rb$getAdvancedFPSString(int fps)
    {
        vice.magnesium_extras.features.framecounter.MinFrameProvider.recalculate();

        if (repe$lastMeasuredFPS != fps)
        {
            repe$lastMeasuredFPS = fps;

            if (repe$fpsRunningAverageQueue.size() > 14)
                repe$fpsRunningAverageQueue.poll();

            repe$fpsRunningAverageQueue.offer(fps);

            int totalFps = 0;
            int frameCount = 0;
            for (int frameTime : repe$fpsRunningAverageQueue)
            {
                totalFps += frameTime;
                frameCount++;
            }

            @SuppressWarnings("RedundantCast") int average = (int) (totalFps / frameCount);
            repe$runningAverageFPS = String.valueOf(average);
        }

        return fps + " | MIN " + vice.magnesium_extras.features.framecounter.MinFrameProvider.getLastMinFrame() + " | AVG " + repe$runningAverageFPS;
    }
}

