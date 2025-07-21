package de.zordak.shapeviz.shape.strategy;


import de.zordak.shapeviz.shape.Shape;
import de.zordak.shapeviz.shape.SphereShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

import java.util.HashSet;
import java.util.Set;

public class SphereHullStrategy implements DisplayStrategy {
    @Override
    public Set<BlockPos> getBlockPositions(Shape shape) {
        if (!(shape instanceof SphereShape sphere)) return Set.of();

        Vec3i center = sphere.center();
        int r = sphere.radius();
        Set<BlockPos> result = new HashSet<>();


        return result;
    }
}
