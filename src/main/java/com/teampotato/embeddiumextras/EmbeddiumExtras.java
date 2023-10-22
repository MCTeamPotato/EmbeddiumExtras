package com.teampotato.embeddiumextras;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.EntityType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

@Mod(EmbeddiumExtras.MOD_ID)
public class EmbeddiumExtras {
    public static final String MOD_ID = "embeddiumextras";
    public static final KeyBinding ZOOM_KEY = new KeyBinding("extras.key.zoom", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "extras.key.category");

    public EmbeddiumExtras() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        EmbeddiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("embeddium_extras.toml"));
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    public void onClientSetup(@NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ClientRegistry.registerKeyBinding(ZOOM_KEY);
            for (EntityType<?> entityType : ForgeRegistries.ENTITIES) {
                ResourceLocation id = entityType.getRegistryName();
                if (id != null) ((RenderChecker)entityType).ee$setShouldAlwaysRender(EmbeddiumExtrasConfig.entityList.get().contains(id.toString()) || EmbeddiumExtrasConfig.entityModIdList.get().contains(id.getNamespace()));
            }
            for (TileEntityType<?> entityType : ForgeRegistries.TILE_ENTITIES) {
                ResourceLocation id = entityType.getRegistryName();
                if (id != null) ((RenderChecker)entityType).ee$setShouldAlwaysRender(EmbeddiumExtrasConfig.tileEntityList.get().contains(id.toString()) || EmbeddiumExtrasConfig.tileEntityModIdList.get().contains(id.getNamespace()));
            }
        });
    }
}