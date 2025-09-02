package me.kall.embeddiumextras.mixin.fade;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkRenderShaderBackend;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChunkRenderShaderBackend.class, remap = false)
public class ChunkRenderShaderBackendMixin<P extends ChunkGraphicsState> {
    @Unique protected float extras$currentTime;

    @Inject(method = "begin", at = @At("HEAD"))
    private void updateTime(PoseStack matrixStack, CallbackInfo ci) {
        this.extras$currentTime = ((float) Util.getMillis()) / 1000.0F;
    }
}
