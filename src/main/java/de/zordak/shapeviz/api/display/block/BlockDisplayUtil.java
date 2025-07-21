package de.zordak.shapeviz.api.display.block;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.shapeviz.ShapeVisualizer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BlockDisplayUtil {

    public static final String DEFAULT_CUSTOM_DATA_KEY = ShapeVisualizer.MOD_ID+":data";

    private BlockDisplayUtil() {}

    public static void setCustomData(Display.BlockDisplay entity, String tagKey, CompoundTag dataTag) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag entityTag = entityDataAccessor.getData();
        entityTag.put(tagKey, dataTag);
        entityDataAccessor.setData(entityTag);
    }

    public static void updatePos(Display.BlockDisplay entity, BlockPos pos) throws CommandSyntaxException {
        entity.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
    }

    public static void updateBlock(Display.BlockDisplay entity, Block block) throws CommandSyntaxException {
        updateBlock(entity, block.defaultBlockState());
    }

    public static void updateBlock(Display.BlockDisplay entity, ResourceLocation blockRl) throws CommandSyntaxException {
        Block block = BuiltInRegistries.BLOCK.get(blockRl);
        if (block.defaultBlockState().isAir()) {
            throw new IllegalArgumentException("Block " + blockRl.toString() + " wasn't found!");
        }
        updateBlock(entity, block);
    }

    public static void updateBlock(Display.BlockDisplay entity, BlockState blockState) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag entityTag = entityDataAccessor.getData();
        CompoundTag stateTag = NbtUtils.writeBlockState(blockState);
        entityTag.put(Display.BlockDisplay.TAG_BLOCK_STATE, stateTag);
        entityDataAccessor.setData(entityTag);
    }

    public static void updateGlow(Display.BlockDisplay entity, boolean glow) {
        entity.setGlowingTag(glow);
    }

    public static void updateGlowColor(Display.BlockDisplay entity, ChatFormatting glowColor) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag entityTag = entityDataAccessor.getData();
        int color = glowColor.getColor() != null ? glowColor.getColor() : 16777215;
        entityTag.putInt("glow_color_override", color);
        entityDataAccessor.setData(entityTag);
    }

    public static void updateLightLevel(Display.BlockDisplay entity, int lightLevel) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag entityTag = entityDataAccessor.getData();
        var brightnessTag = new CompoundTag();
        brightnessTag.putInt("sky", lightLevel);
        brightnessTag.putInt("block", lightLevel);
        entityTag.put("brightness", brightnessTag);
        entityDataAccessor.setData(entityTag);
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

    public static Display.BlockDisplay createBlockDisplay(BlockDisplayProperties properties, BlockPos pos, ServerLevel level) {
        Display.BlockDisplay displayEntity = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, level);
        try {
            updateBlock(displayEntity, properties.blockState());
            updateGlow(displayEntity, properties.glowing());
            updateGlowColor(displayEntity, properties.glowColor());
            updateLightLevel(displayEntity, properties.lightLevel());
            setCustomData(displayEntity, properties.tagKey(), properties.customData());
            updatePos(displayEntity, pos);
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
        return displayEntity;
    }
}
