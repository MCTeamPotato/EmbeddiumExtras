package me.kall.embeddiumextras.features.zoom;

import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

public class ZoomManager {
    public static final KeyMapping ZOOM = new KeyMapping("extras.zoom.key", GLFW.GLFW_KEY_C, "extras.zoom.key.category");

    private static final double DEFAULT_LEVEL = 3;
    private static double currentLevel = -1, defaultMouseSensitivity = -1;


    public static double changeFov(double fov) {
        Options options = Minecraft.getInstance().options;
        if (currentLevel == -1) currentLevel = DEFAULT_LEVEL;

        if (!ZOOM.isDown()) {
            currentLevel = DEFAULT_LEVEL;

            if (defaultMouseSensitivity != -1) {
                options.mouseWheelSensitivity = defaultMouseSensitivity;
                defaultMouseSensitivity = -1;
            }

            return fov;
        }

        if (defaultMouseSensitivity == -1) defaultMouseSensitivity = options.mouseWheelSensitivity;

        options.mouseWheelSensitivity = defaultMouseSensitivity * (fov / currentLevel / fov);

        return fov / currentLevel;
    }

    public static void onMouseScroll(double amount) {
        if (!ZOOM.isDown()) return;

        if (currentLevel == -1) currentLevel = DEFAULT_LEVEL;

        if (amount > 0) {
            currentLevel *= 1.1;
        } else if (amount < 0) {
            currentLevel *= 0.9;
        }

        currentLevel = Mth.clamp(currentLevel, 1, ExtrasConfig.MAX_ZOOM_SCALE.get());
    }
}
