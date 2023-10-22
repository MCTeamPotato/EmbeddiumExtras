package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public class EntityTypeMixin implements RenderChecker {
    @Unique @Nullable private Boolean ee$shouldAlwaysRender = null;

    @Override
    public @Nullable Boolean ee$shouldAlwaysRender() {
        return this.ee$shouldAlwaysRender;
    }

    @Override
    public void ee$setShouldAlwaysRender(@Nullable Boolean shouldAlwaysRender) {
        this.ee$shouldAlwaysRender = shouldAlwaysRender;
    }
}
