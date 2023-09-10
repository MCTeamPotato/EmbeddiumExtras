package vice.magnesium_extras.mixins.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.magnesium_extras.features.fadeinchunks.ChunkGraphicsStateExt;

@Mixin(value = {ChunkGraphicsState.class}, remap = false)
public abstract class ChunkGraphicsStateMixin implements ChunkGraphicsStateExt {
    private ChunkRenderContainer<?> container;

    private float loadTime;

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void init(ChunkRenderContainer<?> container, CallbackInfo ci) {
        this.container = container;
    }

    public ChunkRenderContainer<?> getContainer() {
        return this.container;
    }

    public float getLoadTime() {
        return this.loadTime;
    }

    public void setLoadTime(float time) {
        this.loadTime = time;
    }
}

