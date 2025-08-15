package de.zordak.vizit.shape;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

/**
 * Interface for all shapes that can be visualized.
 */
public interface Shape {

    ShapeType<? extends Shape> getType();

    boolean contains(BlockPos pos);
    boolean contains(Vec3 pos);

    Vec3 origin();
}

