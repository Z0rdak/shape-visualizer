package de.zordak.vizit.shape.strategy;

import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.Shape;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static de.zordak.vizit.shape.AreaUtil.isInFacePlane;

public class CuboidHullStrategy implements DisplayStrategy {
    @Override
    public Set<Vec3> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape cuboid)) return Set.of();
        return getHull(cuboid);
    }

    public static Set<Vec3> getHull(CuboidShape cuboid) {
        var area = cuboid.area();
        var p1 = new Vec3(area.minX, area.minY, area.minZ);
        var p2 = new Vec3(area.maxX, area.minY, area.minZ);
        var p3 = new Vec3(area.minX, area.minY, area.maxZ);
        var p4 = new Vec3(area.maxX, area.minY, area.maxZ);
        var p5 = new Vec3(area.minX, area.maxY, area.minZ);
        var p6 = new Vec3(area.maxX, area.maxY, area.minZ);
        var p7 = new Vec3(area.minX, area.maxY, area.maxZ);
        var p8 = new Vec3(area.maxX, area.maxY, area.maxZ);
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

    private static Set<Vec3> getBlocksInFace(Vec3 corner1, Vec3 corner2, Vec3 corner3, Vec3 corner4) {
        Set<Vec3> blocksInFace = new HashSet<>();
        double minX = Math.min(Math.min(corner1.x, corner2.x), Math.min(corner3.x, corner4.x));
        double minY = Math.min(Math.min(corner1.y, corner2.y), Math.min(corner3.y, corner4.y));
        double minZ = Math.min(Math.min(corner1.z, corner2.z), Math.min(corner3.z, corner4.z));
        double maxX = Math.max(Math.max(corner1.x, corner2.x), Math.max(corner3.x, corner4.x));
        double maxY = Math.max(Math.max(corner1.y, corner2.y), Math.max(corner3.y, corner4.y));
        double maxZ = Math.max(Math.max(corner1.z, corner2.z), Math.max(corner3.z, corner4.z));
        for (double x = minX; x <= maxX; x++) {
            for (double y = minY; y <= maxY; y++) {
                for (double z = minZ; z <= maxZ; z++) {
                    var currentPos = new Vec3(x, y, z);
                    if (isInFacePlane(currentPos, corner1, corner2, corner3, corner4)) {
                        blocksInFace.add(currentPos);
                    }
                }
            }
        }
        return blocksInFace;
    }

}