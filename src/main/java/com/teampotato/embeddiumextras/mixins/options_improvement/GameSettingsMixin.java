package com.teampotato.embeddiumextras.mixins.options_improvement;

import net.minecraft.client.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameSettings.class)
public abstract class GameSettingsMixin {
    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;parseInt(Ljava/lang/String;)I", ordinal = 14))
    private int disableOpenGLDebugMessage(String s) {
        return 0;
    }

    @Redirect(method = "load", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 80))
    private boolean alwaysSkipMultiplayerWarning(String s, Object o) {
        return true;
    }
}
