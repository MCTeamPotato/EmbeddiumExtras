package com.teampotato.embeddiumextras.mixins.particle;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.ActiveRenderInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Redirect(method = "renderParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/Particle;render(Lcom/mojang/blaze3d/vertex/IVertexBuilder;Lnet/minecraft/client/renderer/ActiveRenderInfo;F)V"))
    private void cullPartcilesBasedOnDist(Particle particle, IVertexBuilder iVertexBuilder, ActiveRenderInfo activeRenderInfo, float v) {
        if (EmbeddiumExtrasConfig.enableParticleDistanceCheck.get()) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null && ee$getDistToPlayer(((ParticleAccessor) particle).getX(), ((ParticleAccessor) particle).getY(), ((ParticleAccessor) particle).getZ(), player.getX(), player.getY(), player.getZ()) >= EmbeddiumExtrasConfig.maxParticleRenderDistance.get().doubleValue()) return;
        }
        particle.render(iVertexBuilder, activeRenderInfo, v);
    }

    @Unique
    private static double ee$getDistToPlayer(double particleX, double particleY, double particleZ, double playerX, double playerY, double playerZ) {
        double x = particleX - playerX;
        double y = particleY - playerY;
        double z = particleZ - playerZ;
        return StrictMath.sqrt(x * x + y * y + z * z);
    }
}
