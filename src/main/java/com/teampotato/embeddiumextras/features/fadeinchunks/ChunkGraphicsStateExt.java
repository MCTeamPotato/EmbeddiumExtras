package com.teampotato.embeddiumextras.features.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;

import java.util.Objects;

public interface ChunkGraphicsStateExt {
    ChunkRenderContainer<?> ee$getcontainer();

    float ee$getloadTime();

    void ee$setloadTime(float paramFloat);

    default float getFadeInProgress(float currentTime) {
        String mode = EmbeddiumExtrasConfig.fadeInQuality.get();
        float fadeTime = 2.5F;
        if (!Objects.equals(mode, "FANCY"))
            if (Objects.equals(mode, "OFF")) {
                fadeTime = Float.POSITIVE_INFINITY;
            } else if (Objects.equals(mode, "FAST")) {
                fadeTime = 5.0F;
            }
        return (currentTime - ee$getloadTime()) * fadeTime;
    }

    static ChunkGraphicsStateExt ext(ChunkGraphicsState self) {
        return (ChunkGraphicsStateExt)self;
    }
}

