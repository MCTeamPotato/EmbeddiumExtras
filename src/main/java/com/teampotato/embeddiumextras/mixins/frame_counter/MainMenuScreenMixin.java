package com.teampotato.embeddiumextras.mixins.frame_counter;

import com.teampotato.embeddiumextras.features.framecounter.FpsBarInfoProvider;
import net.minecraft.client.gui.screen.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDateTime;

@Mixin(MainMenuScreen.class)
public abstract class MainMenuScreenMixin {
    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(CallbackInfo ci) {
        if (FpsBarInfoProvider.dateStarted == null) FpsBarInfoProvider.dateStarted = LocalDateTime.now();
    }
}
