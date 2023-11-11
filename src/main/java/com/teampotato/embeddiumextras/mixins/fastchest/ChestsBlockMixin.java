package com.teampotato.embeddiumextras.mixins.fastchest;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.EnderChestBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/* Source:
   https://github.com/FakeDomi/FastChest/blob/1.16/src/main/java/re/domi/fastchest/mixin/ChestBlockMixin.java
   https://github.com/FakeDomi/FastChest/blob/1.16/src/main/java/re/domi/fastchest/mixin/EnderChestBlockMixin.java
*/
@Mixin({ChestBlock.class, EnderChestBlock.class})
public abstract class ChestsBlockMixin {
    @Inject(method = "getRenderShape", at = @At("HEAD"), cancellable = true)
    private void ee$getRenderShape(BlockState state, CallbackInfoReturnable<BlockRenderType> cir) {
        if (EmbeddiumExtrasConfig.enableFastChest.get()) cir.setReturnValue(BlockRenderType.MODEL);
    }
}
