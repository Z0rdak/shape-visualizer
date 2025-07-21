package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.ShapeVisualizer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public final class CompositeBlockDisplayBuilder {

    private final Set<BlockPos> positions = new HashSet<>();
    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private int lightLevel = 0;
    private String tagKey = ShapeVisualizer.MOD_ID + ":data";
    private CompoundTag customData = new CompoundTag();

    public CompositeBlockDisplayBuilder add(BlockPos pos, BlockState state) {
        positions.add(pos);
        return this;
    }

    public CompositeBlockDisplayBuilder glowColor(ChatFormatting color) {
        this.glowColor = color.getColor() == null ? ChatFormatting.WHITE : color;
        return this;
    }

    public CompositeBlockDisplayBuilder glowing(boolean glow) {
        this.glowing = glow;
        return this;
    }

    public CompositeBlockDisplayBuilder customData(String tagKey, CompoundTag customData) {
        this.tagKey = tagKey;
        this.customData = customData;
        return this;
    }

    public CompositeBlockDisplayBuilder lightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public CompositeBlockDisplayHandle build(ServerLevel level) {
        Set<BlockDisplay> blockDisplays = this.positions.stream().map(pos -> {
                    Display.BlockDisplay displayEntity = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
                    BlockDisplay display = new BlockDisplay(displayEntity);

                    try {
                        BlockDisplayUtil.updateBlock(display, blockState);
                        BlockDisplayUtil.updateGlow(display, glowing);
                        BlockDisplayUtil.updateGlowColor(display, glowColor);
                        BlockDisplayUtil.updateLightLevel(display, lightLevel);
                        BlockDisplayUtil.setCustomData(display, tagKey, customData);
                        BlockDisplayUtil.updatePos(display, pos);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                    return display;
                }
        ).collect(Collectors.toSet());

        return new CompositeBlockDisplayHandle(new CompositeBlockDisplay(blockDisplays));
    }
}

