package de.zordak.shapeviz.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
public record CuboidShape(BlockPos a, BlockPos b) implements Shape {

    public static final MapCodec<CuboidShape> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BlockPos.CODEC.fieldOf("a").forGetter(c -> new BlockPos(c.a)),
            BlockPos.CODEC.fieldOf("b").forGetter(c -> new BlockPos(c.b))
    ).apply(instance, CuboidShape::new));

    public CuboidShape {
        a = AreaUtil.getLowerPos(a, b);
        b = AreaUtil.getHigherPos(a, b);
    }

    public BoundingBox area() {
        return BoundingBox.fromCorners(a, b);
    }

    public int getXsize() {
        return Math.max(area().getXSpan(), 1);
    }

    public int getYsize() {
        return Math.max(area().getYSpan(), 1);
    }

    public int getZsize() {
        return Math.max(area().getZSpan(), 1);
    }

    @Override
    public ShapeType<? extends Shape> getType() {
        return ShapeTypes.CUBOID;
    }

    @Override
    public boolean contains(BlockPos pos) {
        var area = area();
        return pos.getX() >= area.minX() && pos.getX() <= area.maxX()
                && pos.getY() >= area.minY() && pos.getY() <= area.maxY()
                && pos.getZ() >= area.minZ() && pos.getZ() <= area.maxZ();
    }

    @Override
    public BlockPos origin() {
        return a;
    }
}