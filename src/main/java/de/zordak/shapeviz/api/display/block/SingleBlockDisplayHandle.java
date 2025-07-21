package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.api.display.properties.DisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class SingleBlockDisplayHandle implements BlockDisplayHandle {

    private final BlockDisplay display;

    public SingleBlockDisplayHandle(BlockDisplay display) {
        this.display = display;
    }

    @Override
    public Set<BlockDisplay> getEntities() {
        return Set.of(display);
    }

    @Override
    public void updateBlock(BlockState state) {
        try {
            BlockDisplayUtil.updateBlock(display, state);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGlow(boolean glowing) {
        display.blockDisplay().setGlowingTag(glowing);
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        try {
            BlockDisplayUtil.updateGlowColor(display, color);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLightLevel(int level) {
        try {
            BlockDisplayUtil.updateLightLevel(display, level);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        display.blockDisplay().discard();
    }
}
