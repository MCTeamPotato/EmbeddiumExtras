package com.teampotato.embeddiumextras.mixins.fast_chest;

import com.teampotato.embeddiumextras.features.fastchest.FastChestContainer;
import net.minecraft.tileentity.TileEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(TileEntityType.class)
public class TileEntityTypeMixin implements FastChestContainer {
    @Unique
    private boolean ee$canBeImpactedByFastChest;
    @Override
    public boolean ee$canBeImpactedByFastChest() {
        return this.ee$canBeImpactedByFastChest;
    }

    @Override
    public void ee$setCanBeImpactedByFastChest(boolean canBeImpactedByFastChest) {
        this.ee$canBeImpactedByFastChest = canBeImpactedByFastChest;
    }
}
