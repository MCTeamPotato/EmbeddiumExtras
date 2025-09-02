package me.kall.embeddiumextras.mixin.fade;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;
import me.kall.embeddiumextras.features.fade.ChunkDrawParamsVectorExt;
import org.lwjgl.system.MemoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ChunkDrawParamsVector.UnsafeChunkDrawCallVector.class, remap = false)
public abstract class UnsafeChunkDrawCallVectorMixin extends ChunkDrawParamsVector implements ChunkDrawParamsVectorExt {
    @Shadow private long writePointer;

    protected UnsafeChunkDrawCallVectorMixin(int capacity) {
        super(capacity);
    }

    @Override
    public void extras$pushChunkDrawParamFadeIn(float progress) {
        MemoryUtil.memPutFloat(this.writePointer - 4L, progress);
    }
}
