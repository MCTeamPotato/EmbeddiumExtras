package com.teampotato.embeddiumextras.mixins.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.teampotato.embeddiumextras.config.EmbeddiumExtrasConfig;
import mezz.jei.api.gui.handlers.IGuiProperties;
import mezz.jei.gui.elements.GuiIconToggleButton;
import mezz.jei.gui.overlay.IngredientListOverlay;
import mezz.jei.input.GuiTextFieldFilter;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(value = IngredientListOverlay.class, remap = false)
public abstract class IngredientListOverlayMixin {
    @Shadow @Final private GuiTextFieldFilter searchField;
    @Shadow @Nullable private IGuiProperties guiProperties;
    @Shadow @Final private GuiIconToggleButton configButton;

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;draw(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/matrix/MatrixStack;IIF)V"), cancellable = true)
    private void ee$onDrawScreen(Minecraft minecraft, MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (EmbeddiumExtrasConfig.hideJeiItems.get()) {
            if (this.searchField.getValue().isEmpty()) {
                if (this.guiProperties != null) this.configButton.draw(matrixStack, mouseX, mouseY, partialTicks);
                ci.cancel();
            }
        }
    }

    @Inject(method = "drawTooltips", at = @At("HEAD"), cancellable = true)
    private void ee$onDrawTooltips(Minecraft minecraft, MatrixStack matrixStack, int mouseX, int mouseY, CallbackInfo ci) {
        if (EmbeddiumExtrasConfig.hideJeiItems.get() && this.searchField.getValue().isEmpty()) ci.cancel();
    }
}
