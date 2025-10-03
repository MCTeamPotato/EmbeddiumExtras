package me.kall.embeddiumextras.mixin.dist;

import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getViewDistance()D", shift = At.Shift.AFTER), cancellable = true)
    private <E extends BlockEntity> void after(E blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, CallbackInfo ci) {
        if (ExtrasConfig.ENABLE_TILE_ENTITY_DIST_CHECK.get() || ((Renderable)blockEntity.getType()).dist$alwaysRenderable() || ((Renderable.Entity)blockEntity).dist$renderable()) return;
        ci.cancel();
    }
}
