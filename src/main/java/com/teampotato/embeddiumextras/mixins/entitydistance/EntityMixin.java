package com.teampotato.embeddiumextras.mixins.entitydistance;

import com.teampotato.embeddiumextras.config.EntityListConfig;
import com.teampotato.embeddiumextras.features.entitydistance.RenderChecker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements RenderChecker {
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

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(EntityType<?> entityType, World world, CallbackInfo ci) {
        if (this.ee$shouldAlwaysRender() || world == null || !world.isClientSide) return;
        ResourceLocation id = entityType.getRegistryName();
        if (id != null && (EntityListConfig.ENTITY_LIST.get().contains(id.toString()) || EntityListConfig.ENTITY_MODID_LIST.get().contains(id.getNamespace()))) {
            this.ee$setShouldAlwaysRender(true);
        }
    }
}
