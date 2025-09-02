package me.kall.embeddiumextras.features.fade;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;

public interface ChunkDrawParamsVectorExt {
    void extras$pushChunkDrawParamFadeIn(float progress);

    static ChunkDrawParamsVectorExt ext(ChunkDrawParamsVector self) {
        return (ChunkDrawParamsVectorExt) self;
    }
}