package me.kall.embeddiumextras.mixin.dist;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @WrapMethod(method = "shouldRender")
    private <E extends Entity> boolean onCheck(E entity, Frustum frustum, double camX, double camY, double camZ, Operation<Boolean> original) {
        return (((Renderable)entity.getType()).dist$alwaysRenderable() || !ExtrasConfig.ENABLE_ENTITY_DIST_CHECK.get() || ((Renderable.Entity)entity).dist$renderable()) ? original.call(entity, frustum, camX, camY, camZ) : false;
    }
}
