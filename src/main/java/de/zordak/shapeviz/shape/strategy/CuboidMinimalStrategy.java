package de.zordak.shapeviz.shape.strategy;

import de.zordak.shapeviz.shape.AreaUtil;
import de.zordak.shapeviz.shape.CuboidShape;
import de.zordak.shapeviz.shape.Shape;
import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

public class CuboidMinimalStrategy implements DisplayStrategy {

    @Override
    public Set<BlockPos> getBlockPositions(Shape shape) {
        if (!(shape instanceof CuboidShape cuboid)) return Set.of();
        Set<BlockPos> vertices = AreaUtil.getVertices(cuboid);
        int minX = cuboid.area().minX();
        int minY = cuboid.area().minY();
        int minZ = cuboid.area().minZ();
        int maxX = cuboid.area().maxX();
        int maxY = cuboid.area().maxY();
        int maxZ = cuboid.area().maxZ();
        Set<BlockPos> result = new HashSet<>();
        for (BlockPos corner : vertices) {
            result.add(corner);
            if (minX != maxX) {
                int dx = (corner.getX() == minX) ? 1 : -1;
                result.add(corner.offset(dx, 0, 0));
            }
            if (minY != maxY) {
                int dy = (corner.getY() == minY) ? 1 : -1;
                result.add(corner.offset(0, dy, 0));
            }
            if (minZ != maxZ) {
                int dz = (corner.getZ() == minZ) ? 1 : -1;
                result.add(corner.offset(0, 0, dz));
            }
        }
        return result;
    }
}