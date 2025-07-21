package de.zordak.shapeviz.shape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class AreaUtil {

    private AreaUtil() {
    }

    public static double distance(BlockPos a, BlockPos b) {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2)
                + Math.pow(b.getY() - a.getY(), 2)
                + Math.pow(b.getZ() - a.getZ(), 2));
    }

    public static int distanceManhattan(BlockPos a, BlockPos b) {
        return Math.abs(b.getX() - a.getX())
                + Math.abs(b.getY() - a.getY())
                + Math.abs(b.getZ() - a.getZ());
    }

    public static double length(BlockPos a) {
        return Math.sqrt(Math.pow(a.getX(), 2)
                + Math.pow(a.getY(), 2)
                + Math.pow(a.getZ(), 2));
    }

    public static String blockPosStr(BlockPos pos) {
        return new StringJoiner(", ", "[", "]")
                .add(String.valueOf(pos.getX()))
                .add(String.valueOf(pos.getY()))
                .add(String.valueOf(pos.getZ()))
                .toString();
    }

    public static BlockPos getLowerPos(BlockPos pos1, BlockPos pos2) {
        return pos1.getZ() < pos2.getZ() ? pos1 : pos2;
    }

    public static BlockPos getHigherPos(BlockPos pos1, BlockPos pos2) {
        return pos1.getZ() > pos2.getZ() ? pos1 : pos2;
    }

    public static Set<BlockPos> getBoundingBoxFrame(BoundingBox box) {
        BlockPos[] corners = new BlockPos[]{
                new BlockPos(box.minX(), box.minY(), box.minZ()),
                new BlockPos(box.minX(), box.minY(), box.maxZ()),
                new BlockPos(box.minX(), box.maxY(), box.minZ()),
                new BlockPos(box.minX(), box.maxY(), box.maxZ()),
                new BlockPos(box.maxX(), box.minY(), box.minZ()),
                new BlockPos(box.maxX(), box.minY(), box.maxZ()),
                new BlockPos(box.maxX(), box.maxY(), box.minZ()),
                new BlockPos(box.maxX(), box.maxY(), box.maxZ())
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

        Set<BlockPos> frame = new HashSet<>();
        for (int[] pair : edgePairs) {
            frame.addAll(getEdge(corners[pair[0]], corners[pair[1]]));
        }

        return frame;
    }

    public static Set<BlockPos> getEdge(BlockPos a, BlockPos b) {
        Set<BlockPos> result = new HashSet<>();

        int dx = Integer.compare(b.getX(), a.getX());
        int dy = Integer.compare(b.getY(), a.getY());
        int dz = Integer.compare(b.getZ(), a.getZ());

        BlockPos current = a;
        result.add(current);

        while (!current.equals(b)) {
            current = current.offset(dx, dy, dz);
            result.add(current);
        }

        return result;
    }

    public static Set<BlockPos> getVertices(CuboidShape cuboid) {
        var area = cuboid.area();
        var p1 = new BlockPos(area.minX(), area.minY(), area.minZ());
        var p2 = new BlockPos(area.maxX(), area.minY(), area.minZ());
        var p3 = new BlockPos(area.minX(), area.minY(), area.maxZ());
        var p4 = new BlockPos(area.maxX(), area.minY(), area.maxZ());
        var p5 = new BlockPos(area.minX(), area.maxY(), area.minZ());
        var p6 = new BlockPos(area.maxX(), area.maxY(), area.minZ());
        var p7 = new BlockPos(area.minX(), area.maxY(), area.maxZ());
        var p8 = new BlockPos(area.maxX(), area.maxY(), area.maxZ());
        return Set.of(p1, p2, p3, p4, p5, p6, p7, p8);
    }

    public static boolean isInFacePlane(BlockPos point, BlockPos corner1, BlockPos corner2, BlockPos corner3, BlockPos corner4) {
        return point.getX() >= corner1.getX() && point.getX() <= corner2.getX()
                && point.getY() >= corner1.getY() && point.getY() <= corner3.getY()
                && point.getZ() >= corner1.getZ() && point.getZ() <= corner4.getZ();
    }

    public static Set<BlockPos> getFrame(CuboidShape cuboid) {
        BoundingBox area = cuboid.area();
        var p1 = new BlockPos(area.minX(), area.minY(), area.minZ());
        var p2 = new BlockPos(area.maxX(), area.minY(), area.minZ());
        var p3 = new BlockPos(area.minX(), area.minY(), area.maxZ());
        var p4 = new BlockPos(area.maxX(), area.minY(), area.maxZ());
        var p5 = new BlockPos(area.minX(), area.maxY(), area.minZ());
        var p6 = new BlockPos(area.maxX(), area.maxY(), area.minZ());
        var p7 = new BlockPos(area.minX(), area.maxY(), area.maxZ());
        var p8 = new BlockPos(area.maxX(), area.maxY(), area.maxZ());
        Set<BlockPos> p12 = blocksBetweenOnAxis(p1, p2, Direction.Axis.X);
        Set<BlockPos> p34 = blocksBetweenOnAxis(p3, p4, Direction.Axis.X);
        Set<BlockPos> p56 = blocksBetweenOnAxis(p5, p6, Direction.Axis.X);
        Set<BlockPos> p78 = blocksBetweenOnAxis(p7, p8, Direction.Axis.X);
        Set<BlockPos> p15 = blocksBetweenOnAxis(p1, p5, Direction.Axis.Y);
        Set<BlockPos> p26 = blocksBetweenOnAxis(p2, p6, Direction.Axis.Y);
        Set<BlockPos> p37 = blocksBetweenOnAxis(p3, p7, Direction.Axis.Y);
        Set<BlockPos> p48 = blocksBetweenOnAxis(p4, p8, Direction.Axis.Y);
        Set<BlockPos> p13 = blocksBetweenOnAxis(p1, p3, Direction.Axis.Z);
        Set<BlockPos> p24 = blocksBetweenOnAxis(p2, p4, Direction.Axis.Z);
        Set<BlockPos> p57 = blocksBetweenOnAxis(p5, p7, Direction.Axis.Z);
        Set<BlockPos> p68 = blocksBetweenOnAxis(p6, p8, Direction.Axis.Z);
        return Stream.of(p12, p34, p56, p78, p15, p26, p37, p48, p13, p24, p57, p68)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }


    public static Set<BlockPos> blocksBetweenOnAxis(BlockPos p1, BlockPos p2, Direction.Axis axis) {
        BoundingBox blockLine = BoundingBox.fromCorners(p1, p2);
        Set<BlockPos> blocks = new HashSet<>();
        switch (axis) {
            case X:
                for (int x = blockLine.minX(); x <= blockLine.maxX(); x++) {
                    blocks.add(new BlockPos(x, p1.getY(), p1.getZ()));
                }
                break;
            case Y:
                for (int y = blockLine.minY(); y <= blockLine.maxY(); y++) {
                    blocks.add(new BlockPos(p1.getX(), y, p1.getZ()));
                }
                break;
            case Z:
                for (int z = blockLine.minZ(); z <= blockLine.maxZ(); z++) {
                    blocks.add(new BlockPos(p1.getX(), p1.getY(), z));
                }
                break;
        }
        return blocks;
    }

    public static Set<BlockPos> blocksIn(BoundingBox cube) {
        Set<BlockPos> blocks = new HashSet<>();
        for (int x = cube.minX(); x <= cube.maxX(); x++) {
            for (int y = cube.minY(); y <= cube.maxY(); y++) {
                for (int z = cube.minZ(); z <= cube.maxZ(); z++) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
        }

    public static Set<BlockPos> blocksIn(BoundingBox cube, Predicate<BlockPos> inclusion) {
        Set<BlockPos> blocks = new HashSet<>();
        for (int x = cube.minX(); x <= cube.maxX(); x++) {
            for (int y = cube.minY(); y <= cube.maxY(); y++) {
                for (int z = cube.minZ(); z <= cube.maxZ(); z++) {
                    BlockPos blockPos = new BlockPos(x, y, z);
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

    public static BoundingBox getSlice(BlockPos center, int halfSize, int offset, Direction.Axis axis) {
        return switch (axis) {
            case X -> {
                var p1 = center.offset(halfSize, halfSize, offset);
                var p2 = center.offset(-halfSize, -halfSize, offset);
                yield  BoundingBox.fromCorners(p1, p2);
            }
            case Y -> {
                var p1 = center.offset(halfSize, offset, halfSize);
                var p2 = center.offset(-halfSize, offset, -halfSize);
                yield BoundingBox.fromCorners(p1, p2);
            }
            case Z -> {
                var p1 = center.offset(offset, halfSize, halfSize);
                var p2 = center.offset(offset, -halfSize, -halfSize);
                yield BoundingBox.fromCorners(p1, p2);
            }
        };
    }

    public static Set<BlockPos> getSliceBlocks(BlockPos center, int halfSize, int offset, Direction.Axis axis, Predicate<BlockPos> include) {
        var slice = getSlice(center, halfSize, offset, axis);
        return blocksIn(slice, include);
    }
}