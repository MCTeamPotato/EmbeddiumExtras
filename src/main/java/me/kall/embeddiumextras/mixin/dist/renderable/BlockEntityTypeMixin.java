package me.kall.embeddiumextras.mixin.dist.renderable;

import me.kall.embeddiumextras.features.dist.Renderable;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin implements Renderable {
    @Unique
    private boolean dist$alwaysRenderable;

    @Override
    public boolean dist$alwaysRenderable() {
        return this.dist$alwaysRenderable;
    }

    @Override
    public void dist$setAlwaysRenderable(boolean alwaysRenderable) {
        this.dist$alwaysRenderable = alwaysRenderable;
    }
}
