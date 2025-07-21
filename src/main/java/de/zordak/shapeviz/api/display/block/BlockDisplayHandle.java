package de.zordak.shapeviz.api.display.block;

import net.minecraft.world.level.block.state.BlockState;

public  interface BlockDisplayHandle extends DisplayHandle {
    void updateBlock(BlockState state);
}
