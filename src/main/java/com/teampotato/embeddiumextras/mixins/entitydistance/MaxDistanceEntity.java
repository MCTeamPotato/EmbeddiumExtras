package com.teampotato.embeddiumextras.mixins.entitydistance;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import com.teampotato.embeddiumextras.config.EntityListConfig;
import com.teampotato.embeddiumextras.config.MagnesiumExtrasConfig;
import com.teampotato.embeddiumextras.util.DistanceUtility;

@Mixin(EntityRendererManager.class)
public abstract class MaxDistanceEntity {
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, @NotNull CallbackInfoReturnable<Boolean> cir) {
        ResourceLocation id = entity.getType().getRegistryName();
        if (id == null || !MagnesiumExtrasConfig.enableDistanceChecks.get() || EntityListConfig.ENTITY_LIST.get().contains(id.toString()) || EntityListConfig.ENTITY_MODID_LIST.get().contains(id.getNamespace()) || DistanceUtility.isEntityWithinDistance(entity, cameraX, cameraY, cameraZ, MagnesiumExtrasConfig.maxEntityRenderDistanceY.get(), MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get())) return;

        cir.setReturnValue(false);
    }
}