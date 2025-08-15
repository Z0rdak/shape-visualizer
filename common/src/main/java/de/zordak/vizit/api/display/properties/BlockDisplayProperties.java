package de.zordak.vizit.api.display.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.vizit.Constants;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;


public record BlockDisplayProperties(
    BlockState blockState,
    String tagKey,
    CompoundTag customData
) implements DisplayTypeProperties {

    @Override
    public String type() {
        return "block";
    }

    public static final String TAG_BLOCK_STATE = "block_state";

    public static final Codec<BlockDisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockState.CODEC.fieldOf(TAG_BLOCK_STATE)
                    .orElse(BlockDisplayUtil.randomFromDefault())
                    .forGetter(BlockDisplayProperties::blockState),
            Codec.STRING.fieldOf("tagKey")
                    .orElse(Constants.TAG_DEFAULT_CUSTOM_DATA)
                    .forGetter(BlockDisplayProperties::tagKey),
            CompoundTag.CODEC.fieldOf("customData")
                    .orElse(new CompoundTag())
                    .forGetter(BlockDisplayProperties::customData),
            Codec.STRING.fieldOf("type").orElse("block").forGetter(BlockDisplayProperties::type)
            ).apply(instance, (blockState, tagKey, customData, type) ->
            new BlockDisplayProperties(blockState, tagKey, customData)
    ));
}
