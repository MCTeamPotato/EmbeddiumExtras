package com.teampotato.embeddiumextras.features.entitydistance;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface RenderChecker {
    boolean ee$shouldAlwaysRender();
    void ee$setShouldAlwaysRender(boolean shouldAlwaysRender);
}
