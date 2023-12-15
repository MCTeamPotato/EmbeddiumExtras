package com.teampotato.embeddiumextras.mixins.fast_chest;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.fastchest.FastChestContainer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Source: https://github.com/FakeDomi/FastChest/blob/1.16/src/main/java/re/domi/fastchest/mixin/BlockEntityRenderDispatcherMixin.java
@Mixin(TileEntityRendererDispatcher.class)
public abstract class TileEntityRendererDispatcherMixin {
    @Inject(method = "getRenderer", at = @At("HEAD"), cancellable = true)
    private <E extends TileEntity> void ee$getRenderer(E tile, CallbackInfoReturnable<TileEntityRenderer<E>> cir) {
        if (EmbeddiumExtrasConfig.enableFastChest.get() && ((FastChestContainer)tile.getType()).ee$canBeImpactedByFastChest()) {
            cir.setReturnValue(null);
        }
    }
}
