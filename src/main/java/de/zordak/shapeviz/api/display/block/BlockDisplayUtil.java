package de.zordak.shapeviz.api.display.block;


import de.zordak.shapeviz.api.display.properties.BlockDisplayProperties;
import de.zordak.shapeviz.api.display.properties.DisplayProperties;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static de.zordak.shapeviz.api.display.properties.DisplayEntityUtils.*;

public final class BlockDisplayUtil {

    private BlockDisplayUtil() {}

    public static Display.BlockDisplay setBlock(Display.BlockDisplay entity, Block block) {
        return setBlock(entity, block.defaultBlockState());
    }

    public static Display.BlockDisplay setBlock(Display.BlockDisplay entity, ResourceLocation blockRl) {
        Block block = BuiltInRegistries.BLOCK.get(blockRl);
        if (block.defaultBlockState().isAir()) {
            throw new IllegalArgumentException("Block " + blockRl.toString() + " wasn't found!");
        }
        return setBlock(entity, block);
    }

    public static BlockState getBlock(Display.BlockDisplay entity) {
        CompoundTag tag = getEntityData(entity);
        CompoundTag stateTag = tag.getCompound(Display.BlockDisplay.TAG_BLOCK_STATE);
        return NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), stateTag);
    }

    public static Display.BlockDisplay setBlock(Display.BlockDisplay entity, BlockState blockState) {
        CompoundTag tag = new CompoundTag();
        CompoundTag stateTag = NbtUtils.writeBlockState(blockState);
        tag.put(Display.BlockDisplay.TAG_BLOCK_STATE, stateTag);
        return setEntityData(entity, tag);
    }

    public static final List<Block> DEFAULT_BLOCKS = new ArrayList<>();
    static {
        DEFAULT_BLOCKS.add(Blocks.CYAN_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.BLACK_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.YELLOW_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.WHITE_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.RED_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.BLUE_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.GREEN_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.ORANGE_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.PURPLE_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.PINK_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.LIGHT_BLUE_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.LIGHT_GRAY_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.LIME_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.GRAY_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.BROWN_STAINED_GLASS);
        DEFAULT_BLOCKS.add(Blocks.MAGENTA_STAINED_GLASS);
    }

    public static BlockState randomFromDefault() {
        int randomNum = new Random().nextInt(0, 16);
        return DEFAULT_BLOCKS.get(randomNum).defaultBlockState();
    }

    public static Display.BlockDisplay createBlockDisplay(DisplayProperties displayProperties,
                                                          BlockDisplayProperties properties,
                                                          ServerLevel level) {
        var displayEntity = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
        setDisplayProperties(displayEntity, displayProperties);
        setBlock(displayEntity, properties.blockState());
        setCustomData(displayEntity, properties.tagKey(), properties.customData());
        return displayEntity;
    }

}
