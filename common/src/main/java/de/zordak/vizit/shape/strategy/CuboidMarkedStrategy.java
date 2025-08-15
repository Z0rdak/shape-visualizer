package de.zordak.vizit.shape.strategy;

import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.Shape;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class CuboidMarkedStrategy implements DisplayStrategy {

    @Override
    public Set<Vec3> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape(Vec3 a, Vec3 b)))
            return Set.of();
        HashSet<Vec3> marked = new HashSet<>();
        marked.add(a);
        marked.add(b);
        return marked;
    }
}