package com.teampotato.embeddiumextras.features.gpumemleakfix;

public final class FramebufferIdsContainer {
    public final int depthBufferId;
    public final int colorTextureId;
    public final int frameBufferId;

    public FramebufferIdsContainer(int depthBufferId, int colorTextureId, int frameBufferId) {
        this.depthBufferId = depthBufferId;
        this.colorTextureId = colorTextureId;
        this.frameBufferId = frameBufferId;
    }
}