package me.kall.embeddiumextras.mixin.clearskies;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/CubicSampler;gaussianSampleVec3(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/util/CubicSampler$Vec3Fetcher;)Lnet/minecraft/world/phys/Vec3;"), ordinal = 2, require = 1, allow = 1)
    private static Vec3 onSampleColor(Vec3 vec3) {
        if (ExtrasConfig.CLEAR_SKIES.get()) {
            Minecraft client = Minecraft.getInstance();
            ClientLevel level = client.level;
            if (level != null && level.dimensionType().hasSkyLight()) {
                return level.getSkyColor(client.gameRenderer.getMainCamera().getBlockPosition(), client.getDeltaFrameTime());
            }
        }

        return vec3;
    }

    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE_ASSIGN", target = "Lcom/mojang/math/Vector3f;dot(Lcom/mojang/math/Vector3f;)F"), ordinal = 7, require = 1, allow = 1)
    private static float afterPlaneDot(float constant) {
        return ExtrasConfig.CLEAR_SKIES.get() ? 0.0F : constant;
    }

    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getRainLevel(F)F"))
    private static float onGetRainLevel(ClientLevel instance, float v, Operation<Float> original) {
        return ExtrasConfig.CLEAR_SKIES.get() ? 0.0F : original.call(instance, v);
    }

    @WrapOperation(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;getThunderLevel(F)F"))
    private static float onGetThunderLevel(ClientLevel instance, float v, Operation<Float> original) {
        return ExtrasConfig.CLEAR_SKIES.get() ? 0.0F : original.call(instance, v);
    }
}
