package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.IRendererManager;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class TileEntityRendererDispatcherMixin implements IRendererManager {
    @Shadow public ActiveRenderInfo camera;
    @Unique private int ee$maxHeight, ee$maxDistanceSquare;
    @Unique private boolean ee$shouldCull;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        this.ee$shouldCull = EmbeddiumExtrasConfig.enableDistanceChecks.get();
        this.ee$maxHeight = EmbeddiumExtrasConfig.maxTileEntityRenderDistanceY.get();
        this.ee$maxDistanceSquare = EmbeddiumExtrasConfig.maxTileEntityRenderDistanceSquare.get();
    }

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends TileEntity> void render(E entity, float val, MatrixStack matrix, IRenderTypeBuffer buffer, CallbackInfo ci) {
        if (((RenderChecker) entity.getType()).ee$shouldAlwaysRender()) return;
        if (RenderChecker.isEntityWithinDistance(entity.getBlockPos(), this.camera.getPosition(), this.ee$maxHeight, this.ee$maxDistanceSquare)) return;
        if (this.ee$shouldCull()) ci.cancel();
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
