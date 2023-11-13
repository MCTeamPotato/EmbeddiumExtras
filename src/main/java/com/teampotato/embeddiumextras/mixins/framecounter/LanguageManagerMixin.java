package com.teampotato.embeddiumextras.mixins.framecounter;

import com.teampotato.embeddiumextras.features.framecounter.FpsBarInfoProvider;
import net.minecraft.client.resources.LanguageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LanguageManager.class)
public abstract class LanguageManagerMixin {
    @Inject(method = "onResourceManagerReload", at = @At("RETURN"))
    private void reload(CallbackInfo ci) {
        FpsBarInfoProvider.splitChar = null;
        FpsBarInfoProvider.fpsAvg = null;
        FpsBarInfoProvider.fpsMin = null;
        FpsBarInfoProvider.fpsNow = null;
        FpsBarInfoProvider.playTime = null;
        FpsBarInfoProvider.usedMemory = null;
    }
}
