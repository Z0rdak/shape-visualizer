package de.zordak.vizit.shape;

import net.minecraft.core.Direction;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;

public final class AreaUtil {

    private AreaUtil() {
    }

    public static double distance(Vec3 a, Vec3 b) {
        return a.distanceTo(b);
    }

    public static double distanceManhattan(Vec3 a, Vec3 b) {
        return Math.abs(b.x - a.x)
                + Math.abs(b.y - a.y)
                + Math.abs(b.z - a.z);
    }

    public static double length(Vec3 a) {
        return Math.sqrt(a.x * a.x + a.y * a.y + a.z * a.z);
    }

    public static String vec3Str(Vec3 pos) {
        return new StringJoiner(", ", "[", "]")
                .add(String.valueOf(pos.x))
                .add(String.valueOf(pos.y))
                .add(String.valueOf(pos.z))
                .toString();
    }

    public static Vec3 getLowerVec3(Vec3 pos1, Vec3 pos2) {
        return new Vec3(
                Math.min(pos1.x, pos2.x),
                Math.min(pos1.y, pos2.y),
                Math.min(pos1.z, pos2.z)
        );
    }

    public static Vec3 getHigherVec3(Vec3 pos1, Vec3 pos2) {
        return new Vec3(
                Math.max(pos1.x, pos2.x),
                Math.max(pos1.y, pos2.y),
                Math.max(pos1.z, pos2.z)
        );
    }

    // TODO: Verify
    public static Set<Vec3> getBoundingBoxFrame(CuboidShape cuboid) {
        BoundingBox box = new BoundingBox(0,0,0,0,0,0);
        Vec3[] corners = new Vec3[]{
                new Vec3(box.minX(), box.minY(), box.minZ()),
                new Vec3(box.minX(), box.minY(), box.maxZ()),
                new Vec3(box.minX(), box.maxY(), box.minZ()),
                new Vec3(box.minX(), box.maxY(), box.maxZ()),
                new Vec3(box.maxX(), box.minY(), box.minZ()),
                new Vec3(box.maxX(), box.minY(), box.maxZ()),
                new Vec3(box.maxX(), box.maxY(), box.minZ()),
                new Vec3(box.maxX(), box.maxY(), box.maxZ())
        };

        int[][] edgePairs = {
                {0, 1}, {0, 2}, {0, 4},
                {1, 3}, {1, 5},
                {2, 3}, {2, 6},
                {3, 7},
                {4, 5}, {4, 6},
                {5, 7},
                {6, 7}
        };

        Set<Vec3> frame = new HashSet<>();
        for (int[] pair : edgePairs) {
            frame.addAll(getEdge(corners[pair[0]], corners[pair[1]]));
        }

        return frame;
    }

    public static Set<Vec3> getBoundingBoxFrame(BoundingBox box) {
        Vec3[] corners = new Vec3[]{
                new Vec3(box.minX(), box.minY(), box.minZ()),
                new Vec3(box.minX(), box.minY(), box.maxZ()),
                new Vec3(box.minX(), box.maxY(), box.minZ()),
                new Vec3(box.minX(), box.maxY(), box.maxZ()),
                new Vec3(box.maxX(), box.minY(), box.minZ()),
                new Vec3(box.maxX(), box.minY(), box.maxZ()),
                new Vec3(box.maxX(), box.maxY(), box.minZ()),
                new Vec3(box.maxX(), box.maxY(), box.maxZ())
        };

        int[][] edgePairs = {
                {0, 1}, {0, 2}, {0, 4},
                {1, 3}, {1, 5},
                {2, 3}, {2, 6},
                {3, 7},
                {4, 5}, {4, 6},
                {5, 7},
                {6, 7}
        };

        Set<Vec3> frame = new HashSet<>();
        for (int[] pair : edgePairs) {
            frame.addAll(getEdge(corners[pair[0]], corners[pair[1]]));
        }

        return frame;
    }

    public static Set<Vec3> getEdge(Vec3 a, Vec3 b) {
        Set<Vec3> result = new HashSet<>();
        int steps = (int) Math.max(Math.abs(b.x - a.x), Math.max(Math.abs(b.y - a.y), Math.abs(b.z - a.z)));
        for (int i = 0; i <= steps; i++) {
            double t = steps == 0 ? 0 : (double) i / steps;
            double x = a.x + (b.x - a.x) * t;
            double y = a.y + (b.y - a.y) * t;
            double z = a.z + (b.z - a.z) * t;
            result.add(new Vec3(x, y, z));
        }
        return result;
    }

