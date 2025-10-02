package me.kall.embeddiumextras.features.fade;

import me.kall.embeddiumextras.features.fade.shader.IShaderChecker;
import me.kall.embeddiumextras.features.fade.shader.IrisShaderChecker;
import net.minecraftforge.fml.loading.FMLLoader;

public class ShaderChecker {
    public static final IShaderChecker SHADER_CHECKER = FMLLoader.getLoadingModList().getModFileById("oculus") != null ? new IrisShaderChecker() : () -> false;

    public static boolean shaderAbsent() {
        return !SHADER_CHECKER.isShaderPresent();
    }
}
