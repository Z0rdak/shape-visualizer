package de.zordak.shapeviz.shape.strategy;

import de.zordak.shapeviz.shape.CuboidShape;
import de.zordak.shapeviz.shape.Shape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import static de.zordak.shapeviz.shape.AreaUtil.isInFacePlane;

public class CuboidHullStrategy implements DisplayStrategy {
    @Override
    public Set<BlockPos> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape cuboid)) return Set.of();


        return getHull(cuboid);
    }



    public static Set<BlockPos> getHull(CuboidShape cuboid) {
        var area = cuboid.area();
        var p1 = new BlockPos(area.minX(), area.minY(), area.minZ());
        var p2 = new BlockPos(area.maxX(), area.minY(), area.minZ());
        var p3 = new BlockPos(area.minX(), area.minY(), area.maxZ());
        var p4 = new BlockPos(area.maxX(), area.minY(), area.maxZ());
        var p5 = new BlockPos(area.minX(), area.maxY(), area.minZ());
        var p6 = new BlockPos(area.maxX(), area.maxY(), area.minZ());
        var p7 = new BlockPos(area.minX(), area.maxY(), area.maxZ());
        var p8 = new BlockPos(area.maxX(), area.maxY(), area.maxZ());
        var face1 = getBlocksInFace(p1, p2, p3, p4); // Bottom
        var face2 = getBlocksInFace(p5, p6, p7, p8); // Top
        var face3 = getBlocksInFace(p1, p3, p5, p7); // West
        var face4 = getBlocksInFace(p2, p4, p6, p8); // East
        var face5 = getBlocksInFace(p1, p2, p5, p6); // North
        var face6 = getBlocksInFace(p3, p4, p7, p8); // South
        return Stream.of(face1, face2, face3, face4, face5, face6)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }


    private static Set<BlockPos> getBlocksInFace(BlockPos corner1, BlockPos corner2, BlockPos corner3, BlockPos corner4) {
        Set<BlockPos> blocksInFace = new HashSet<>();
        // Determine the min and max coordinates along each axis
        int minX = Math.min(Math.min(corner1.getX(), corner2.getX()), Math.min(corner3.getX(), corner4.getX()));
        int minY = Math.min(Math.min(corner1.getY(), corner2.getY()), Math.min(corner3.getY(), corner4.getY()));
        int minZ = Math.min(Math.min(corner1.getZ(), corner2.getZ()), Math.min(corner3.getZ(), corner4.getZ()));
        int maxX = Math.max(Math.max(corner1.getX(), corner2.getX()), Math.max(corner3.getX(), corner4.getX()));
        int maxY = Math.max(Math.max(corner1.getY(), corner2.getY()), Math.max(corner3.getY(), corner4.getY()));
        int maxZ = Math.max(Math.max(corner1.getZ(), corner2.getZ()), Math.max(corner3.getZ(), corner4.getZ()));
        // Iterate through the grid defined by the min and max coordinates
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    var currentPos = new BlockPos(x, y, z);
                    if (isInFacePlane(currentPos, corner1, corner2, corner3, corner4)) {
                        blocksInFace.add(currentPos);
                    }
                }
            }
        }
        return blocksInFace;
    }

}