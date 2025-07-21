package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.shape.Shape;
import de.zordak.shapeviz.shape.ShapeStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.stream.Collectors;

public class ShapedBlockDisplay implements ShapedDisplayHandle {

    private final Map<ShapeStyle, Set<BlockDisplay>> activeDisplays;
    private final Shape shape;
    private final UUID id;
    private final Marker marker;

    public ShapedBlockDisplay(Shape shape) {
        this.shape = shape;
        this.activeDisplays = new EnumMap<>(ShapeStyle.class);
    }

    protected ShapedBlockDisplay(Shape shape, ShapeStyle shapeStyle, Set<BlockDisplay> displays) {
        this(shape);
        this.activeDisplays.put(shapeStyle, displays);
    }

    @Override
    public Set<BlockDisplay> getEntities() {
        return this.activeDisplays.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public void updateGlow(boolean glowing) {
        this.activeDisplays.keySet().forEach(style -> this.updateGlow(style, glowing));
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
        this.activeDisplays.keySet().forEach(style -> this.updateGlowColor(style, color));
    }

    @Override
    public void updateLightLevel(int level) {
        this.activeDisplays.keySet().forEach(style -> this.updateLightLevel(style, level));
    }

    @Override
    public void remove() {
        this.activeDisplays.forEach((style, displays) -> {
            displays.forEach(display -> display.blockDisplay().discard());
        });
        this.activeDisplays.clear();
    }

    @Override
    public Set<BlockDisplay> getEntities(ShapeStyle style) {
        return this.activeDisplays.get(style);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public void updateBlock(ShapeStyle style, BlockState state) {
        Set<BlockDisplay> displays = this.getEntities(style);
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
        Set<BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> BlockDisplayUtil.updateGlow(display, glowing));
    }

    @Override
    public void updateLightLevel(ShapeStyle style, int level) {
        Set<BlockDisplay> displays = this.getEntities(style);
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
        this.getEntities(style).forEach(display -> display.blockDisplay().discard());
        this.activeDisplays.get(style).clear();
        this.activeDisplays.remove(style);
    }

    @Override
    public void enable(ShapeStyle style) {
        if (this.activeDisplays.containsKey(style)) return;


    }

    @Override
    public boolean isVisible(ShapeStyle style) {
        return this.activeDisplays.containsKey(style);
    }

    @Override
    public void updateBlock(BlockState state) {
        this.activeDisplays.keySet().forEach(style -> this.updateBlock(style, state));
    }
}
