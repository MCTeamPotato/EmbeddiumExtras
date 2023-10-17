package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntity.class)
public class TileEntityMixin implements RenderChecker {
    @Shadow @Final private TileEntityType<?> type;
    @Unique
    private boolean ee$shouldAlwaysRender;

    @Override
    public boolean ee$shouldAlwaysRender() {
        return this.ee$shouldAlwaysRender;
    }

    @Override
    public void ee$setShouldAlwaysRender(boolean shouldAlwaysRender) {
        this.ee$shouldAlwaysRender = shouldAlwaysRender;
    }

    @Inject(method = "setLevelAndPosition", at = @At("RETURN"))
    private void onInit(World world, BlockPos blockPos, CallbackInfo ci) {
        if (this.ee$shouldAlwaysRender() || world == null || !world.isClientSide) return;
        ResourceLocation id = this.type.getRegistryName();
        if (id != null && (EmbeddiumExtrasConfig.tileEntityList.get().contains(id.toString()) || EmbeddiumExtrasConfig.tileEntityModIdList.get().contains(id.getNamespace()))) {
            this.ee$setShouldAlwaysRender(true);
        }
    }
}
