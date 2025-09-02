package me.kall.embeddiumextras.mixin.zoom;

import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.zoom.ZoomManager;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Inject(method = "onScroll", at = @At("RETURN"))
    private void onScroll(long handle, double xOffset, double yOffset, CallbackInfo ci) {
        if (ExtrasConfig.ZOOM.get()) ZoomManager.onMouseScroll(yOffset);
    }
}
