package com.teampotato.embeddiumextras.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.jellysquid.mods.sodium.client.gui.options.TextProvider;
import net.minecraftforge.common.ForgeConfigSpec;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

@SuppressWarnings("unused")
public class EmbeddiumExtrasConfig {
    public static final ForgeConfigSpec CONFIG_SPEC;

    public static final ZoomValues ZOOM_VALUES = new ZoomValues();

    public static ConfigValue<String> fadeInQuality;
    public static ConfigValue<String> fpsCounterMode;
    public static ConfigValue<Boolean> fpsCounterAlignRight;
    public static ConfigValue<Integer> fpsCounterPosition;

    public static ConfigValue<Boolean> hideJeiItems;

    public static ConfigValue<Integer> maxTileEntityRenderDistanceSquare, maxTileEntityRenderDistanceY, maxEntityRenderDistanceSquare, maxEntityRenderDistanceY;

    public static ConfigValue<Boolean> enableDistanceChecks, enableFastChest, fixGPUMemoryLeak;
    public static ConfigValue<String> zoomTransition, zoomMode, cinematicCameraMode;
    public static ConfigValue<Boolean> zoomScrolling, zoomOverlay, lowerZoomSensitivity, shutUpGLError;
    public static ConfigValue<List<? extends String>> entityList, entityModIdList, tileEntityList, tileEntityModIdList;

