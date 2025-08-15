package de.zordak.vizit.api.display.block;

import de.zordak.vizit.Constants;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import de.zordak.vizit.api.display.properties.DisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;


public final class SingleBlockDisplayBuilder {

    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private final String name;
    private final Vec3 spawnPos;
    private String tagKey = Constants.TAG_DEFAULT_CUSTOM_DATA;
    private CompoundTag customData = new CompoundTag();
    private DisplayProperties displayProperties;

    public SingleBlockDisplayBuilder(Vec3 spawnPos, String name) {
        this.spawnPos = spawnPos;
        this.name = name;
    }

    public SingleBlockDisplayBuilder withProperties(DisplayProperties displayProperties) {
        this.displayProperties = displayProperties;
        return this;
    }

    public SingleBlockDisplayBuilder withBlock(Block block) {
        this.blockState = block.defaultBlockState();
        return this;
    }

    public SingleBlockDisplayBuilder withState(BlockState state) {
        this.blockState = state;
        return this;
    }

    public SingleBlockDisplayBuilder customData(String tagKey, CompoundTag customData) {
        this.tagKey = tagKey;
        this.customData = customData;
        return this;
    }

    public SingleBlockDisplay build(ServerLevel level) {
        DisplayProperties usedDisplayProperties = displayProperties != null
                ? displayProperties
                : DisplayProperties.defaultFor(spawnPos);
        BlockDisplayProperties properties = new BlockDisplayProperties(blockState, tagKey, customData);
        Display.BlockDisplay blockDisplayEntity = BlockDisplayUtil.createBlockDisplay(usedDisplayProperties, properties, level);
        return new SingleBlockDisplay(blockDisplayEntity.getUUID(), name, blockDisplayEntity, usedDisplayProperties, properties);
    }


}
