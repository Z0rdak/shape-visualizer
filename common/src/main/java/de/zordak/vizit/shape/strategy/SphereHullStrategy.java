package de.zordak.vizit.shape.strategy;


import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.SphereShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class SphereHullStrategy implements DisplayStrategy {
    @Override
    public Set<Vec3> getBlockPositions(Shape shape) {
        if (!(shape instanceof SphereShape sphere)) return Set.of();

        var center = sphere.center();
        int r = sphere.radius();
        Set<Vec3> result = new HashSet<>();


        return result;
    }
}
