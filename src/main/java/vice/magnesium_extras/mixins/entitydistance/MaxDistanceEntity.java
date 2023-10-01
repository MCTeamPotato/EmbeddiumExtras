package vice.magnesium_extras.mixins.entitydistance;

import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vice.magnesium_extras.config.EntityListConfig;
import vice.magnesium_extras.config.MagnesiumExtrasConfig;
import vice.magnesium_extras.util.DistanceUtility;

import java.util.Objects;

@Mixin(EntityRendererManager.class)
public abstract class MaxDistanceEntity {
    @Inject(at = @At("RETURN"), method = "shouldRender", cancellable = true)
    public <E extends Entity> void shouldDoRender(E entity, ClippingHelper clippingHelper, double cameraX, double cameraY, double cameraZ, @NotNull CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        if (
                !MagnesiumExtrasConfig.enableDistanceChecks.get() ||
                EntityListConfig.ENTITY_LIST.get().contains(Objects.requireNonNull(entity.getType().getRegistryName()).toString()) ||
                EntityListConfig.MODID_LIST.get().contains(Objects.requireNonNull(entity.getType().getRegistryName()).getNamespace()) ||
                DistanceUtility.isEntityWithinDistance(
                        entity,
                        cameraX,
                        cameraY,
                        cameraZ,
                        MagnesiumExtrasConfig.maxEntityRenderDistanceY.get(),
                        MagnesiumExtrasConfig.maxEntityRenderDistanceSquare.get()
                )
        ) {
            return;
        }
        cir.setReturnValue(false);
    }
}