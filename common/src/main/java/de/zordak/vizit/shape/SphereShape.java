package de.zordak.vizit.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public record SphereShape(Vec3 center, int radius) implements Shape {

    public static final MapCodec<SphereShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("center").forGetter(s -> s.center),
            Codec.INT.fieldOf("radius").forGetter(SphereShape::radius)
    ).apply(instance, SphereShape::new));

    public static final MapCodec<SphereShape> MAP_CODEC = CODEC.fieldOf("data");

    @Override
    public ShapeType<? extends Shape> getType() {
        return ShapeTypes.SPHERE;
    }

    @Override
    public boolean contains(BlockPos pos) {
        return contains(new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
    }

    @Override
    public boolean contains(Vec3 pos) {
        return center.distanceTo(pos) < radius;
    }

    @Override
    public Vec3 origin() {
        return center;
    }

    public boolean isHullBlock(Vec3 pos) {
        double d = center.distanceTo(pos);
        return d > radius - 0.5 && d < radius + 0.5;
    }

    public Set<Vec3> getHull() {
        Vec3 p1 = center.add(-radius, -radius, -radius);
        Vec3 p2 = center.add(radius, radius, radius);
        var cube = new AABB(p1, p2);
        return AreaUtil.blocksIn(cube).stream()
                .filter(this::isHullBlock)
                .collect(Collectors.toSet());
    }

    public Set<Vec3> getMinimalOutline() {
        Set<Vec3> frame = new HashSet<>();
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, net.minecraft.core.Direction.Axis.X, this::isHullBlock));
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, net.minecraft.core.Direction.Axis.Y, this::isHullBlock));
        frame.addAll(AreaUtil.getSliceBlocks(center, radius, 0, net.minecraft.core.Direction.Axis.Z, this::isHullBlock));
        return frame;
    }

    public Set<Vec3> getFrame() {
        Set<Vec3> frame = getMinimalOutline();
        double h = radius / 2.0;
        for (net.minecraft.core.Direction.Axis axis : net.minecraft.core.Direction.Axis.values()) {
            frame.addAll(AreaUtil.getSliceBlocks(center, radius, h, axis, this::isHullBlock));
            frame.addAll(AreaUtil.getSliceBlocks(center, radius, -h, axis, this::isHullBlock));
        }
        return frame;
    }

    @Override
    public String toString() {
        return "Sphere " + center + ", r=" + radius;
    }
}