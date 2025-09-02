package me.kall.embeddiumextras.features.fade;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import me.kall.embeddiumextras.config.ExtrasConfig;

import java.util.Objects;

public interface ChunkGraphicsStateExt {
    ChunkRenderContainer<?> extras$getContainer();

    float extras$getLoadTime();

    void extras$setLoadTime(float loadTime);

    default float getFadeInProgress(float currentTime) {
        Integer fadeInTime = ExtrasConfig.FADE_IN_TIME.get();
        if (Objects.equals(fadeInTime, 30)) return Float.POSITIVE_INFINITY;
        return (currentTime - extras$getLoadTime()) * (fadeInTime.floatValue() / 10.0F + 0.1F);
    }

    static ChunkGraphicsStateExt ext(ChunkGraphicsState self) {
        return (ChunkGraphicsStateExt) self;
    }
}
