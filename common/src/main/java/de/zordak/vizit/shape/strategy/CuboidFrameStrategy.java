package de.zordak.vizit.shape.strategy;

import de.zordak.vizit.shape.AreaUtil;
import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.Shape;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public class CuboidFrameStrategy implements DisplayStrategy {

    @Override
    public Set<Vec3> getBlockPositions(Shape params) {
        if (!(params instanceof CuboidShape cuboid)) return Set.of();

        return AreaUtil.getBoundingBoxFrame(cuboid);
    }
}