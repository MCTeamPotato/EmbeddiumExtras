package com.teampotato.embeddiumextras.mixins.disable_debug_message;

import net.minecraft.client.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin {
    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 14))
    private int onParsGlDebugVerbosity(String s) {
        return 0;
    }
}
