package com.teampotato.embeddiumextras.mixins.gpumemleakfix;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.gpumemleakfix.ClientEventHandler;
import com.teampotato.embeddiumextras.features.gpumemleakfix.FramebufferIdsContainer;
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
            if (EmbeddiumExtrasConfig.fixGPUMemoryLeak.get() && (this.depthBufferId > -1 || this.colorTextureId > -1 || this.frameBufferId > -1)) {
                ClientEventHandler.IDS_CONTAINERS.add(new FramebufferIdsContainer(this.depthBufferId, this.colorTextureId, this.frameBufferId));
            }
        } finally {
            try {
                super.finalize();
            } catch (Throwable ignored) {}
        }
    }
}
