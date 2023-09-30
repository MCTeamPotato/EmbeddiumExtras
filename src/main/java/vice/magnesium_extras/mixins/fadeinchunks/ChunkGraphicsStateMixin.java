package vice.magnesium_extras.mixins.fadeinchunks;

import me.jellysquid.mods.sodium.client.render.chunk.ChunkGraphicsState;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vice.magnesium_extras.features.fadeinchunks.ChunkGraphicsStateExt;

@Mixin(value = {ChunkGraphicsState.class}, remap = false)
public abstract class ChunkGraphicsStateMixin implements ChunkGraphicsStateExt {
    @Unique
    private ChunkRenderContainer<?> rbpe$container;

    @Unique
    private float rbpe$loadTime;

    @Inject(method = {"<init>"}, at = {@At("RETURN")})
    private void init(ChunkRenderContainer<?> container, CallbackInfo ci) {
        this.rbpe$container = container;
    }

    public ChunkRenderContainer<?> getRbpe$container() {
        return this.rbpe$container;
    }

    public float getRbpe$loadTime() {
        return this.rbpe$loadTime;
    }

    public void setRbpe$loadTime(float time) {
        this.rbpe$loadTime = time;
    }
}

