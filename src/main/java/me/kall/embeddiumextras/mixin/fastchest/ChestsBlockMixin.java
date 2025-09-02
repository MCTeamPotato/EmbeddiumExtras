package me.kall.embeddiumextras.mixin.fastchest;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ChestBlock.class, EnderChestBlock.class})
public abstract class ChestsBlockMixin {
    @WrapMethod(method = "getRenderShape")
    private RenderShape onRenderShape(BlockState state, Operation<RenderShape> original) {
        return ExtrasConfig.FAST_CHEST.get() ? RenderShape.MODEL : original.call(state);
    }
}
