package de.zordak.shapeviz.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record SphereShape(BlockPos center, int radius) implements Shape {

    public static final MapCodec<SphereShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("center").forGetter( s -> new BlockPos(s.center)),
            Codec.INT.fieldOf("radius").forGetter(SphereShape::radius)
    ).apply(instance, SphereShape::new));

    public static final MapCodec<SphereShape> MAP_CODEC = CODEC.fieldOf("data");



    @Override
    public ShapeType<? extends Shape> getType() {
        return ShapeTypes.SPHERE;
    }

    public static SphereShape fromCenterAndScope(BlockPos center, BlockPos scope) {
        int r = (int) (distance(center, scope) + 0.5);
        return new SphereShape(center, r);
    }

    @Override
    public boolean contains(BlockPos pos) {
        return distance(center, pos) < radius + 0.5;
    }

    @Override
    public BlockPos origin() {
        return center;
    }

    public boolean isHullBlock(BlockPos pos) {
        double d = distance(center, pos);
        return d > radius - 0.5 && d < radius + 0.5;
    }

    public Set<BlockPos> getHull() {
        BlockPos p1 = center.offset(-radius, -radius, -radius);
        BlockPos p2 = center.offset(radius, radius, radius);
        var cube = BoundingBox.fromCorners(p1, p2);
        return AreaUtil.blocksIn(cube).stream()
                .filter(this::isHullBlock)
                .collect(Collectors.toSet());
    }

    public Set<BlockPos> getMinimalOutline() {
        Set<BlockPos> frame = new HashSet<>();
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, Direction.Axis.X, this::isHullBlock));
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, Direction.Axis.Y, this::isHullBlock));
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, Direction.Axis.Z, this::isHullBlock));
        return frame;
    }

    public Set<BlockPos> getFrame() {
        Set<BlockPos> frame = getMinimalOutline();
        int h = radius / 2;
        for (Direction.Axis axis : Direction.Axis.values()) {
            frame.addAll(AreaUtil.getSliceBlocks(center, radius, h, axis, this::isHullBlock));
            frame.addAll(AreaUtil.getSliceBlocks(center, radius, -h, axis, this::isHullBlock));
        }
        return frame;
    }

    @Override
    public String toString() {
        return "Sphere " + center.toShortString() + ", r=" + radius;
    }

    private static double distance(BlockPos a, BlockPos b) {
        int dx = a.getX() - b.getX();
        int dy = a.getY() - b.getY();
        int dz = a.getZ() - b.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}