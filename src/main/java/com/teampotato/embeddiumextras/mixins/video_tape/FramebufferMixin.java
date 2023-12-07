package com.teampotato.embeddiumextras.mixins.video_tape;

import com.teampotato.embeddiumextras.features.videotape.MemoryCleaner;
import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Framebuffer.class)
public abstract class FramebufferMixin {
    @Shadow private int depthBufferId;

    @Shadow private int colorTextureId;

    @Shadow public int frameBufferId;

    @Override
    protected void finalize() throws Throwable {
        try {
            MemoryCleaner.onFramebufferFinalize(this.depthBufferId, this.colorTextureId, this.frameBufferId);
        } catch (Throwable e) {
            MemoryCleaner.LOGGER.error("Error occurs during Framebuffer finalization", e);
        }
        super.finalize();
    }
}