    public static Set<Vec3> getVertices(CuboidShape cuboid) {
        var area = cuboid.area();
        var p1 = new Vec3(area.minX, area.minY, area.minZ);
        var p2 = new Vec3(area.maxX, area.minY, area.minZ);
        var p3 = new Vec3(area.minX, area.minY, area.maxZ);
        var p4 = new Vec3(area.maxX, area.minY, area.maxZ);
        var p5 = new Vec3(area.minX, area.maxY, area.minZ);
        var p6 = new Vec3(area.maxX, area.maxY, area.minZ);
        var p7 = new Vec3(area.minX, area.maxY, area.maxZ);
        var p8 = new Vec3(area.maxX, area.maxY, area.maxZ);
        return Set.of(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public static boolean isInFacePlane(Vec3 point, Vec3 corner1, Vec3 corner2, Vec3 corner3, Vec3 corner4) {
        return point.x >= corner1.x && point.x <= corner2.x
                && point.y >= corner1.y && point.y <= corner3.y
                && point.z >= corner1.z && point.z <= corner4.z;
    }

    public static Set<Vec3> blocksBetweenOnAxis(Vec3 p1, Vec3 p2, Direction.Axis axis) {

        AABB blockLine = new AABB(p1, p2);
        Set<Vec3> blocks = new HashSet<>();
        switch (axis) {
            case X:
                for (double x = blockLine.minX; x <= blockLine.maxX; x++) {
                    blocks.add(new Vec3(x, p1.y, p1.z));
                }
                break;
            case Y:
                for (var y = blockLine.minY; y <= blockLine.maxY; y++) {
                    blocks.add(new Vec3(p1.x, y, p1.z));
                }
                break;
            case Z:
                for (var z = blockLine.minZ; z <= blockLine.maxZ; z++) {
                    blocks.add(new Vec3(p1.x, p1.y, z));
                }
                break;
        }
        return blocks;
    }

    public static Set<Vec3> blocksIn(AABB cube) {
        Set<Vec3> blocks = new HashSet<>();
        for (var x = cube.minX; x <= cube.maxX; x++) {
            for (var y = cube.minY; y <= cube.maxY; y++) {
                for (var z = cube.minZ; z <= cube.maxZ; z++) {
                    blocks.add(new Vec3(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static Set<Vec3> blocksIn(AABB cube, java.util.function.Predicate<Vec3> inclusion) {
        Set<Vec3> blocks = new HashSet<>();
        for (var x = cube.minX; x <= cube.maxX; x++) {
            for (var y = cube.minY; y <= cube.maxY; y++) {
                for (var z = cube.minZ; z <= cube.maxZ; z++) {
                    Vec3 blockPos = new Vec3(x, y, z);
                    if (inclusion.test(blockPos)) {
                        blocks.add(blockPos);
                    }
                }
            }
        }
        return blocks;
    }

    public static int blocksOnAxis(BoundingBox box, Direction.Axis axis) {
        switch (axis) {
            case X:
                return box.getXSpan();
            case Y:
                return box.getYSpan();
            case Z:
                return box.getZSpan();
            default:
                throw new IllegalArgumentException();
        }
    }

    public static AABB getSlice(Vec3 center, double halfSize, double offset, Direction.Axis axis) {
        return switch (axis) {
            case X -> {
                var p1 = center.add(halfSize, halfSize, offset);
                var p2 = center.add(-halfSize, -halfSize, offset);
                yield new AABB(p1, p2);
            }
            case Y -> {
                var p1 = center.add(halfSize, offset, halfSize);
                var p2 = center.add(-halfSize, offset, -halfSize);
                yield new AABB(p1, p2);
            }
            case Z -> {
                var p1 = center.add(offset, halfSize, halfSize);
                var p2 = center.add(offset, -halfSize, -halfSize);
                yield new AABB(p1, p2);
            }
        };
    }

    public static Set<Vec3> getSliceBlocks(Vec3 center, double halfSize, double offset, Direction.Axis axis, java.util.function.Predicate<Vec3> include) {
        var slice = getSlice(center, halfSize, offset, axis);
        return blocksIn(slice, include);
    }
}