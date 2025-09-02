package me.kall.embeddiumextras;

import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.fastchest.FastChestInformer;
import me.kall.embeddiumextras.features.tape.MemoryCleaner;
import me.kall.embeddiumextras.features.zoom.ZoomManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(EmbeddiumExtras.MOD_ID)
public final class EmbeddiumExtras {
    public static final String MOD_ID = "embeddiumextras";
    public static final String MOD_NAME = "EmbeddiumExtras";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);
    private static boolean note;

    public EmbeddiumExtras() {
        if (!FMLLoader.getDist().isClient()) return;
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ExtrasConfig.INSTANCE);
        ClientRegistry.registerKeyBinding(ZoomManager.ZOOM);
        MinecraftForge.EVENT_BUS.addListener(FastChestInformer::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(MemoryCleaner::onClientTick);
        MinecraftForge.EVENT_BUS.addListener(this::onClientTick);
    }

    public void onClientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.level != null && !note && ExtrasConfig.NOTE.get()) {
            note = true;
            minecraft.player.displayClientMessage(new TranslatableComponent("extras.note"), false);
        }
    }
}
