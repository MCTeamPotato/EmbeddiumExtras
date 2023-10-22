package com.teampotato.embeddiumextras.features.framecounter;

import com.teampotato.embeddiumextras.EmbeddiumExtras;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EmbeddiumExtras.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class DebugOverlayImprovements
{
    @SubscribeEvent
    public static void onRenderDebugText(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.DEBUG) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.renderFpsChart) event.setCanceled(true);
    }
}
