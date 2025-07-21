package de.zordak.shapeviz.shape.strategy;

import de.zordak.shapeviz.shape.AreaUtil;
import de.zordak.shapeviz.shape.CuboidShape;
import de.zordak.shapeviz.shape.Shape;
import net.minecraft.core.BlockPos;

import java.util.Set;


public class CuboidFrameStrategy implements DisplayStrategy {

    @Override
    public Set<BlockPos> getBlockPositions(Shape params) {
        if (!(params instanceof CuboidShape cuboid)) return Set.of();

        return AreaUtil.getFrame(cuboid);
    }
}