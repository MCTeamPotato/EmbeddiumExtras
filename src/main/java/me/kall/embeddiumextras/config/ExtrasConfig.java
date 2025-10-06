package me.kall.embeddiumextras.config;

import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.*;

import java.util.List;

public class ExtrasConfig {
    public static final ForgeConfigSpec INSTANCE;

    public static final ConfigValue<FpsMode> FPS_DISPLAY_MODE;
    public static final BooleanValue FPS_ALIGN_RIGHT;
    public static final IntValue FPS_POS_X_OFFSET;
    public static final IntValue FPS_POS_Y_OFFSET;
    public static final BooleanValue SHOW_GPU_INFO;
    public static final BooleanValue SHOW_MEMORY_INFO;
    public static final BooleanValue SHOW_GAME_TIME;

    public static final BooleanValue CLEAR_SKIES;

    public static final BooleanValue HIDE_JEI_ITEMS;

    public static final BooleanValue ZOOM;
    public static final DoubleValue MAX_ZOOM_SCALE;

    public static final BooleanValue ENABLE_ENTITY_DIST_CHECK;
    public static final IntValue ENTITY_MAX_RENDERABLE_HEIGHT;
    public static final IntValue ENTITY_MAX_RENDERABLE_DIST_SQR;
    public static final ConfigValue<List<? extends String>> ENTITIES_ALWAYS_RENDERABLE;

    public static final BooleanValue ENABLE_TILE_ENTITY_DIST_CHECK;
    public static final IntValue TILE_MAX_RENDERABLE_HEIGHT;
    public static final IntValue TILE_MAX_RENDERABLE_DIST_SQR;
    public static final ConfigValue<List<? extends String>> TILES_ALWAYS_RENDERABLE;

    public static final BooleanValue FAST_CHEST;
    public static final BooleanValue MEMORY_LEAK_FIX;
    public static final BooleanValue SHUT_UP_GL_ERROR;

    public static final BooleanValue FADE_IN_CHUNKS;
    public static final IntValue FADE_IN_TIME;

    public static final BooleanValue NOTE;

    public static final BooleanValue NIGHT_VISION;

    static {
        Builder builder = new Builder();
        builder.push("EmbeddiumExtras");

        builder.push("FadeInChunks");
        FADE_IN_CHUNKS = builder.define("EnableFadeInChunks", false);
        FADE_IN_TIME = builder.defineInRange("ChunksFadeInAnimationSpeedModifier", 10, 0, 30);
        builder.pop();

        builder.push("RenderDistance");
        ENABLE_ENTITY_DIST_CHECK = builder.define("EnableEntityDistanceCheck", false);
        ENTITY_MAX_RENDERABLE_HEIGHT = builder.defineInRange("EntityMaxRenderableHeight", 32, 0, Integer.MAX_VALUE);
        ENTITY_MAX_RENDERABLE_DIST_SQR = builder.defineInRange("EntityMaxRenderableDistanceSquared", 4096, 0, Integer.MAX_VALUE);
        ENTITIES_ALWAYS_RENDERABLE = builder.comment("Accept both modID and registry name").defineList("EntitiesAlwaysRenderable", Lists.newArrayList(), Predicates.alwaysTrue());
        ENABLE_TILE_ENTITY_DIST_CHECK = builder.define("EnableTileEntityDistanceCheck", false);
        TILE_MAX_RENDERABLE_HEIGHT = builder.defineInRange("TileMaxRenderableHeight", 32, 0, Integer.MAX_VALUE);
        TILE_MAX_RENDERABLE_DIST_SQR = builder.defineInRange("TileMaxRenderableDistanceSquared", 4096, 0, Integer.MAX_VALUE);
        TILES_ALWAYS_RENDERABLE = builder.comment("Accept both modID and registry name").defineList("TilesAlwaysRenderable", Lists.newArrayList(), Predicates.alwaysTrue());
        builder.pop();

        builder.push("FpsDisplay");
        FPS_DISPLAY_MODE = builder.defineEnum("FpsDisplayMode", FpsMode.OFF);
        FPS_ALIGN_RIGHT = builder.define("DisplayFpsAtRightSide", false);
        FPS_POS_X_OFFSET = builder.defineInRange("FpsDisplayPositionXOffset", 12, 0, Integer.MAX_VALUE);
        FPS_POS_Y_OFFSET = builder.defineInRange("FpsDisplayPositionYOffset", 12, 0, Integer.MAX_VALUE);
        SHOW_GPU_INFO = builder.define("ShowGpuInfo", false);
        SHOW_MEMORY_INFO = builder.define("ShowMemoryInfo", false);
        SHOW_GAME_TIME = builder.define("ShowGameTime", false);
        builder.pop();

        builder.push("Misc");
        NOTE = builder.define("Note", true);
        SHUT_UP_GL_ERROR = builder.define("ShutUpGLError", false);
        MEMORY_LEAK_FIX = builder.define("PotentialMemoryLeakFix", false);
        FAST_CHEST = builder.define("FastChest", false);
        HIDE_JEI_ITEMS = builder.comment("Stop rendering the items in Just Enough Items GUI unless you are searching").define("HideJEIItems", false);
        CLEAR_SKIES = builder.comment("Removes the banding at the horizon of Vanilla Minecraft").define("EnableClearSkies", false);
        NIGHT_VISION = builder.define("EnableNightVision", false);
        builder.pop();

        builder.push("Zoom");
        ZOOM = builder.define("EnableZoom", false);
        MAX_ZOOM_SCALE = builder.comment("Default: Double#MAX_VALUE").defineInRange("MaxZoomScale", Double.MAX_VALUE, 50, Double.MAX_VALUE);
        builder.pop();

        builder.pop();
        INSTANCE = builder.build();
    }

    public enum FpsMode {
        OFF, SIMPLE, ADVANCED
    }
}
