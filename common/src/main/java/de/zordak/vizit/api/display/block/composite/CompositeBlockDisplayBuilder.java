package de.zordak.vizit.api.display.block.composite;

import de.zordak.vizit.Constants;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class CompositeBlockDisplayBuilder {

    private final Set<BlockPos> positions = new HashSet<>();
    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private String name;
    private int lightLevel = 0;
    private String tagKey = Constants.TAG_DEFAULT_CUSTOM_DATA;
    private CompoundTag customData = new CompoundTag();

    public CompositeBlockDisplayBuilder(@NotNull String name) {
        this.name = name;
    }

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
        BlockDisplayProperties properties = new BlockDisplayProperties(blockState, tagKey, customData);
        Set<Display.BlockDisplay> blockDisplays = this.positions.stream()
            .map(pos -> BlockDisplayUtil.createBlockDisplay(null, properties, level))
            .collect(Collectors.toSet());
        var uuid = UUID.randomUUID();
        return new CompositeBlockDisplay(uuid, name, blockDisplays, properties);
    }
}
