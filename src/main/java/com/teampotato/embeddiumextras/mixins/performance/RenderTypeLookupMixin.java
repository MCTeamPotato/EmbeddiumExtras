package com.teampotato.embeddiumextras.mixins.performance;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraftforge.registries.IRegistryDelegate;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Predicate;

@Mixin(value = RenderTypeLookup.class)
public abstract class RenderTypeLookupMixin
{
    @Shadow
    private static boolean renderCutout;

    @Shadow(remap = false)
    @Final
    private static Map<IRegistryDelegate<Block>, Predicate<RenderType>> blockRenderChecks;

    @Shadow(remap = false)
    @Final
    private static Map<IRegistryDelegate<Fluid>, Predicate<RenderType>> fluidRenderChecks;

    @Inject(remap = false, at = @At("HEAD"), method = "canRenderInLayer(Lnet/minecraft/block/BlockState;Lnet/minecraft/client/renderer/RenderType;)Z", cancellable = true)
    private static void render(@NotNull BlockState state, RenderType type, CallbackInfoReturnable<Boolean> cir) {
        Block block = state.getBlock();
        if (block instanceof LeavesBlock) {
            cir.setReturnValue(renderCutout ? type == RenderType.cutoutMipped() : type == RenderType.solid());
        } else {
            Predicate<RenderType> renderType = blockRenderChecks.get(block.delegate);
            cir.setReturnValue(renderType != null ? renderType.test(type) : type == RenderType.solid());
        }
    }

    @Inject(remap = false, at = @At("HEAD"), method = "canRenderInLayer(Lnet/minecraft/fluid/FluidState;Lnet/minecraft/client/renderer/RenderType;)Z", cancellable = true)
    private static void render(@NotNull FluidState fluid, RenderType type, @NotNull CallbackInfoReturnable<Boolean> cir)
    {
        Predicate<RenderType> renderType = fluidRenderChecks.get(fluid.getType().delegate);
        cir.setReturnValue(renderType != null ? renderType.test(type) : type == RenderType.solid());
    }
}
