package me.kall.embeddiumextras.mixin.dist.renderable;

import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import me.kall.embeddiumextras.features.fps.FpsBarInfoProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements Renderable.Entity {
    @Shadow protected BlockPos worldPosition;

    @Unique private volatile boolean dist$renderable = true;
    @Unique private int dist$renderCallCount;

    @Override
    public boolean dist$renderable() {
        return this.dist$renderable;
    }

    @Inject(method = "getViewDistance", at = @At("RETURN"))
    private void onRenderCall(CallbackInfoReturnable<Double> cir) {
        if (ExtrasConfig.ENABLE_ENTITY_DIST_CHECK.get()) {
            this.dist$renderCallCount++;
            if (this.dist$renderCallCount >= FpsBarInfoProvider.getAvgFps()) {
                this.dist$renderCallCount = 0;
                Vec3 camera = Renderable.renderInfo();
                this.dist$renderable = Renderable.isEntityInDist(this.worldPosition, camera, ExtrasConfig.ENTITY_MAX_RENDERABLE_HEIGHT.get(), ExtrasConfig.ENTITY_MAX_RENDERABLE_DIST_SQR.get());
            }
        }
    }
}
