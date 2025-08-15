package de.zordak.vizit.api.display.block.shape;

import de.zordak.vizit.api.display.block.BlockDisplayHandle;
import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.ShapeStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.UUID;

public interface ShapedDisplayHandle extends BlockDisplayHandle {
    Set<Display.BlockDisplay> getEntities(ShapeStyle style);
    Set<UUID> getEntityUuids(ShapeStyle style);
    Shape getShape();
    void updateBlock(ShapeStyle style, BlockState state);
    void updateGlow(ShapeStyle style, boolean glowing);
    void updateGlowColor(ShapeStyle style, ChatFormatting color);
    void updateLightLevel(ShapeStyle style, int level);
    void remove(ShapeStyle style);
    void enable(ShapeStyle style);
    boolean isVisible(ShapeStyle style);
}