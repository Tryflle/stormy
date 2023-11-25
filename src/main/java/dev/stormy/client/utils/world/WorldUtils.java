package dev.stormy.client.utils.world;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldUtils {
    public static BlockPos findNearestBedPos(World world, BlockPos playerPos, int searchRadius) {
        BlockPos nearestBedPos = null;
        double nearestDistanceSq = Double.MAX_VALUE;

        int minX = playerPos.getX() - searchRadius;
        int minY = Math.max(0, playerPos.getY() - searchRadius);
        int minZ = playerPos.getZ() - searchRadius;
        int maxX = playerPos.getX() + searchRadius;
        int maxY = Math.min(world.getHeight(), playerPos.getY() + searchRadius);
        int maxZ = playerPos.getZ() + searchRadius;

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    BlockPos currentPos = new BlockPos(x, y, z);
                    IBlockState blockState = world.getBlockState(currentPos);
                    Block block = blockState.getBlock();
                    if (block == Blocks.bed) {
                        double distanceSq = playerPos.distanceSq(currentPos);
                        if (distanceSq < nearestDistanceSq) {
                            nearestBedPos = currentPos;
                            nearestDistanceSq = distanceSq;
                        }
                    }
                }
            }
        }

        return nearestBedPos;
    }
}