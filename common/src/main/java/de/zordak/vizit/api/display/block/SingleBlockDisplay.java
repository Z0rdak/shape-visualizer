package de.zordak.vizit.api.display.block;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import de.zordak.vizit.api.display.properties.DisplayEntityUtils;
import de.zordak.vizit.api.display.properties.DisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

public class SingleBlockDisplay implements BlockDisplayHandle {
    private final BlockDisplayProperties blockProperties;
    private final DisplayProperties properties;
    private final UUID uuid;
    private final String name;
    @Nullable
    private Display.BlockDisplay blockDisplay;

    private boolean isVisible;

    public SingleBlockDisplay(UUID uuid, String name, Display.BlockDisplay blockDisplay, DisplayProperties properties, BlockDisplayProperties blockProperties) {
        this.blockProperties = blockProperties;
        this.properties = properties;
        this.name = name;
        this.uuid = blockDisplay.getUUID();
        this.blockDisplay = blockDisplay;
    }

    public static final Codec<SingleBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.STRING_CODEC.fieldOf("uuid")
                    .forGetter(SingleBlockDisplay::getId),
            Codec.STRING.fieldOf("name")
                    .forGetter(SingleBlockDisplay::getName),
            BlockDisplayProperties.CODEC.fieldOf("properties")
                    .forGetter(SingleBlockDisplay::getBlockProperties),
            DisplayProperties.CODEC.fieldOf("properties")
                    .forGetter(SingleBlockDisplay::getDisplayProperties)
            // blockDisplay is intentionally omitted from serialization
    ).apply(instance, (uuid, name, blockProperties, properties) ->
            new SingleBlockDisplay(uuid, name, null, properties, blockProperties)
    ));

    @Override
    public UUID getId() {
        return this.uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isVisible() {
        return this.blockDisplay != null && this.isVisible;
    }

    public void setEntity(Display.BlockDisplay blockDisplay) {
        this.blockDisplay = blockDisplay;
    }

    public Set<Display.BlockDisplay> getEntities() {
        return Set.of(this.blockDisplay);
    }

    public BlockDisplayProperties getBlockProperties() {
        return blockProperties;
    }

    public DisplayProperties getDisplayProperties() {
        return properties;
    }

    public Display.BlockDisplay getOrCreateDisplay(ServerLevel level) {
        Entity entity = level.getEntity(this.uuid);
        return blockDisplay;
    }

    @Override
    public void updateBlock(BlockState state) {
        BlockDisplayUtil.setBlock(blockDisplay, state);
    }

    @Override
    public void updateGlow(boolean glowing) {
        blockDisplay.setGlowingTag(glowing);
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        DisplayEntityUtils.setGlowColor(blockDisplay, color);
    }

    @Override
    public void updateLightLevel(int level) {
        DisplayEntityUtils.setBrightness(blockDisplay, level);
    }

    @Override
    public void remove() {
        blockDisplay.discard();
    }

    @Override
    public Set<UUID> getEntityUuids() {
        return Set.of(this.blockDisplay.getUUID());
    }

    @Override
    public boolean tracks(UUID uuid) {
        return this.blockDisplay.getUUID().equals(uuid);
    }
}
