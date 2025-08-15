package de.zordak.vizit.shape.strategy;

import de.zordak.vizit.shape.AreaUtil;
import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.Shape;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.Set;

public class CuboidMinimalStrategy implements DisplayStrategy {

    @Override
    public Set<Vec3> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape cuboid)) return Set.of();
        Set<Vec3> vertices = AreaUtil.getVertices(cuboid);
        double minX = cuboid.area().minX;
        double minY = cuboid.area().minY;
        double minZ = cuboid.area().minZ;
        double maxX = cuboid.area().maxX;
        double maxY = cuboid.area().maxY;
        double maxZ = cuboid.area().maxZ;
        Set<Vec3> result = new HashSet<>();
        for (Vec3 corner : vertices) {
            result.add(corner);
            if (minX != maxX) {
                double dx = (corner.x == minX) ? 1 : -1;
                result.add(corner.add(dx, 0, 0));
            }
            if (minY != maxY) {
                double dy = (corner.y == minY) ? 1 : -1;
                result.add(corner.add(0, dy, 0));
            }
            if (minZ != maxZ) {
                double dz = (corner.z == minZ) ? 1 : -1;
                result.add(corner.add(0, 0, dz));
            }
        }
        return result;
    }
}