package com.teampotato.embeddiumextras.mixins.fadeinchunks;

import com.teampotato.embeddiumextras.features.fadeinchunks.ChunkDrawParamsVectorExt;
import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = {ChunkDrawParamsVector.UnsafeChunkDrawCallVector.class}, remap = false)
public abstract class UnsafeChunkDrawCallVectorMixin extends ChunkDrawParamsVector implements ChunkDrawParamsVectorExt {
    @Shadow
    private long writePointer;

    protected UnsafeChunkDrawCallVectorMixin(int capacity) {
        super(capacity);
    }

    public void ee$pushChunkDrawParamFadeIn(float progress) {
        MemoryUtil.memPutFloat(this.writePointer - 4L, progress);
    }
}

