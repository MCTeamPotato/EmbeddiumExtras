package com.teampotato.embeddiumextras.features.entitydistance;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public interface RenderChecker {
    @Nullable Boolean ee$shouldAlwaysRender();
    void ee$setShouldAlwaysRender(@Nullable Boolean shouldAlwaysRender);
}
