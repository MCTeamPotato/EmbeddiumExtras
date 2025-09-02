package me.kall.embeddiumextras.features.fastchest;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class FastChestInformer {
    public static final TranslatableComponent ENABLE = new TranslatableComponent("extras.fast_chest.note.enable");
    public static final TranslatableComponent DISABLE = new TranslatableComponent("extras.fast_chest.note.disable");

    public static boolean shouldInform = false;
    public static String informType = "none";

    public static void onClientTick(TickEvent.@NotNull ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (shouldInform && event.phase == TickEvent.Phase.START && minecraft.player != null && minecraft.level != null && !informType.equals("none")) {
            minecraft.player.displayClientMessage(informType.equals("true") ? ENABLE : DISABLE, false);
            shouldInform = false;
            informType = "none";
        }
    }
}
