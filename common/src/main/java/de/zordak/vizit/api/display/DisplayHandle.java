package de.zordak.vizit.api.display;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Display;

import java.util.Set;
import java.util.UUID;

public interface DisplayHandle {
    UUID getId();
    String getName();
    Set<Display.BlockDisplay> getEntities();
    Set<UUID> getEntityUuids();
    void updateGlow(boolean glowing);
    void updateGlowColor(ChatFormatting color);
    void updateLightLevel(int level);
    void remove();
    boolean isVisible();
    boolean tracks(UUID uuid);
}