    static {
        final ConfigBuilder builder = new ConfigBuilder("Embeddium Extra Settings");

        builder.block("Misc", b -> {
            shutUpGLError = b.define("Disable GL Error 65539 Invalid key -1 when pressing keys (There was never a real error going on here - not the kind you should have to worry about and definitely not the kind that should pollute logs constantly.)", true);
            fixGPUMemoryLeak = b.define("Enable GPU Memory Leak Fix", true);
            enableFastChest = b.comment("FastChest helps by removing their dynamic models (aka BlockEntityRenderer) and making them render as static chunk geometry, like most normal blocks. This means they will lose their lid opening animation!").define("Enable Fast Chest", false);
            fadeInQuality = b.define("Chunk Fade In Quality (OFF, FAST, FANCY)", "FANCY");
            hideJeiItems = b.comment("Tweak JEI rendering so that the item list does not display unless search is active. This de-clutters the screen significantly unless you're actively using JEI.").define("Hide JEI Items", false);
        });

        builder.block("FPS Counter", b -> {
            fpsCounterMode = b.define("Display FPS Counter (OFF, SIMPLE, ADVANCED)", "ADVANCED");
            fpsCounterAlignRight = b.define("Right-align FPS Counter", false);
            fpsCounterPosition = b.define("FPS Counter Distance", 12);
        });

        builder.block("Entity Distance", b -> {
            enableDistanceChecks = b.define("Enable Max Distance Checks", true);

            maxTileEntityRenderDistanceSquare = b.define("(TileEntity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxTileEntityRenderDistanceY = b.define("(TileEntity) Max Vertical Render Distance [Raw, Default 32]", 32);

            maxEntityRenderDistanceSquare = b.define("(Entity) Max Horizontal Render Distance [Squared, Default 64^2]", 4096);
            maxEntityRenderDistanceY = b.define("(Entity) Max Vertical Render Distance [Raw, Default 32]", 32);
        });

        builder.block("Zoom", b -> {
            lowerZoomSensitivity = b.define("Lower Zoom Sensitivity", true);
            zoomScrolling = b.define("Zoom Scrolling Enabled", true);
            zoomTransition = b.define("Zoom Transition Mode (OFF, LINEAR, SMOOTH)", ZoomTransitionOptions.SMOOTH.toString());
            zoomMode = b.define("Zoom Transition Mode (TOGGLE, HOLD, PERSISTENT)", ZoomModes.HOLD.toString());
            cinematicCameraMode = b.define("Cinematic Camera Mode (OFF, VANILLA, MULTIPLIED)", CinematicCameraOptions.OFF.toString());
            zoomOverlay = b.define("Enable Zoom Overlay", true);
        });

        builder.block("Entity List", b -> {
            final Predicate<Object> validator = o -> o instanceof String;
            final List<? extends String> tileEntityList = ObjectArrayList.wrap(new String[]{"minecraft:beacon"});
            final List<? extends String> tileEntityModidList = ObjectArrayList.wrap(new String[]{});
            final List<? extends String> entityModidList = ObjectArrayList.wrap(new String[]{"witherstormmod"});
            final List<? extends String> entityList = ObjectArrayList.wrap(new String[] {"minecraft:ender_dragon", "minecraft:ghast", "alexsmobs:void_worm", "alexsmobs:void_worm_part", "minecraft:wither", "twilightforest:naga", "twilightforest:lich", "twilightforest:yeti", "twilightforest:snow_queen", "twilightforest:minoshroom", "twilightforest:hydra", "twilightforest:knight_phantom", "twilightforest:ur_ghast", "atum:pharaoh", "mowziesmobs:barako", "mowziesmobs:ferrous_wroughtnaut", "mowziesmobs:frostmaw", "aoa3:skeletron", "aoa3:smash", "aoa3:baroness", "aoa3:clunkhead", "aoa3:corallus", "aoa3:cotton_candor", "aoa3:craexxeus", "aoa3:xxeus", "aoa3:creep", "aoa3:crystocore", "aoa3:dracyon", "aoa3:graw", "aoa3:gyro", "aoa3:hive_king", "aoa3:kajaros", "aoa3:miskel", "aoa3:harkos", "aoa3:raxxan", "aoa3:okazor", "aoa3:king_bambambam", "aoa3:king_shroomus", "aoa3:kror", "aoa3:mechbot", "aoa3:nethengeic_wither", "aoa3:red_guardian", "aoa3:blue_guardian", "aoa3:green_guardian", "aoa3:yellow_guardian", "aoa3:rock_rider", "aoa3:shadowlord", "aoa3:tyrosaur", "aoa3:vinecorne", "aoa3:visualent", "aoa3:voxxulon", "aoa3:bane", "aoa3:elusive", "gaiadimension:malachite_drone", "gaiadimension:malachite_guard", "blue_skies:alchemist", "blue_skies:arachnarch", "blue_skies:starlit_crusher", "blue_skies:summoner", "stalwart_dungeons:awful_ghast", "stalwart_dungeons:nether_keeper", "stalwart_dungeons:shelterer_without_armor", "dungeonsmod:extrapart", "dungeonsmod:king", "dungeonsmod:deserted", "dungeonsmod:crawler", "dungeonsmod:ironslime", "dungeonsmod:kraken", "dungeonsmod:voidmaster", "dungeonsmod:lordskeleton", "dungeonsmod:winterhunter", "dungeonsmod:sun", "forestcraft:beequeen", "forestcraft:iguana_king", "forestcraft:cosmic_fiend", "forestcraft:nether_scourge", "cataclysm:ender_golem", "cataclysm:ender_guardian", "cataclysm:ignis", "cataclysm:ignited_revenant", "cataclysm:netherite_monstrosity", "iceandfire:fire_dragon", "iceandfire:ice_dragon", "iceandfire:lightning_dragon", "iceandfire:dragon_multipart", "alexsmobs:spectre", "mowziesmobs:naga"});
            EmbeddiumExtrasConfig.entityList = b.defineList("entities which should always be rendered", entityList, validator);
            EmbeddiumExtrasConfig.entityModIdList = b.defineList("modid(entities of this mod will always be rendered)", entityModidList, validator);
            EmbeddiumExtrasConfig.tileEntityList = b.defineList("tile entities which should always be rendered", tileEntityList, validator);
            EmbeddiumExtrasConfig.tileEntityModIdList = b.defineList("modid(tile entities of this mod will always be rendered)", tileEntityModidList, validator);
        });

        CONFIG_SPEC = builder.save();
    }

    public static void loadConfig(Path path) {
        final CommentedFileConfig configData = CommentedFileConfig.builder(path).sync().autosave().writingMode(WritingMode.REPLACE).build();

        configData.load();
        CONFIG_SPEC.setConfig(configData);
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
