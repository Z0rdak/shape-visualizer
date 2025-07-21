package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class SingleBlockDisplay implements BlockDisplayHandle {
    private final BlockDisplayProperties properties;
    private final BlockPos position;
    private final UUID uuid;
    private Display.BlockDisplay blockDisplay;

    public SingleBlockDisplay(Display.BlockDisplay display, UUID uuid, BlockPos pos, BlockDisplayProperties properties) {
        this.properties = properties;
        this.position = pos;
        this.uuid = uuid;
        this.blockDisplay = display;
    }

    public SingleBlockDisplay(UUID uuid, Display.BlockDisplay blockDisplay, BlockDisplayProperties properties, BlockPos position) {
        this.properties = properties;
        this.position = position;
        this.uuid = uuid;
        this.blockDisplay = blockDisplay;
    }

    public static final Codec<SingleBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("uuid")
                    .forGetter(SingleBlockDisplay::getId),
            BlockDisplayProperties.CODEC.fieldOf("properties")
                    .forGetter(SingleBlockDisplay::getProperties),
            BlockPos.CODEC.fieldOf("position")
                    .forGetter(SingleBlockDisplay::getPosition)
            // blockDisplay is intentionally omitted from serialization
    ).apply(instance, (uuid, properties, position) ->
            new SingleBlockDisplay(uuid, null, properties, position)
    ));

    @Override
    public UUID getId() {
        return this.uuid;
    }

    public void setEntity(Display.BlockDisplay blockDisplay) {
        this.blockDisplay = blockDisplay;
    }

    public Set<Display.BlockDisplay> getEntities() {
        return Set.of(this.blockDisplay);
    }

    public BlockDisplayProperties getProperties() {
        return properties;
    }

    public BlockPos getPosition() {
        return position;
    }

    public Display.BlockDisplay getBlockDisplayEntity() {
        return blockDisplay;
    }

    @Override
    public void updateBlock(BlockState state) {
        try {
            BlockDisplayUtil.updateBlock(blockDisplay, state);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateGlow(boolean glowing) {
        blockDisplay.setGlowingTag(glowing);
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        try {
            BlockDisplayUtil.updateGlowColor(blockDisplay, color);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateLightLevel(int level) {
        try {
            BlockDisplayUtil.updateLightLevel(blockDisplay, level);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove() {
        blockDisplay.discard();
    }
}
