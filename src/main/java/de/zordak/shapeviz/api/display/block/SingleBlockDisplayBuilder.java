package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static de.zordak.shapeviz.api.display.block.BlockDisplayUtil.DEFAULT_CUSTOM_DATA_KEY;

public final class SingleBlockDisplayBuilder {

    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private int lightLevel = 15;
    private final BlockPos spawnPos;
    private String tagKey = DEFAULT_CUSTOM_DATA_KEY;
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

    public SingleBlockDisplay build(ServerLevel level) {
        BlockDisplayProperties properties = new BlockDisplayProperties(
            blockState, glowing, glowColor, lightLevel, tagKey, customData
        );
        Display.BlockDisplay blockDisplayEntity = BlockDisplayUtil.createBlockDisplay(properties, spawnPos, level);
        return new SingleBlockDisplay(blockDisplayEntity.getUUID(), blockDisplayEntity, properties, spawnPos);
    }


}
