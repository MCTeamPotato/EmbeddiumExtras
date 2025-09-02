package me.kall.embeddiumextras.features.tape;

import com.mojang.blaze3d.platform.GlConst;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.TextureUtil;
import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MemoryCleaner {
    public static final Queue<FramebufferIdsContainer> IDS_CONTAINERS = new ConcurrentLinkedQueue<>();

    public static void onClientTick(TickEvent.@NotNull ClientTickEvent event) {
        if (!ExtrasConfig.MEMORY_LEAK_FIX.get()) return;
        if (!event.phase.equals(TickEvent.Phase.END)) return;
        Minecraft.getInstance().getProfiler().push("fixPotentialMemoryLeak");
        boolean done = false;
        int counter = 0;
        while (!IDS_CONTAINERS.isEmpty() && counter++ < 20) {
            if (!done) {
                GlStateManager._bindTexture(0);
                GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
                done = true;
            }

            FramebufferIdsContainer ids = IDS_CONTAINERS.poll();
            if (ids != null) {
                int depthBufferId = ids.getDepthBufferId();
                int colorTextureId = ids.getColorTextureId();
                int frameBufferId = ids.getFrameBufferId();
                if (depthBufferId > -1) TextureUtil.releaseTextureId(depthBufferId);
                if (colorTextureId > -1) TextureUtil.releaseTextureId(colorTextureId);
                if (frameBufferId > -1) {
                    GlStateManager._glBindFramebuffer(GlConst.GL_FRAMEBUFFER, 0);
                    GlStateManager._glDeleteFramebuffers(frameBufferId);
                }
            }
        }
        Minecraft.getInstance().getProfiler().pop();
    }

    public static void onFinalize(final int depthBufferId, final int colorTextureId, final int frameBufferId) {
        if (ExtrasConfig.MEMORY_LEAK_FIX.get() && (depthBufferId > -1 || colorTextureId > -1 || frameBufferId > -1)) {
            IDS_CONTAINERS.add(new FramebufferIdsContainer(depthBufferId, colorTextureId, frameBufferId));
        }
    }
}
