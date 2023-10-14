package com.teampotato.embeddiumextras.mixins.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.teampotato.embeddiumextras.features.fadeinchunks.ChunkGraphicsStateExt;

@Mixin(value = {ChunkGraphicsState.class}, remap = false)
public abstract class ChunkGraphicsStateMixin implements ChunkGraphicsStateExt {
    @Unique
    private ChunkRenderContainer<?> ee$container;

    @Unique
    private float ee$loadTime;

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void init(ChunkRenderContainer<?> container, CallbackInfo ci) {
        this.ee$container = container;
    }

    public ChunkRenderContainer<?> getee$container() {
        return this.ee$container;
    }

    public float getee$loadTime() {
        return this.ee$loadTime;
    }

    public void setee$loadTime(float time) {
        this.ee$loadTime = time;
    }
}

