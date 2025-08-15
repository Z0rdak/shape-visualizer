package de.zordak.vizit.api.display.block.shape;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import de.zordak.vizit.api.display.properties.DisplayEntityUtils;
import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.ShapeStyle;
import de.zordak.vizit.shape.ShapeTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static net.minecraft.core.UUIDUtil.STRING_CODEC;

public class ShapedBlockDisplay implements ShapedDisplayHandle {
    private final Map<ShapeStyle, Set<Display.BlockDisplay>> activeEntities;
    private final Map<ShapeStyle, Set<UUID>> managedEntityUuids;
    private final Shape shape;
    private final String name;
    private final BlockDisplayProperties properties;
    private final UUID id;
    private final Map<ShapeStyle, Boolean> visibility = new EnumMap<>(ShapeStyle.class);

    public static final Codec<Set<UUID>> STRING_CODEC_SET =
            Codec.list(STRING_CODEC).xmap(Sets::newLinkedHashSet, Lists::newArrayList);

    public static final Codec<ShapedBlockDisplay> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            STRING_CODEC.fieldOf("id")
                    .forGetter(ShapedBlockDisplay::getId),
            Codec.STRING.fieldOf("name")
                    .forGetter(ShapedBlockDisplay::getName),
            ShapeTypes.SHAPE_CODEC.fieldOf("shape")
                    .forGetter(ShapedBlockDisplay::getShape),
            Codec.unboundedMap(ShapeStyle.CODEC, STRING_CODEC_SET)
                    .fieldOf("displayUuids")
                    .forGetter(c -> c.managedEntityUuids),
            BlockDisplayProperties.CODEC.fieldOf("properties")
                    .forGetter(c -> c.properties)
    ).apply(instance, ShapedBlockDisplay::new));



    public ShapedBlockDisplay(UUID id, String name, Shape shape, BlockDisplayProperties properties) {
        this.shape = shape;
        this.name = name;
        this.properties = properties;
        this.activeEntities = new EnumMap<>(ShapeStyle.class);
        this.id = id;
        this.managedEntityUuids = new EnumMap<>(ShapeStyle.class);
    }

    protected ShapedBlockDisplay(UUID id, String name, Shape shape, Map<ShapeStyle, Set<UUID>> managedEntityUuids, BlockDisplayProperties properties) {
        this(id, name, shape, properties);
        managedEntityUuids.forEach((style, uuids) -> {
            this.managedEntityUuids.put(style, uuids);

           // TODO: Reinitialize entities -  save pos with ids?
        });
    }

    protected ShapedBlockDisplay(UUID id, String name, Shape shape, ShapeStyle style, Set<Display.BlockDisplay> entities, BlockDisplayProperties properties) {
        this(id, name, shape, properties);
        this.activeEntities.put(style, entities);
        var uuids = entities.stream()
                .map(Entity::getUUID)
                .collect(Collectors.toSet());
        this.managedEntityUuids.put(style, uuids);
    }

    public BlockDisplayProperties getProperties() {
        return properties;
    }

    @Override
    public UUID getId() {
        return this.id;
    }

    @Override
    public Set<Display.BlockDisplay> getEntities() {
        return this.activeEntities.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UUID> getEntityUuids() {
        return this.managedEntityUuids.values().stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<UUID> getEntityUuids(ShapeStyle style) {
        return this.managedEntityUuids.get(style);
    }

    @Override
    public void updateGlow(boolean glowing) {
        this.activeEntities.keySet().forEach(style -> this.updateGlow(style, glowing));
    }

    @Override
    public void updateGlowColor(ShapeStyle style, ChatFormatting color) {
        var displays = this.getEntities(style);
        displays.forEach(display -> {

            DisplayEntityUtils.setGlowColor(display, color);

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
    }

    public Set<Display.BlockDisplay> getEntities(ShapeStyle style) {
        return this.activeEntities.get(style);
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void updateBlock(ShapeStyle style, BlockState state) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> BlockDisplayUtil.setBlock(display, state));
    }

    @Override
    public void updateGlow(ShapeStyle style, boolean glowing) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> DisplayEntityUtils.updateGlow(display, glowing));
    }

    @Override
    public void updateLightLevel(ShapeStyle style, int level) {
        Set<Display.BlockDisplay> displays = this.getEntities(style);
        displays.forEach(display -> DisplayEntityUtils.setBrightness(display, level));

    }

    @Override
    public void remove(ShapeStyle style) {
        this.getEntities(style).forEach(Display.BlockDisplay::discard);
        this.activeEntities.get(style).clear();
        this.activeEntities.remove(style);
    }

    @Override
    public boolean tracks(UUID uuid) {
        return this.getEntityUuids().contains(uuid);
    }

    @Override
    public boolean isVisible() {
        return !this.activeEntities.isEmpty() && this.activeEntities.containsValue(true);
    }

    @Override
    public boolean isVisible(ShapeStyle style) {
        return !this.activeEntities.get(style).isEmpty() && this.visibility.get(style);
    }

    @Override
    public void enable(ShapeStyle style) {
        if (this.activeEntities.containsKey(style)) return;
    }

    @Override
    public void updateBlock(BlockState state) {
        this.activeEntities.keySet().forEach(style -> this.updateBlock(style, state));
    }
}
