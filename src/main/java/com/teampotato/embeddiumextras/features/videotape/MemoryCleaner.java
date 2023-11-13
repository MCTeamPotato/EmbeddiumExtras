package com.teampotato.embeddiumextras.features.videotape;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.FramebufferConstants;
import net.minecraftforge.event.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class MemoryCleaner {
    public static final Queue<FramebufferIdsContainer> IDS_CONTAINERS = new ConcurrentLinkedQueue<>();

    public static boolean shouldFixGPUMemoryLeak;

    public static final Logger LOGGER = LogManager.getLogger();

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.END) && shouldFixGPUMemoryLeak) {
            boolean done = false;
            int counter = 0;
            while (!IDS_CONTAINERS.isEmpty() && counter++ < 20) {
                if (!done) {
                    GlStateManager._bindTexture(0);
                    GlStateManager._glBindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, 0);
                    done = true;
                }
                final FramebufferIdsContainer ids = IDS_CONTAINERS.poll();
                if (ids != null) {
                    final int depthBufferId = ids.getDepthBufferId();
                    final int colorTextureId = ids.getColorTextureId();
                    final int frameBufferId = ids.getFrameBufferId();
                    if (depthBufferId > -1) TextureUtil.releaseTextureId(depthBufferId);
                    if (colorTextureId > -1) TextureUtil.releaseTextureId(colorTextureId);
                    if (frameBufferId > -1) {
                        GlStateManager._glBindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, 0);
                        GlStateManager._glDeleteFramebuffers(frameBufferId);
                    }
                }
            }
        }
    }

    public static void onFramebufferFinalize(final int depthBufferId, final int colorTextureId, final int frameBufferId) {
        if (shouldFixGPUMemoryLeak && (depthBufferId > -1 || colorTextureId > -1 || frameBufferId > -1)) {
            IDS_CONTAINERS.add(new FramebufferIdsContainer(depthBufferId, colorTextureId, frameBufferId));
        }
    }
}
