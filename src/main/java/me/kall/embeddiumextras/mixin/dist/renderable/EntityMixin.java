package me.kall.embeddiumextras.mixin.dist.renderable;

import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import me.kall.embeddiumextras.features.fps.FpsBarInfoProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements Renderable.Entity {
    @Shadow public abstract Vec3 position();

    @Unique private volatile boolean dist$renderable = true;
    @Unique private int dist$renderCallCount;

    @Override
    public boolean dist$renderable() {
        return this.dist$renderable;
    }

    @Inject(method = "shouldRender", at = @At("RETURN"))
    private void onRenderCall(CallbackInfoReturnable<Boolean> cir) {
        if (ExtrasConfig.ENABLE_ENTITY_DIST_CHECK.get()) {
            this.dist$renderCallCount++;
            if (this.dist$renderCallCount >= FpsBarInfoProvider.getAvgFps()) {
                this.dist$renderCallCount = 0;
                Vec3 camera = Renderable.renderInfo();
                this.dist$renderable = Renderable.isEntityInDist(this.position(), camera.x, camera.y, camera.z, ExtrasConfig.ENTITY_MAX_RENDERABLE_HEIGHT.get(), ExtrasConfig.ENTITY_MAX_RENDERABLE_DIST_SQR.get());
            }
        }
    }
}
