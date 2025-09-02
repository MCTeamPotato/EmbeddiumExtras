package me.kall.embeddiumextras.mixin.zoom;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.zoom.ZoomManager;
import net.minecraft.client.Options;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @WrapOperation(method = "getFov", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;fov:D", ordinal = 0, opcode = Opcodes.GETFIELD))
    private double getFov(Options instance, Operation<Double> original) {
        return ExtrasConfig.ZOOM.get() ? ZoomManager.changeFov(original.call(instance)) : original.call(instance);
    }
}
