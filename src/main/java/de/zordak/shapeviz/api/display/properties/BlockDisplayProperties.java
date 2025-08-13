package de.zordak.shapeviz.api.display.properties;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.shapeviz.api.display.block.BlockDisplayUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import static de.zordak.shapeviz.api.display.block.BlockDisplayUtil.DEFAULT_CUSTOM_DATA_KEY;

public record BlockDisplayProperties(
    BlockState blockState,
    boolean glowing,
    ChatFormatting glowColor,
    int lightLevel,
    String tagKey,
    CompoundTag customData
) implements DisplayTypeProperties {

    @Override
    public String type() {
        return "block";
    }


    public static final Codec<BlockDisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("type").orElse("block").forGetter(BlockDisplayProperties::type),
        BlockState.CODEC.fieldOf("blockState")
                .orElse(BlockDisplayUtil.randomFromDefault())
                .forGetter(BlockDisplayProperties::blockState),
        Codec.BOOL.fieldOf("glowing")
                .orElse(false)
                .forGetter(BlockDisplayProperties::glowing),
        Codec.STRING.xmap(ChatFormatting::getByName, ChatFormatting::name)
                .fieldOf("glowColor")
                .orElse(ChatFormatting.WHITE)
                .forGetter(p -> ChatFormatting.getByName(p.glowColor().name())),
        Codec.INT.fieldOf("lightLevel")
                .orElse(15)
                .forGetter(BlockDisplayProperties::lightLevel),
        Codec.STRING.fieldOf("tagKey")
                .orElse(DEFAULT_CUSTOM_DATA_KEY)
                .forGetter(BlockDisplayProperties::tagKey),
        CompoundTag.CODEC.fieldOf("customData")
                .orElse(new CompoundTag())
                .forGetter(BlockDisplayProperties::customData)
    ).apply(instance, (type, blockState, glowing, glowColor, lightLevel, tagKey, customData) ->
            new BlockDisplayProperties(blockState, glowing, glowColor, lightLevel, tagKey, customData)
    ));
}
