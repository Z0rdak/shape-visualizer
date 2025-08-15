package de.zordak.vizit.shape;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record CuboidShape(Vec3 a, Vec3 b) implements Shape {

    public static final MapCodec<CuboidShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Vec3.CODEC.fieldOf("a").forGetter(c -> c.a),
            Vec3.CODEC.fieldOf("b").forGetter(c -> c.b)
    ).apply(instance, CuboidShape::new));

    public CuboidShape(Vec3 a, Vec3 b) {
        this.a = AreaUtil.getLowerVec3(a, b);
        this.b = AreaUtil.getHigherVec3(a, b);
    }

    public AABB area() {
        return new AABB(a, b);
    }

    public double getXsize() {
        return Math.abs(a.x - b.x);
    }

    public double getYsize() {
        return Math.abs(a.y - b.y);
    }

    public double getZsize() {
        return Math.abs(a.z - b.z);
    }

    @Override
    public ShapeType<? extends Shape> getType() {
        return ShapeTypes.CUBOID;
    }

    @Override
    public boolean contains(Vec3 pos) {
        var area = area();
        return pos.x >= area.minX && pos.x <= area.maxX
                && pos.y >= area.minY && pos.y <= area.maxY
                && pos.z >= area.minZ && pos.z <= area.maxZ;
    }

    @Override
    public boolean contains(BlockPos pos) {
        return this.contains(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    @Override
    public Vec3 origin() {
        return a;
    }
}