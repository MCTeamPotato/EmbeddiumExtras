package me.kall.embeddiumextras.mixin.tape;

import com.mojang.blaze3d.pipeline.RenderTarget;
import me.kall.embeddiumextras.features.tape.MemoryCleaner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(RenderTarget.class)
public abstract class RenderTargetMixin {
    @Shadow private int depthBufferId;
    @Shadow private int colorTextureId;
    @Shadow public int frameBufferId;

    @Override
    protected void finalize() throws Throwable {
        MemoryCleaner.onFinalize(this.depthBufferId, this.colorTextureId, this.frameBufferId);
        super.finalize();
    }
}
