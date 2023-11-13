package com.teampotato.embeddiumextras.features.gpumemleakfix;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.FramebufferConstants;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.concurrent.ConcurrentLinkedQueue;

public final class MemoryCleaner {
    public static final ConcurrentLinkedQueue<FramebufferIdsContainer> IDS_CONTAINERS = new ConcurrentLinkedQueue<>();

    public static boolean shouldFixGPUMemoryLeak;

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
                FramebufferIdsContainer ids = IDS_CONTAINERS.poll();
                if (ids != null) {
                    if (ids.depthBufferId > -1) TextureUtil.releaseTextureId(ids.depthBufferId);
                    if (ids.colorTextureId > -1) TextureUtil.releaseTextureId(ids.colorTextureId);
                    if (ids.frameBufferId > -1) {
                        GlStateManager._glBindFramebuffer(FramebufferConstants.GL_FRAMEBUFFER, 0);
                        GlStateManager._glDeleteFramebuffers(ids.frameBufferId);
                    }
                }
            }
        }
    }

    public static void onFramebufferFinalize(int depthBufferId, int colorTextureId, int frameBufferId) {
        if (shouldFixGPUMemoryLeak && (depthBufferId > -1 || colorTextureId > -1 || frameBufferId > -1)) {
            IDS_CONTAINERS.add(new FramebufferIdsContainer(depthBufferId, colorTextureId, frameBufferId));
        }
    }
}
