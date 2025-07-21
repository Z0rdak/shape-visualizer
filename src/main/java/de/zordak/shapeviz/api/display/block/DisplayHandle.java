package de.zordak.shapeviz.api.display.block;

import net.minecraft.ChatFormatting;

import java.util.Set;
import java.util.UUID;

public interface DisplayHandle {
    UUID getId();
    Set<BlockDisplay> getEntities();
    void updateGlow(boolean glowing);
    void updateGlowColor(ChatFormatting color);
    void updateLightLevel(int level);
    void remove();
}