package me.kall.embeddiumextras.mixin.jei;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.config.ExtrasConfig;
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

@Mixin(IngredientListOverlay.class)
public class IngredientListOverlayMixin {
    @Shadow(remap = false) @Final private GuiTextFieldFilter searchField;
    @Shadow(remap = false) @Nullable private IGuiProperties guiProperties;
    @Shadow(remap = false) @Final private GuiIconToggleButton configButton;

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lmezz/jei/gui/overlay/IngredientGridWithNavigation;draw(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V"), cancellable = true)
    private void onDraw(Minecraft minecraft, PoseStack matrixStack, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (ExtrasConfig.HIDE_JEI_ITEMS.get()) {
            if (this.searchField.getValue().isEmpty()) {
                if (this.guiProperties != null) this.configButton.draw(matrixStack, mouseX, mouseY, partialTicks);
                ci.cancel();
            }
        }
    }

    @WrapMethod(method = "drawTooltips")
    private void onDrawTooltips(Minecraft minecraft, PoseStack matrixStack, int mouseX, int mouseY, Operation<Void> original) {
        if (ExtrasConfig.HIDE_JEI_ITEMS.get() && this.searchField.getValue().isEmpty()) return;
        original.call(minecraft, matrixStack, mouseX, mouseY);
    }
}
