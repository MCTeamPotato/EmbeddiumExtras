package me.kall.embeddiumextras.mixin.dist;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @WrapMethod(method = "setupAndRender")
    private static <T extends BlockEntity> void onRender(BlockEntityRenderer<T> renderer, T blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, Operation<Void> original) {
        if (ExtrasConfig.ENABLE_TILE_ENTITY_DIST_CHECK.get() || ((Renderable)blockEntity.getType()).dist$alwaysRenderable() || ((Renderable.Entity)blockEntity).dist$renderable()) original.call(renderer, blockEntity, partialTicks, matrixStack, buffer);
    }
}
