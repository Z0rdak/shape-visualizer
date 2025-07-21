package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.ShapeVisualizer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public final class SingleBlockDisplayBuilder {

    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private int lightLevel = 15;
    private final BlockPos spawnPos;
    private String tagKey = ShapeVisualizer.MOD_ID + ":data";
    private CompoundTag customData = new CompoundTag();

    public SingleBlockDisplayBuilder(BlockPos spawnPos) {
        this.spawnPos = spawnPos;
    }

    public SingleBlockDisplayBuilder withBlock(Block block) {
        this.blockState = block.defaultBlockState();
        return this;
    }

    public SingleBlockDisplayBuilder withState(BlockState state) {
        this.blockState = state;
        return this;
    }

    public SingleBlockDisplayBuilder glowColor(ChatFormatting glowColor) {
        if (glowColor.getColor() == null) {
            this.glowColor = ChatFormatting.WHITE;
        } else {
            this.glowColor = glowColor;
        }
        this.glowing = true;
        return this;
    }

    public SingleBlockDisplayBuilder glowing(boolean glow) {
        this.glowing = glow;
        return this;
    }


    public SingleBlockDisplayBuilder customData(String tagKey, CompoundTag customData) {
        this.tagKey = tagKey;
        this.customData = customData;
        return this;
    }

    public SingleBlockDisplayBuilder lightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public SingleBlockDisplayHandle build(ServerLevel level) {
        Display.BlockDisplay blockDisplayEntity = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
        try {
            var display = new BlockDisplay(blockDisplayEntity);
            BlockDisplayUtil.updateBlock(display, blockState);
            BlockDisplayUtil.updateGlow(display, glowing);
            BlockDisplayUtil.updateGlowColor(display, glowColor);
            BlockDisplayUtil.updateLightLevel(display, lightLevel);
            BlockDisplayUtil.setCustomData(display, tagKey, customData);
            BlockDisplayUtil.updatePos(display, spawnPos);
            return new SingleBlockDisplayHandle(display);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }


}
