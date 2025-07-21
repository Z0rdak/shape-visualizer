package de.zordak.shapeviz.api.display.block;

import de.zordak.shapeviz.shape.ShapeStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;

import java.util.Set;
import java.util.UUID;

public interface DisplayHandle {
    UUID getId();
    Set<Display.BlockDisplay> getEntities();
    void updateGlow(boolean glowing);
    void updateGlowColor(ChatFormatting color);
    void updateLightLevel(int level);
    void remove();

    static void spawn(ServerLevel level, DisplayHandle handle) {
        handle.getEntities().forEach(level::addFreshEntity);
    }

    static void spawnShape(ServerLevel level, ShapedDisplayHandle handle, ShapeStyle style) {
        handle.getEntities(style).forEach(level::addFreshEntity);
    }
}