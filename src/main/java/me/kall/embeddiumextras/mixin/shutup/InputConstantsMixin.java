package me.kall.embeddiumextras.mixin.shutup;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.platform.InputConstants;
import me.kall.embeddiumextras.config.ExtrasConfig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InputConstants.class)
public abstract class InputConstantsMixin {
    @WrapMethod(method = "isKeyDown")
    private static boolean onCheckKeyDown(long window, int key, Operation<Boolean> original) {
        if (ExtrasConfig.SHUT_UP_GL_ERROR.get() && (key == -1 || key == 0)) return false;
        return original.call(window, key);
    }
}
