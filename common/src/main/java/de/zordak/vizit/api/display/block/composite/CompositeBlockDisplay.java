package de.zordak.vizit.api.display.block.composite;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.vizit.api.display.block.BlockDisplayHandle;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import de.zordak.vizit.api.display.properties.DisplayEntityUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CompositeBlockDisplay implements BlockDisplayHandle {
    private final Set<Display.BlockDisplay> blockDisplays;
    private Set<UUID> blockDisplaysUuids;
    private final BlockDisplayProperties properties;
    private final UUID id;
    private final String name;
    private boolean isVisible;

    public static final Codec<CompositeBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.STRING_CODEC.fieldOf("id")
                    .forGetter(CompositeBlockDisplay::getId),
            Codec.STRING.fieldOf("name")
                    .forGetter(CompositeBlockDisplay::getName),
        UUIDUtil.CODEC_SET.fieldOf("displayUuids")
                .forGetter(c -> new HashSet<>(c.blockDisplaysUuids)),
        BlockDisplayProperties.CODEC.fieldOf("properties")
                .forGetter(c -> c.properties)
    ).apply(instance, (id, name, uuids, properties) -> {
        CompositeBlockDisplay display = new CompositeBlockDisplay(id, name, new HashSet<>(), properties);
        display.blockDisplaysUuids = new HashSet<>(uuids);
        return display;
    }));

    public CompositeBlockDisplay(UUID uuid, String name, Set<Display.BlockDisplay> blockDisplays, BlockDisplayProperties properties) {
        this.id = uuid;
        this.name = name;
        this.blockDisplays = blockDisplays;
        this.blockDisplaysUuids = new HashSet<>();
        for (Display.BlockDisplay entity : blockDisplays) {
            this.blockDisplaysUuids.add(entity.getUUID());
        }
        this.properties = properties;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setEntities(Set<Display.BlockDisplay> blockDisplays) {
        this.blockDisplays.clear();
        this.blockDisplays.addAll(blockDisplays);
        this.blockDisplaysUuids.clear();
        for (Display.BlockDisplay entity : blockDisplays) {
            this.blockDisplaysUuids.add(entity.getUUID());
        }
    }

    @Override
    public boolean isVisible() {
        return !this.blockDisplays.isEmpty() && this.isVisible;
    }

    public Set<Display.BlockDisplay> getEntities() {
        return this.blockDisplays;
    }

    @Override
    public void updateBlock(BlockState state) {
        for (var display : this.blockDisplays) {
            BlockDisplayUtil.setBlock(display, state);
        }
    }

    @Override
    public void updateGlow(boolean glowing) {
        for (var display : this.blockDisplays) {
            display.setGlowingTag(glowing);
        }
    }

    @Override
    public Set<UUID> getEntityUuids() {
        return this.blockDisplaysUuids;
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        for (var display : this.blockDisplays) {
            DisplayEntityUtils.setGlowColor(display, color);
        }
    }

    @Override
    public void updateLightLevel(int level) {
        for (var display : this.blockDisplays) {
            DisplayEntityUtils.setBrightness(display, level);

        }
    }

    @Override
    public void remove() {
        this.blockDisplays.forEach(Display.BlockDisplay::discard);
    }

    @Override
    public boolean tracks(UUID uuid) {
        return this.blockDisplaysUuids.contains(uuid);
    }
}
