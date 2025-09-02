package me.kall.embeddiumextras.mixin.zoom;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import me.kall.embeddiumextras.config.ExtrasConfig;
import me.kall.embeddiumextras.features.zoom.ZoomManager;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Inventory.class)
public abstract class InventoryMixin {
    @WrapMethod(method = "swapPaint")
    private void onScrollInHotbar(double direction, Operation<Void> original) {
        if (ExtrasConfig.ZOOM.get() && ZoomManager.ZOOM.isDown()) return;
        original.call(direction);
    }
}
