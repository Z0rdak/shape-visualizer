package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.api.display.properties.DisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Function;

public class CompositeBlockDisplayHandle implements BlockDisplayHandle {

    private final CompositeBlockDisplay composite;

    public CompositeBlockDisplayHandle(CompositeBlockDisplay composite) {
        this.composite = composite;
    }

    @Override
    public Set<BlockDisplay> getEntities() {
        return composite.displays();
    }

    @Override
    public void updateBlock(BlockState state) {
        for (BlockDisplay display : composite.displays()) {
            try {
                BlockDisplayUtil.updateBlock(display, state);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateGlow(boolean glowing) {
        for (BlockDisplay display : composite.displays()) {
            display.blockDisplay().setGlowingTag(glowing);
        }
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        for (BlockDisplay display : composite.displays()) {
            try {
                BlockDisplayUtil.updateGlowColor(display, color);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateLightLevel(int level) {
        for (BlockDisplay display : composite.displays()) {
            try {
                BlockDisplayUtil.updateLightLevel(display, level);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void remove() {
        for (BlockDisplay display : composite.displays()) {
            display.blockDisplay().discard();
        }
    }

}
