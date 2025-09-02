package me.kall.embeddiumextras.mixin.dist;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Shadow public Camera camera;

    @WrapMethod(method = "render")
    private <E extends BlockEntity> void onRender(E blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, Operation<Void> original) {
        if (ExtrasConfig.ENABLE_TILE_ENTITY_DIST_CHECK.get() || ((Renderable)blockEntity.getType()).dist$alwaysRenderable() || Renderable.isEntityInDist(blockEntity.getBlockPos(), this.camera.getPosition(), ExtrasConfig.TILE_MAX_RENDERABLE_HEIGHT.get(), ExtrasConfig.TILE_MAX_RENDERABLE_DIST_SQR.get())) original.call(blockEntity, partialTicks, matrixStack, buffer);
    }
}
