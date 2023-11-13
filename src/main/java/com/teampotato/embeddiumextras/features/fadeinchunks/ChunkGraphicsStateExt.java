package com.teampotato.embeddiumextras.features.fadeinchunks;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public interface ChunkGraphicsStateExt {
    ChunkRenderContainer<?> ee$getcontainer();

    float ee$getloadTime();

    void ee$setloadTime(float paramFloat);

    default float getFadeInProgress(float currentTime) {
        Integer fadeInTime = EmbeddiumExtrasConfig.fadeInTime.get();
        if (Objects.equals(fadeInTime, 30)) return Float.POSITIVE_INFINITY;
        return (currentTime - ee$getloadTime()) * (fadeInTime.floatValue() / 10.0F + 0.1F);
    }

    static ChunkGraphicsStateExt ext(ChunkGraphicsState self) {
        return (ChunkGraphicsStateExt)self;
    }
}

