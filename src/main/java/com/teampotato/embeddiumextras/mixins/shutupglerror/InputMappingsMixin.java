package com.teampotato.embeddiumextras.mixins.shutupglerror;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import net.minecraft.client.util.InputMappings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InputMappings.class)
public abstract class InputMappingsMixin {
    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    private static void onCheckIsKeyDown(long window, int code, CallbackInfoReturnable<Boolean> cir) {
        if (!EmbeddiumExtrasConfig.shutUpGLError.get()) return;
        if (code == -1 || code == 0) cir.setReturnValue(Boolean.FALSE);
    }
}
