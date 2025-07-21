package de.zordak.shapeviz.shape.strategy;

import de.zordak.shapeviz.shape.Shape;
import net.minecraft.core.BlockPos;

import java.util.Set;

@FunctionalInterface
public interface DisplayStrategy {
    Set<BlockPos> getBlockPositions(Shape shape);
}