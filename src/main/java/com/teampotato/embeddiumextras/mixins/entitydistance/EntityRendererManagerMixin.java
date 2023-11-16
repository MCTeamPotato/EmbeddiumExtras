package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.IRendererManager;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRendererManager.class)
public abstract class EntityRendererManagerMixin implements IRendererManager {
    @Unique private int ee$maxHeight, ee$maxDistanceSquare;
    @Unique private boolean ee$shouldCull;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.ee$shouldCull = EmbeddiumExtrasConfig.enableDistanceChecks.get();
        this.ee$maxHeight = EmbeddiumExtrasConfig.maxEntityRenderDistanceY.get();
        this.ee$maxDistanceSquare = EmbeddiumExtrasConfig.maxEntityRenderDistanceSquare.get();
    }

    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (((RenderChecker) entity.getType()).ee$shouldAlwaysRender()) return;
        if (RenderChecker.isEntityWithinDistance(entity, cameraX, cameraY, cameraZ, this.ee$maxHeight, this.ee$maxDistanceSquare)) return;
        if (this.ee$shouldCull()) cir.setReturnValue(false);
    }

    @Override
    public boolean ee$shouldCull() {
        return this.ee$shouldCull;
    }

    @Override
    public void ee$setShouldCull(boolean shouldCull) {
        this.ee$shouldCull = shouldCull;
    }
}