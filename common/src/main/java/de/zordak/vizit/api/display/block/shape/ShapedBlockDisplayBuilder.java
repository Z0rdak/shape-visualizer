package de.zordak.vizit.api.display.block.shape;

import de.zordak.vizit.Constants;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import de.zordak.vizit.api.display.properties.BlockDisplayProperties;
import de.zordak.vizit.api.display.properties.DisplayEntityUtils;
import de.zordak.vizit.api.display.properties.DisplayProperties;
import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.ShapeStyle;
import de.zordak.vizit.shape.ShapeType;
import de.zordak.vizit.shape.strategy.DisplayStrategy;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ShapedBlockDisplayBuilder {

    private final Shape shape;
    private final String name;
    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private CompoundTag customData = new CompoundTag();
    private String tagKey = Constants.TAG_DEFAULT_CUSTOM_DATA;
    private ShapeStyle initialStyle = ShapeStyle.FRAME;
    private DisplayProperties displayProperties;

    public ShapedBlockDisplayBuilder(@NotNull Shape shape, @NotNull String name) {
        this.shape = shape;
        this.name = name;
    }

    public ShapedBlockDisplayBuilder(@NotNull Shape shape, @NotNull String name, @NotNull DisplayProperties displayProperties) {
        this.shape = shape;
        this.name = name;
        this.displayProperties = displayProperties;
    }

    public ShapedBlockDisplayBuilder withBlock(Block block) {
        this.blockState = block.defaultBlockState();
        return this;
    }

    public ShapedBlockDisplayBuilder withState(BlockState state) {
        this.blockState = state;
        return this;
    }

    public ShapedBlockDisplayBuilder customData(String tagKey, CompoundTag tag) {
        this.tagKey = tagKey;
        this.customData = tag;
        return this;
    }

    public ShapedBlockDisplayBuilder customData(CompoundTag tag) {
        this.tagKey = Constants.TAG_DEFAULT_CUSTOM_DATA;
        this.customData = tag;
        return this;
    }

    public ShapedBlockDisplayBuilder initialStyle(ShapeStyle style) {
        this.initialStyle = style;
        return this;
    }

    public ShapedBlockDisplayBuilder withDisplayProperties(DisplayProperties displayProperties) {
        this.displayProperties = displayProperties;
        return this;
    }

    public ShapedBlockDisplay build(ServerLevel level) {
        ShapeType<?> shapeType = shape.getType();
        DisplayStrategy strategy = shapeType.strategyFor(initialStyle);
        if (strategy == null) {
            throw new IllegalStateException("ShapeStyle " + initialStyle + " not supported for shape type: " + shapeType.id());
        }
        Set<Vec3> relativePositions = strategy.getBlockPositions(shape);

        // TODO: default display properties defined in DisplayProperties
        // TODO: Position and Rotation are not really used for shaped displays..
        // TODO: I guess the anchor position could be saved this way and the rotation of the shape around the anchor
        DisplayProperties usedDisplayProperties = displayProperties != null ? displayProperties : new DisplayProperties(
            shape.origin(), // pos
            0.0f, // xRot
            0.0f, // yRot
            Display.BillboardConstraints.FIXED, // billboard
            15, // brightness
            1.0f, // viewRange
            true,
            ChatFormatting.WHITE
        );
        BlockDisplayProperties properties = new BlockDisplayProperties(
            blockState, tagKey, customData
        );

        // TODO:
        Set<Display.BlockDisplay> entities = relativePositions.stream()
            .map(offset -> BlockDisplayUtil.createBlockDisplay(usedDisplayProperties, properties, level))
            .collect(Collectors.toSet());
        var uuid = UUID.randomUUID();
        return new ShapedBlockDisplay(uuid, name, shape, initialStyle, entities, properties);
    }
}
