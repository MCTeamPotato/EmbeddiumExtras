package com.teampotato.embeddiumextras.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import org.jetbrains.annotations.NotNull;

public class DistanceUtility {

    public static boolean isEntityWithinDistance(@NotNull BlockPos player, @NotNull Vector3d entity, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - entity.y - 4) < maxHeight) {
            double x = player.getX() - entity.x;
            double z = player.getZ() - entity.z;
            return x * x + z * z < maxDistanceSquare;
        }
        return false;
    }

    public static boolean isEntityWithinDistance(@NotNull Entity player, double cameraX, double cameraY, double cameraZ, int maxHeight, int maxDistanceSquare) {
        if (Math.abs(player.getY() - cameraY - 4) < maxHeight) {
            double x = player.getX() - cameraX;
            double z = player.getZ() - cameraZ;

            return x * x + z * z < maxDistanceSquare;
        }
        return false;
    }
}
