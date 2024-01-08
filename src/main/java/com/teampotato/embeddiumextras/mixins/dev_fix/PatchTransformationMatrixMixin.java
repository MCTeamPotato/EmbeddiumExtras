package com.teampotato.embeddiumextras.mixins.dev_fix;

import net.minecraft.util.math.vector.TransformationMatrix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(TransformationMatrix.class)
public abstract class PatchTransformationMatrixMixin {

    @Shadow protected abstract TransformationMatrix composeVanilla(TransformationMatrix p_composeVanilla_1_);

    @Shadow @Nullable protected abstract TransformationMatrix inverseVanilla();

    public TransformationMatrix func_227985_a_(TransformationMatrix e) {
        return composeVanilla(e);
    }

    public TransformationMatrix func_227987_b_() {
        return inverseVanilla();
    }
}
