package me.kall.embeddiumextras.mixin.fastchest;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.kall.embeddiumextras.config.ExtrasConfig;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.entity.TrappedChestBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;

@Mixin(BlockEntityRenderDispatcher.class)
public abstract class BlockEntityRenderDispatcherMixin {
    @Unique private static final Set<Class<?>> ACCEPTABLE_CHESTS = new ObjectOpenHashSet<>();

    static {
        ACCEPTABLE_CHESTS.add(ChestBlockEntity.class);
        ACCEPTABLE_CHESTS.add(TrappedChestBlockEntity.class);
        ACCEPTABLE_CHESTS.add(EnderChestBlockEntity.class);
    }

    @WrapMethod(method = "getRenderer")
    private <E extends BlockEntity> BlockEntityRenderer<E> getRenderer(E blockEntity, Operation<BlockEntityRenderer<E>> original) {
        if (ExtrasConfig.FAST_CHEST.get()) {
            Class<?> chestClass = blockEntity.getClass();
            if (ACCEPTABLE_CHESTS.contains(chestClass)) return null;
        }

        return original.call(blockEntity);
    }
}
