/*******************************************************************************
 * Copyright 2019 grondag
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * <p>
 *   http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.teampotato.embeddiumextras.mixins.clear_skies;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FogRenderer.class)
public abstract class FogRendererMixin {
    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/vector/Vector3d;x()D", shift = At.Shift.BEFORE), ordinal = 2)
    private static Vector3d onSampleColor(Vector3d vector3d) {
        if (!EmbeddiumExtrasConfig.enableClearSkies.get()) return vector3d;
        final Minecraft minecraft = Minecraft.getInstance();
        final ClientWorld world = minecraft.level;
        assert world != null;
        if (world.dimensionType().hasSkyLight()) {
            return world.getSkyColor(minecraft.gameRenderer.getMainCamera().getBlockPosition(), minecraft.getDeltaFrameTime());
        } else {
            return vector3d;
        }
    }

    @ModifyVariable(method = "setupColor", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/vector/Vector3f;dot(Lnet/minecraft/util/math/vector/Vector3f;)F"), ordinal = 7)
    private static float afterPlaneDot(float f) {
        if (!EmbeddiumExtrasConfig.enableClearSkies.get()) return f;
        return 0;
    }

    @ModifyConstant(method = "setupColor", constant = @Constant(floatValue = 0.5F, ordinal = 1))
    private static float onGetFogRedAndGreenInRain(float constant) {
        if (!EmbeddiumExtrasConfig.enableClearSkies.get()) return constant;
        return 0.0F;
    }

    @ModifyConstant(method = "setupColor", constant = @Constant(floatValue = 0.4F))
    private static float onGetFogBlueInRain(float constant) {
        if (!EmbeddiumExtrasConfig.enableClearSkies.get()) return constant;
        return 0.0F;
    }

    @ModifyConstant(method = "setupColor", constant = @Constant(floatValue = 0.5F, ordinal = 2))
    private static float onGetFogColorInThunder(float constant) {
        if (!EmbeddiumExtrasConfig.enableClearSkies.get()) return constant;
        return 0.0F;
    }
}
