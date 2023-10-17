package com.teampotato.embeddiumextras;

import com.teampotato.embeddiumextras.config.EntityListConfig;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

@Mod(EmbeddiumExtras.MODID)
public class EmbeddiumExtras
{
    public static final String MODID = "magnesium_extras";
    public static final KeyBinding zoomKey = new KeyBinding("extras.key.zoom", KeyConflictContext.IN_GAME, KeyModifier.NONE, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_C, "extras.key.category");

    public EmbeddiumExtras() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        EmbeddiumExtrasConfig.loadConfig(FMLPaths.CONFIGDIR.get().resolve("embeddium_extras.toml"));
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, EntityListConfig.ENTITY_LIST_CONFIG, "embeddium_extras_entitylist.toml");
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
    }

    public void onClientSetup(@NotNull FMLClientSetupEvent event) {
        event.enqueueWork(() -> ClientRegistry.registerKeyBinding(zoomKey));
    }
}