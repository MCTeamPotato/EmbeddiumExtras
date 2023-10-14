package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.config.EntityListConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.loading.FMLLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntity.class)
public class TileEntityMixin implements RenderChecker {
    @Unique
    private boolean ee$shouldAlwaysRender;

    @Override
    public boolean ee$shouldAlwaysRender() {
        return this.ee$shouldAlwaysRender;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(TileEntityType<?> entityType, CallbackInfo ci) {
        if (!FMLLoader.getDist().isClient()) return;
        ResourceLocation id = entityType.getRegistryName();
        if (id != null && (EntityListConfig.TILE_ENTITY_LIST.get().contains(id.toString()) || EntityListConfig.TILE_ENTITY_MODID_LIST.get().contains(id.getNamespace()))) this.ee$shouldAlwaysRender = true;
    }
}
