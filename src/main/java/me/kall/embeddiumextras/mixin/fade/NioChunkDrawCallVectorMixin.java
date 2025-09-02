package me.kall.embeddiumextras.mixin.fade;

import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.ChunkDrawParamsVector;
import me.kall.embeddiumextras.features.fade.ChunkDrawParamsVectorExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ChunkDrawParamsVector.NioChunkDrawCallVector.class, remap = false)
public abstract class NioChunkDrawCallVectorMixin extends ChunkDrawParamsVector implements ChunkDrawParamsVectorExt {
    @Shadow private int writeOffset;

    protected NioChunkDrawCallVectorMixin(int capacity) {
        super(capacity);
    }

    @Override
    public void extras$pushChunkDrawParamFadeIn(float progress) {
        this.buffer.putFloat(this.writeOffset - 4, progress);
    }
}
