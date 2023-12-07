package com.teampotato.embeddiumextras.mixins.fade_in_chunks;

import com.teampotato.embeddiumextras.features.fadeinchunks.ChunkDrawParamsVectorExt;
import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {ChunkDrawParamsVector.NioChunkDrawCallVector.class}, remap = false)
public abstract class NioChunkDrawCallVectorMixin extends ChunkDrawParamsVector implements ChunkDrawParamsVectorExt {
    @Shadow
    private int writeOffset;

    protected NioChunkDrawCallVectorMixin(int capacity) {
        super(capacity);
    }

    public void ee$pushChunkDrawParamFadeIn(float progress) {
        this.buffer.putFloat(this.writeOffset - 4, progress);
    }
}
