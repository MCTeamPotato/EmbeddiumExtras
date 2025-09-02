package me.kall.embeddiumextras.mixin.dist.renderable;

import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin implements Renderable {
    @Unique private boolean dist$alwaysRenderable;

    @Override
    public boolean dist$alwaysRenderable() {
        return this.dist$alwaysRenderable;
    }

    @Override
    public void dist$setAlwaysRenderable(boolean alwaysRenderable) {
        this.dist$alwaysRenderable = alwaysRenderable;
    }
}
