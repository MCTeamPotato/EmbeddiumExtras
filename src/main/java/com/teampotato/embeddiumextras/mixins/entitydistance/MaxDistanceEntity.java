package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.config.MagnesiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import com.teampotato.embeddiumextras.util.DistanceUtility;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRendererManager.class)
public abstract class MaxDistanceEntity {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (!MagnesiumExtrasConfig.enableDistanceChecks.get() || ((RenderChecker)entity).ee$shouldAlwaysRender() || DistanceUtility.isEntityWithinDistance(entity, cameraX, cameraY, cameraZ, MagnesiumExtrasConfig.maxEntityRenderDistanceY.get(), MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get())) return;

        cir.setReturnValue(false);
    }
}