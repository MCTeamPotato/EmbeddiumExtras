package me.kall.embeddiumextras.features.dist;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public interface Renderable {
    static boolean isEntityInDist(@NotNull BlockPos entity, @NotNull Vec3 camera, int maxHeight, int maxDistSqr) {
        if (Math.abs(entity.getY() - camera.y - 4) < maxHeight) {
            double x = entity.getX() - camera.x;
            double z = entity.getZ() - camera.z;
            return x * x + z * z < maxDistSqr;
        }
        return false;
    }

    static boolean isEntityInDist(@NotNull Vec3 entity, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistSqr) {
        if (Math.abs(entity.y() - cameraY - 4) < maxHeight) {
            double x = entity.x() - cameraX;
            double z = entity.z() - cameraZ;
            return x * x + z * z < maxDistSqr;
        }
        return false;
    }

    static Vec3 renderInfo() {
        return Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
    }

    boolean dist$alwaysRenderable();
    void dist$setAlwaysRenderable(boolean alwaysRenderable);

    interface Entity {
        boolean dist$renderable();
    }
}
