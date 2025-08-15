package de.zordak.vizit.api.display.block;

import de.zordak.vizit.api.display.DisplayHandle;
import net.minecraft.world.level.block.state.BlockState;

public  interface BlockDisplayHandle extends DisplayHandle {
    void updateBlock(BlockState state);
}
