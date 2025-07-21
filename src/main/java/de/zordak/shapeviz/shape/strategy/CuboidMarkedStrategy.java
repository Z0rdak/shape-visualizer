package de.zordak.shapeviz.shape.strategy;

import de.zordak.shapeviz.shape.CuboidShape;
import de.zordak.shapeviz.shape.Shape;
import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class CuboidMarkedStrategy implements DisplayStrategy {

    @Override
    public Set<BlockPos> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape(BlockPos a, BlockPos b)))
            return Set.of();
        HashSet<BlockPos> marked = new HashSet<>();
        marked.add(a);
        marked.add(b);
        return marked;
    }
}