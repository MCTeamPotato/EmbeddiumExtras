package com.teampotato.embeddiumextras.mixins.fade_in_chunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.shader.ChunkRenderShaderBackend;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ChunkRenderShaderBackend.class}, remap = false)
public abstract class ChunkRenderShaderBackendMixin<P extends ChunkGraphicsState> {
    @Unique
    protected float ee$currentTime;

    @Inject(method = "begin", at = {@At("HEAD")})
    private void updateTime(CallbackInfo ci) {
        this.ee$currentTime = (float)Util.getMillis() / 1000.0F;
    }
}