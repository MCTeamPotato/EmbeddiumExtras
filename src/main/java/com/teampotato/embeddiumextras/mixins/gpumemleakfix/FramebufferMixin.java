package com.teampotato.embeddiumextras.mixins.gpumemleakfix;

import com.teampotato.embeddiumextras.features.gpumemleakfix.MemoryCleaner;
import net.minecraft.client.shader.Framebuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Framebuffer.class)
public abstract class FramebufferMixin {
    @Shadow private int depthBufferId;

    @Shadow private int colorTextureId;

    @Shadow public int frameBufferId;

    @Override
    protected void finalize() {
        try {
            MemoryCleaner.onFramebufferFinalize(this.depthBufferId, this.colorTextureId, this.frameBufferId);
        } catch (Throwable ignored) {} finally {
            try {
                super.finalize();
            } catch (Throwable ignored) {}
        }
    }
}
