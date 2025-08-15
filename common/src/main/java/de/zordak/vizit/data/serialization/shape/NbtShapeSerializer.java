package de.zordak.vizit.data.serialization.shape;

import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.ShapeTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

public class NbtShapeSerializer {

    public static CompoundTag serialize(Shape shape) {
        return (CompoundTag) ShapeTypes.SHAPE_CODEC.encodeStart(NbtOps.INSTANCE, shape)
                .getOrThrow();
    }

    public static Shape deserialize(CompoundTag tag) {
        return ShapeTypes.SHAPE_CODEC.parse(NbtOps.INSTANCE, tag)
                .getOrThrow();
    }
}