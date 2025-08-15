package de.zordak.vizit.api.display.text;

import de.zordak.vizit.api.display.DisplayHandle;
import de.zordak.vizit.api.display.properties.TextDisplayProperties;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Display;

import java.util.Set;
import java.util.UUID;

public class TextDisplayHandle implements DisplayHandle {
    private final TextDisplayProperties properties;
    private final String name;
    private final UUID id;

    public TextDisplayHandle(String text, TextDisplayProperties properties, String name, UUID id) {
        this.properties = properties;
        this.name = name;
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<Display.BlockDisplay> getEntities() {
        return Set.of();
    }

    @Override
    public Set<UUID> getEntityUuids() {
        return Set.of();
    }

    public String getText() {
        return this.properties.text();
    }

    @Override
    public void updateGlow(boolean glowing) {
        // No implementation needed for text display
    }

    @Override
    public void updateGlowColor(ChatFormatting color) {
        // No implementation needed for text display
    }

    @Override
    public void updateLightLevel(int level) {
        // No implementation needed for text display
    }

    @Override
    public void remove() {
        // Logic to remove the text display
    }

    @Override
    public boolean isVisible() {
        return true; // Text displays are always visible by default
    }

    @Override
    public boolean tracks(UUID uuid) {
        return false;
    }
}
