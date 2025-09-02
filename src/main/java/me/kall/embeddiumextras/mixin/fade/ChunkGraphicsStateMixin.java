package me.kall.embeddiumextras.mixin.fade;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import me.kall.embeddiumextras.features.fade.ChunkGraphicsStateExt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ChunkGraphicsState.class, remap = false)
public abstract class ChunkGraphicsStateMixin implements ChunkGraphicsStateExt {
    @Unique private ChunkRenderContainer<?> extras$container;
    @Unique private float extras$loadTime;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ChunkRenderContainer<?> container, CallbackInfo ci) {
        this.extras$container = container;
    }

    @Override
    public ChunkRenderContainer<?> extras$getContainer() {
        return this.extras$container;
    }

    @Override
    public float extras$getLoadTime() {
        return this.extras$loadTime;
    }

    @Override
    public void extras$setLoadTime(float loadTime) {
        this.extras$loadTime = loadTime;
    }
}
