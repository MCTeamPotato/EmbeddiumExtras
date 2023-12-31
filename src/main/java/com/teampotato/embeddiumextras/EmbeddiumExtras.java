package com.teampotato.embeddiumextras;

import com.google.common.collect.Sets;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import com.teampotato.embeddiumextras.features.fastchest.FastChestContainer;
import com.teampotato.embeddiumextras.features.videotape.MemoryCleaner;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.Set;

@Mod(EmbeddiumExtras.MOD_ID)
public class EmbeddiumExtras {
    public static final String MOD_ID = "embeddiumextras";
    public static final KeyBinding ZOOM_KEY = new KeyBinding("extras.key.zoom", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "extras.key.category");

    public EmbeddiumExtras() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        if (FMLLoader.getDist().isClient()) {
            MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, MemoryCleaner::onClientTick);
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
            EmbeddiumExtrasConfig.loadConfig(FMLLoader.getGamePath().resolve("config").resolve("embeddium_extras.toml"));
        }
    }

    //not configurable because this may not work with mods chests.
    private static final Set<String> FAST_CHEST_ENABLED_TILE_ENTITIES = Sets.newHashSet("minecraft:chest", "minecraft:ender_chest", "minecraft:trapped_chest");

    public void onClientSetup(@NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(ZOOM_KEY);
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES) {
                ResourceLocation id = entityType.getRegistryName();
                if (id != null) ((RenderChecker)entityType).ee$setShouldAlwaysRender(EmbeddiumExtrasConfig.entityList.get().contains(id.toString()) || EmbeddiumExtrasConfig.entityModIdList.get().contains(id.getNamespace()));
            }
            for (TileEntityType<?> entityType : ForgeRegistries.TILE_ENTITIES) {
                ResourceLocation id = entityType.getRegistryName();
                if (id != null) {
                    ((RenderChecker)entityType).ee$setShouldAlwaysRender(EmbeddiumExtrasConfig.tileEntityList.get().contains(id.toString()) || EmbeddiumExtrasConfig.tileEntityModIdList.get().contains(id.getNamespace()));
                    ((FastChestContainer)entityType).ee$setCanBeImpactedByFastChest(FAST_CHEST_ENABLED_TILE_ENTITIES.contains(id.toString()));
                }
            }
        });
    }
}