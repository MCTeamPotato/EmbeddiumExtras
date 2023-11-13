package com.teampotato.embeddiumextras.features.videotape;

public final class FramebufferIdsContainer {
    private final int depthBufferId;
    private final int colorTextureId;
    private final int frameBufferId;

    public FramebufferIdsContainer(int depthBufferId, int colorTextureId, int frameBufferId) {
        this.depthBufferId = depthBufferId;
        this.colorTextureId = colorTextureId;
        this.frameBufferId = frameBufferId;
    }

    public int getDepthBufferId() {
        return this.depthBufferId;
    }

    public int getColorTextureId() {
        return this.colorTextureId;
    }

    public int getFrameBufferId() {
        return this.frameBufferId;
    }
}