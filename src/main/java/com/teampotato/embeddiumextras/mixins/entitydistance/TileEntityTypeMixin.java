package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TileEntityType.class)
public class TileEntityTypeMixin implements RenderChecker {
    @Unique private boolean ee$shouldAlwaysRender;

    @Override
    public boolean ee$shouldAlwaysRender() {
        return this.ee$shouldAlwaysRender;
    }

    @Override
    public void ee$setShouldAlwaysRender(boolean shouldAlwaysRender) {
        this.ee$shouldAlwaysRender = shouldAlwaysRender;
    }
}
