package com.teampotato.embeddiumextras.mixins.fadeinchunks;

import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import me.jellysquid.mods.sodium.client.render.chunk.backends.multidraw.MultidrawChunkRenderBackend;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = {ShaderLoader.class}, remap = false)
public abstract class ShaderLoaderMixin {
    @Shadow
    private static String getShaderPath(ResourceLocation name) {
        return null;
    }

    @Inject(method = {"getShaderSource"}, at = {@At("RETURN")}, cancellable = true)
    private static void modifyShaderForFadeInEffect(String path, CallbackInfoReturnable<String> cir) {
        if (!(SodiumClientMod.options()).advanced.useChunkMultidraw || !MultidrawChunkRenderBackend.isSupported(false))
            return;
        boolean isVertexShader = path.equals(getShaderPath(new ResourceLocation("sodium", "chunk_gl20.v.glsl")));
        boolean isFragmentShader = path.equals(getShaderPath(new ResourceLocation("sodium", "chunk_gl20.f.glsl")));
        if (!isVertexShader && !isFragmentShader)
            return;
        StringBuilder source = new StringBuilder(cir.getReturnValue());
        rbpe$prepend(source, "varying", "varying float v_FadeInProgress;");
        if (isVertexShader)
            rbpe$prepend(source, "v_Color = ", "v_FadeInProgress = d_ModelOffset.w;");
        if (isFragmentShader)
            rbpe$replace(source, "(getFogFactor(),", "(min(v_FadeInProgress, getFogFactor()),");
        cir.setReturnValue(source.toString());
    }

    @Unique
    private static void rbpe$replace(StringBuilder buffer, String search, String str) {
        int idx = buffer.indexOf(search);
        buffer.replace(idx, idx + search.length(), str);
    }

    @Unique
    private static void rbpe$prepend(StringBuilder buffer, String search, String str) {
        rbpe$replace(buffer, search, str + search);
    }
}

