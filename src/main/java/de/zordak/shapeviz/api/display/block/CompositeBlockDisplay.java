package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.core.UUIDUtil;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class CompositeBlockDisplay implements BlockDisplayHandle {
    private final Set<Display.BlockDisplay> blockDisplays;
    private Set<UUID> blockDisplaysUuids;
    private final BlockDisplayProperties properties;
    private final UUID id;

    public static final Codec<CompositeBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("id")
                    .forGetter(CompositeBlockDisplay::getId),
        UUIDUtil.CODEC_SET.fieldOf("displayUuids")
                .forGetter(c -> new HashSet<>(c.blockDisplaysUuids)),
        BlockDisplayProperties.CODEC.fieldOf("properties")
                .forGetter(c -> c.properties)
    ).apply(instance, (id, uuids, properties) -> {
        CompositeBlockDisplay display = new CompositeBlockDisplay(id, new HashSet<>(), properties);
        display.blockDisplaysUuids = new HashSet<>(uuids);
        return display;
    }));

    public CompositeBlockDisplay(UUID uuid, Set<Display.BlockDisplay> blockDisplays, BlockDisplayProperties properties) {
        this.id = uuid;
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


    public void setEntities(Set<Display.BlockDisplay> blockDisplays) {
        this.blockDisplays.clear();
        this.blockDisplays.addAll(blockDisplays);
        this.blockDisplaysUuids.clear();
        for (Display.BlockDisplay entity : blockDisplays) {
            this.blockDisplaysUuids.add(entity.getUUID());
        }
    }
    public Set<Display.BlockDisplay> getEntities() {
        return this.blockDisplays;
    }

    @Override
    public void updateBlock(BlockState state) {
        for (var display : this.blockDisplays) {
            try {
                BlockDisplayUtil.updateBlock(display, state);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateGlow(boolean glowing) {
        for (var display : this.blockDisplays) {
            display.setGlowingTag(glowing);
        }
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        for (var display : this.blockDisplays) {
            try {
                BlockDisplayUtil.updateGlowColor(display, color);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void updateLightLevel(int level) {
        for (var display : this.blockDisplays) {
            try {
                BlockDisplayUtil.updateLightLevel(display, level);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void remove() {
        for (var display : this.blockDisplays) {
            display.discard();
        }
    }

}
