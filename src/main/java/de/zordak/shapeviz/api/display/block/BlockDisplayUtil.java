package de.zordak.shapeviz.api.display.block;


import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class BlockDisplayUtil {

    private BlockDisplayUtil() {}

    public static void updateBlock(BlockDisplay blockDisplay, Block block) throws CommandSyntaxException {
        updateBlock(blockDisplay, block.defaultBlockState());
    }

    public static void updateBlock(BlockDisplay blockDisplay, ResourceLocation blockRl) throws CommandSyntaxException {
        Block block = BuiltInRegistries.BLOCK.get(blockRl);
        if (block.defaultBlockState().isAir()) {
            throw new IllegalArgumentException("Block " + blockRl.toString() + " wasn't found!");
        }
        updateBlock(blockDisplay, block);
    }

    public static void updateBlock(BlockDisplay blockDisplay, BlockState blockState) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(blockDisplay.blockDisplay());
        CompoundTag entityTag = entityDataAccessor.getData();
        CompoundTag stateTag = NbtUtils.writeBlockState(blockState);
        entityTag.put(Display.BlockDisplay.TAG_BLOCK_STATE, stateTag);
        entityDataAccessor.setData(entityTag);
    }

    public static void updateGlow(BlockDisplay blockDisplayEntity, boolean glow) {
        blockDisplayEntity.blockDisplay().setGlowingTag(glow);
    }

    public static void updateGlowColor(BlockDisplay blockDisplay, ChatFormatting glowColor) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(blockDisplay.blockDisplay());
        CompoundTag entityTag = entityDataAccessor.getData();
        int color = glowColor.getColor() != null ? glowColor.getColor() : 16777215;
        entityTag.putInt("glow_color_override", color);
        entityDataAccessor.setData(entityTag);
    }

    public static void updateLightLevel(BlockDisplay blockDisplay, int lightLevel) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(blockDisplay.blockDisplay());
        CompoundTag entityTag = entityDataAccessor.getData();
        var brightnessTag = new CompoundTag();
        brightnessTag.putInt("sky", lightLevel);
        brightnessTag.putInt("block", lightLevel);
        entityTag.put("brightness", brightnessTag);
        entityDataAccessor.setData(entityTag);
    }

    public static void setCustomData(BlockDisplay blockDisplay, String tagKey, CompoundTag dataTag) throws CommandSyntaxException {
        var entityDataAccessor = new EntityDataAccessor(blockDisplay.blockDisplay());
        CompoundTag entityTag = entityDataAccessor.getData();
        entityTag.put(tagKey, dataTag);
        entityDataAccessor.setData(entityTag);
    }

    public static void updatePos(BlockDisplay blockDisplay, BlockPos pos) throws CommandSyntaxException {
        blockDisplay.blockDisplay().moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
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
}
