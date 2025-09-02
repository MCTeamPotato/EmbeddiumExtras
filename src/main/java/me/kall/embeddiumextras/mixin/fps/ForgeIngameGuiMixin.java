package me.kall.embeddiumextras.mixin.fps;

import com.mojang.blaze3d.vertex.PoseStack;
import me.kall.embeddiumextras.features.fps.FpsBarInfoProvider;
import net.minecraftforge.client.gui.ForgeIngameGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ForgeIngameGui.class)
public abstract class ForgeIngameGuiMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void renderFpsBar(PoseStack stack, float partialTicks, CallbackInfo ci) {
        FpsBarInfoProvider.render(stack);
    }
}
