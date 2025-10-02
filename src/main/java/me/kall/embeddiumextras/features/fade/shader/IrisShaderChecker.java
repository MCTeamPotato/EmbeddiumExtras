package me.kall.embeddiumextras.features.fade.shader;

import net.irisshaders.iris.api.v0.IrisApi;

public class IrisShaderChecker implements IShaderChecker {
    @Override
    public boolean isShaderPresent() {
        return IrisApi.getInstance().isShaderPackInUse();
    }
}
