package me.kall.embeddiumextras.mixin.nightvision;

import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public abstract class LightTextureMixin {
    @Shadow @Final private Minecraft minecraft;

    @Inject(method = "updateLightTexture", at = @At("HEAD"))
    private void onCheckGamma(float partialTicks, CallbackInfo ci) {
        if (this.minecraft.options.gamma != 15D && ExtrasConfig.NIGHT_VISION.get()) {
            this.minecraft.options.gamma = 15D;
        }
    }
}
