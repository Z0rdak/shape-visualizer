package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.shape.Shape;
import de.zordak.shapeviz.shape.ShapeStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class ShapedBlockDisplay implements ShapedDisplayHandle {
    private final Map<ShapeStyle, Set<Display.BlockDisplay>> activeEntities;
    private final Shape shape;
    private final BlockDisplayProperties properties;
    private final UUID id;

    public ShapedBlockDisplay(UUID id, Shape shape, BlockDisplayProperties properties) {
        this.shape = shape;
        this.properties = properties;
        this.activeEntities = new EnumMap<>(ShapeStyle.class);
        this.id = id;
    }

    protected ShapedBlockDisplay(UUID id, Shape shape, ShapeStyle style, Set<Display.BlockDisplay> entities, BlockDisplayProperties properties) {
        this(id, shape, properties);
        this.activeEntities.put(style, entities);
    }

    public BlockDisplayProperties getProperties() {
        return properties;
    }

    @Override
    public UUID getId() {
        return null;
    }

    @Override
    public Set<Display.BlockDisplay> getEntities() {
        return Set.of();
    }

    @Override
    public void updateGlow(boolean glowing) {
        this.activeEntities.keySet().forEach(style -> this.updateGlow(style, glowing));
    }

    @Override
    public void updateGlowColor(ShapeStyle style, ChatFormatting color) {
        var displays = this.getEntities(style);
        displays.forEach(display -> {
            try {
                BlockDisplayUtil.updateGlowColor(display, color);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        this.activeEntities.keySet().forEach(style -> this.updateGlowColor(style, color));
    }

    @Override
    public void updateLightLevel(int level) {
        this.activeEntities.keySet().forEach(style -> this.updateLightLevel(style, level));
    }

    @Override
    public void remove() {
        this.activeEntities.forEach((style, entities) -> {
            entities.forEach(Display.BlockDisplay::discard);
        });
        this.activeEntities.clear();
    }

    public Set<Display.BlockDisplay> getEntities(ShapeStyle style) {
        return this.activeEntities.get(style);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void updateBlock(ShapeStyle style, BlockState state) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> {
            try {
                BlockDisplayUtil.updateBlock(display, state);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateGlow(ShapeStyle style, boolean glowing) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> BlockDisplayUtil.updateGlow(display, glowing));
    }

    @Override
    public void updateLightLevel(ShapeStyle style, int level) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> {
            try {
                BlockDisplayUtil.updateLightLevel(display, level);
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }
        });

    }

    @Override
    public void remove(ShapeStyle style) {
        this.getEntities(style).forEach(Display.BlockDisplay::discard);
        this.activeEntities.get(style).clear();
        this.activeEntities.remove(style);
    }

    @Override
    public void enable(ShapeStyle style) {
        if (this.activeEntities.containsKey(style)) return;


    }

    @Override
    public boolean isVisible(ShapeStyle style) {
        return this.activeEntities.containsKey(style);
    }

    @Override
    public void updateBlock(BlockState state) {
        this.activeEntities.keySet().forEach(style -> this.updateBlock(style, state));
    }
}
