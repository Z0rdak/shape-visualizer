package de.zordak.shapeviz.shape;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

/**
 * Interface for all shapes that can be visualized.
 */
public interface Shape {

    ShapeType<? extends Shape> getType();

    boolean contains(BlockPos pos);

    BlockPos origin();
}

