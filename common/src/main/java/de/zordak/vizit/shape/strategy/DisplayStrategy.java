package de.zordak.vizit.shape.strategy;

import de.zordak.vizit.shape.Shape;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

@FunctionalInterface
public interface DisplayStrategy {
    Set<Vec3> getBlockPositions(Shape shape);
}