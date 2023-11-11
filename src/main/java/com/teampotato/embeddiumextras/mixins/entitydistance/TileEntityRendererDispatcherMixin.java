package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;

@Mixin(TileEntityRendererDispatcher.class)
public abstract class TileEntityRendererDispatcherMixin
{

    @Shadow public ActiveRenderInfo camera;

    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    public <E extends TileEntity> void render(E entity, float val, MatrixStack matrix, IRenderTypeBuffer p_228850_4_, CallbackInfo ci) {
        if (!EmbeddiumExtrasConfig.enableDistanceChecks.get() || ((RenderChecker)entity.getType()).ee$shouldAlwaysRender() || RenderChecker.isEntityWithinDistance(entity.getBlockPos(), this.camera.getPosition(), EmbeddiumExtrasConfig.maxTileEntityRenderDistanceY.get(), EmbeddiumExtrasConfig.maxTileEntityRenderDistanceSquare.get())) return;
        ci.cancel();
    }
}
