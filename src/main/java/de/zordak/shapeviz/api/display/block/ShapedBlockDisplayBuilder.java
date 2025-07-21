package de.zordak.shapeviz.api.display.block;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.ShapeVisualizer;
import de.zordak.shapeviz.shape.Shape;
import de.zordak.shapeviz.shape.ShapeStyle;
import de.zordak.shapeviz.shape.ShapeType;
import de.zordak.shapeviz.shape.strategy.DisplayStrategy;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static de.zordak.shapeviz.api.display.block.BlockDisplayUtil.DEFAULT_CUSTOM_DATA_KEY;

public final class ShapedBlockDisplayBuilder {

    private final Shape shape;
    private BlockState blockState = BlockDisplayUtil.randomFromDefault();
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;
    private int lightLevel = 15;
    private CompoundTag customData = new CompoundTag();
    private String tagKey = DEFAULT_CUSTOM_DATA_KEY;
    private ShapeStyle initialStyle = ShapeStyle.FRAME;

    public ShapedBlockDisplayBuilder(Shape shape) {
        this.shape = shape;
    }

    public ShapedBlockDisplayBuilder withBlock(Block block) {
        this.blockState = block.defaultBlockState();
        return this;
    }

    public ShapedBlockDisplayBuilder withState(BlockState state) {
        this.blockState = state;
        return this;
    }

    public ShapedBlockDisplayBuilder glowColor(ChatFormatting color) {
        this.glowColor = (color.getColor() == null) ? ChatFormatting.WHITE : color;
        return this;
    }

    public ShapedBlockDisplayBuilder glowing(boolean glow) {
        this.glowing = glow;
        return this;
    }

    public ShapedBlockDisplayBuilder lightLevel(int level) {
        this.lightLevel = level;
        return this;
    }

    public ShapedBlockDisplayBuilder customData(String tagKey, CompoundTag tag) {
        this.tagKey = tagKey;
        this.customData = tag;
        return this;
    }

    public ShapedBlockDisplayBuilder initialStyle(ShapeStyle style) {
        this.initialStyle = style;
        return this;
    }

    public ShapedBlockDisplay build(ServerLevel level) {
        ShapeType<?> shapeType = shape.getType();
        DisplayStrategy strategy = shapeType.strategyFor(initialStyle);
        if (strategy == null) {
            throw new IllegalStateException("ShapeStyle " + initialStyle + " not supported for shape type: " + shapeType.id());
        }
        Set<BlockPos> relativePositions = strategy.getBlockPositions(shape);

        BlockDisplayProperties properties = new BlockDisplayProperties(
            blockState, glowing, glowColor, lightLevel, tagKey, customData
        );

        Set<Display.BlockDisplay> entities = relativePositions.stream()
            .map(offset -> BlockDisplayUtil.createBlockDisplay(properties, shape.origin().offset(offset), level))
            .collect(Collectors.toSet());
        var uuid = UUID.randomUUID();
        return new ShapedBlockDisplay(uuid, shape, initialStyle, entities, properties);
    }
}
