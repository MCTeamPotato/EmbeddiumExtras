package com.teampotato.embeddiumextras.mixins.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import com.teampotato.embeddiumextras.features.fadeinchunks.ChunkDrawParamsVectorExt;

@Mixin(value = {ChunkDrawParamsVector.UnsafeChunkDrawCallVector.class}, remap = false)
public abstract class UnsafeChunkDrawCallVectorMixin extends ChunkDrawParamsVector implements ChunkDrawParamsVectorExt {
    @Shadow
    private long writePointer;

    protected UnsafeChunkDrawCallVectorMixin(int capacity) {
        super(capacity);
    }

    public void rbpe$pushChunkDrawParamFadeIn(float progress) {
        MemoryUtil.memPutFloat(this.writePointer - 4L, progress);
    }
}

