package vice.magnesium_extras.features.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;

public interface ChunkDrawParamsVectorExt {
    void pushChunkDrawParamFadeIn(float paramFloat);

    static ChunkDrawParamsVectorExt ext(ChunkDrawParamsVector self) {
        return (ChunkDrawParamsVectorExt)self;
    }
}
