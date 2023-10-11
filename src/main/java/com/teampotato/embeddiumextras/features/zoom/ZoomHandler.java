package com.teampotato.embeddiumextras.features.zoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.teampotato.embeddiumextras.EmbeddiumExtras;
import com.teampotato.embeddiumextras.config.MagnesiumExtrasConfig;

@Mod.EventBusSubscriber(modid = "magnesium_extras", bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ZoomHandler {
    private static boolean lastZoomPress = false;
    private static boolean persistentZoom = false;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (!MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.HOLD.toString())) {
            if (!persistentZoom) {
                persistentZoom = true;
                lastZoomPress = true;
                ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
            }
        } else {
            if (persistentZoom) {
                persistentZoom = false;
                lastZoomPress = true;
            }
        }

        if (EmbeddiumExtras.zoomKey.isDown() == lastZoomPress) return;
        if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.HOLD.toString())) {
            ZoomUtils.zoomState = EmbeddiumExtras.zoomKey.isDown();
            ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
        } else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.TOGGLE.toString())) {
            if (EmbeddiumExtras.zoomKey.isDown()) {
                ZoomUtils.zoomState = !ZoomUtils.zoomState;
                ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
            }
        } else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
            ZoomUtils.zoomState = true;
        }
        ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress;
        lastZoomPress = EmbeddiumExtras.zoomKey.isDown();
    }
}