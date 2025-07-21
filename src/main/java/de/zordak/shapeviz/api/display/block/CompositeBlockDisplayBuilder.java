package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

import static de.zordak.shapeviz.api.display.block.BlockDisplayUtil.DEFAULT_CUSTOM_DATA_KEY;

public final class CompositeBlockDisplayBuilder {

    private final Set<BlockPos> positions = new HashSet<>();
    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private int lightLevel = 0;
    private String tagKey = DEFAULT_CUSTOM_DATA_KEY;
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

    public CompositeBlockDisplay build(ServerLevel level) {
        BlockDisplayProperties properties = new BlockDisplayProperties(
                blockState, glowing, glowColor, lightLevel, tagKey, customData
        );
        Set<Display.BlockDisplay> blockDisplays = this.positions.stream()
            .map(pos -> BlockDisplayUtil.createBlockDisplay(properties, pos, level))
            .collect(Collectors.toSet());
        var uuid = UUID.randomUUID();
        return new CompositeBlockDisplay(uuid, blockDisplays, properties);
    }
}
