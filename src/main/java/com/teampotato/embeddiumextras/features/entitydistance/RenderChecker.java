package com.teampotato.embeddiumextras.features.entitydistance;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public interface RenderChecker {
    static boolean isEntityWithinDistance(@NotNull BlockPos player, @NotNull Vector3d entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.y - 4) < maxHeight) {
            double x = player.getX() - entity.x;
            double z = player.getZ() - entity.z;
            return x * x + z * z < maxDistanceSquare;
        }
        return false;
    }

    static boolean isEntityWithinDistance(@NotNull Entity player, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - cameraY - 4) < maxHeight) {
            double x = player.getX() - cameraX;
            double z = player.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }
        return false;
    }

    boolean ee$shouldAlwaysRender();
    void ee$setShouldAlwaysRender(boolean shouldAlwaysRender);
}
