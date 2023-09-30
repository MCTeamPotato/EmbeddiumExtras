package vice.magnesium_extras.features.zoom;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vice.magnesium_extras.MagnesiumExtras;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;

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

        if (MagnesiumExtras.zoomKey.isDown() == lastZoomPress) return;
        if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.HOLD.toString())) {
            ZoomUtils.zoomState = MagnesiumExtras.zoomKey.isDown();
            ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
        } else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.TOGGLE.toString())) {
            if (MagnesiumExtras.zoomKey.isDown()) {
                ZoomUtils.zoomState = !ZoomUtils.zoomState;
                ZoomUtils.zoomDivisor = MagnesiumExtrasConfig.zoomValues.zoomDivisor;
            }
        } else if (MagnesiumExtrasConfig.zoomMode.get().equals(MagnesiumExtrasConfig.ZoomModes.PERSISTENT.toString())) {
            ZoomUtils.zoomState = true;
        }
        ZoomUtils.lastZoomState = !ZoomUtils.zoomState && lastZoomPress;
        lastZoomPress = MagnesiumExtras.zoomKey.isDown();
    }
}