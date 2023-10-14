package com.teampotato.embeddiumextras.features.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;

public interface ChunkDrawParamsVectorExt {
    void ee$pushChunkDrawParamFadeIn(float paramFloat);

    static ChunkDrawParamsVectorExt ext(ChunkDrawParamsVector self) {
        return (ChunkDrawParamsVectorExt)self;
    }
}
