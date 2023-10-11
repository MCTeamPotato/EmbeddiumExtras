package com.teampotato.embeddiumextras.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

public class MagnesiumExtrasConfig {
    public static final ForgeConfigSpec ConfigSpec;

    public static ForgeConfigSpec.ConfigValue<String> fadeInQuality;
    public static ConfigValue<String> fpsCounterMode;
    public static ConfigValue<Boolean> fpsCounterAlignRight;
    public static ConfigValue<Integer> fpsCounterPosition;


    public static ConfigValue<Integer> maxTileEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxTileEntityRenderDistanceY;

    public static ConfigValue<Integer> maxEntityRenderDistanceSquare;
    public static ConfigValue<Integer> maxEntityRenderDistanceY;

    public static ConfigValue<Boolean> enableDistanceChecks;

    public static final ZoomValues zoomValues = new ZoomValues();
    public static ConfigValue<Boolean> lowerZoomSensitivity;
    public static ConfigValue<String> zoomTransition;
    public static ConfigValue<String> zoomMode;
    public static ConfigValue<String> cinematicCameraMode;
    public static ConfigValue<Boolean> zoomScrolling;
    public static ConfigValue<Boolean> zoomOverlay;

    static {
        ConfigBuilder builder = new ConfigBuilder("Embeddium Extra Settings");

        builder.Block("Misc", b -> fadeInQuality = b.define("Chunk Fade In Quality (OFF, FAST, FANCY)", "FANCY"));

        builder.Block("FPS Counter", b -> {
            fpsCounterMode = b.define("Display FPS Counter (OFF, SIMPLE, ADVANCED)", "ADVANCED");
            fpsCounterAlignRight = b.define("Right-align FPS Counter", false);
            fpsCounterPosition = b.define("FPS Counter Distance", 12);
        });

        builder.Block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.Block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower Zoom Sensitivity", true);
            zoomScrolling = b.define("Zoom Scrolling Enabled", true);
            zoomTransition = b.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("Enable Zoom Overlay", true);
        });

        ConfigSpec = builder.Save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        ConfigSpec.setConfig(configData);
    }



    public enum Complexity implements TextProvider
    {
        OFF("Off"),
        SIMPLE("Simple"),
        ADVANCED("Advanced");

        private final String name;

        Complexity(String name) {
            this.name = name;
        }

        public String getLocalizedName() {
            return this.name;
        }
    }

    public enum Quality implements TextProvider {
        OFF("Off"),
        FAST("Fast"),
        FANCY("Fancy");

        private final String name;

        Quality(String name) {
            this.name = name;
        }

        public String getLocalizedName() {
            return this.name;
        }
    }

    public enum ZoomTransitionOptions {
        OFF,
        SMOOTH
    }

    public enum ZoomModes {
        HOLD,
        TOGGLE,
        PERSISTENT
    }

    public enum CinematicCameraOptions {
        OFF,
        VANILLA,
        MULTIPLIED
    }

    public static class ZoomValues {
        public final double zoomDivisor = 4.0;
        public final double minimumZoomDivisor = 1.0;

        public final double maximumZoomDivisor = 50.0;
        public final double scrollStep = 1.0;
        public final double lesserScrollStep = 0.5;
        public final double cinematicMultiplier = 4.0;
        public final double smoothMultiplier = 0.75;
        public double minimumLinearStep = 0.125;
        public double maximumLinearStep = 0.25;
    }
}
