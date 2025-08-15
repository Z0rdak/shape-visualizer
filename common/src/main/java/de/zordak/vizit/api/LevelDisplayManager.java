package de.zordak.vizit.api;
import de.zordak.vizit.api.display.DisplayHandle;
import de.zordak.vizit.api.display.block.shape.ShapedDisplayHandle;
import de.zordak.vizit.shape.ShapeStyle;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;

import java.util.*;
import java.util.stream.Collectors;

public class LevelDisplayManager {
    private final ServerLevel level;
    private final Map<UUID, DisplayHandle> handles = new HashMap<>();

    public LevelDisplayManager(ServerLevel level) {
        this.level = level;
    }

    public Optional<DisplayHandle> get(UUID id) {
        return Optional.ofNullable(handles.get(id));
    }

    public Optional<DisplayHandle> getByName(String name) {
        return handles.values().stream()
                .filter(handle -> handle.getName().equals(name))
                .limit(1)
                .findFirst();
    }

    public Optional<DisplayHandle> getByEntity(Display.BlockDisplay entity) {
        return handles.values().stream()
                .filter(h -> h.tracks(entity.getUUID()))
                .limit(1)
                .findFirst();
    }

    public void untrack(DisplayHandle handle) {
        handles.remove(handle.getId(), handle);
    }


    public void untrack(String handleName) {
        handles.values().removeIf(handle -> handle.getName().equals(handleName));
    }

    public void untrack(UUID id) {
        handles.remove(id);
    }

    public void track(DisplayHandle handle) {
        handles.putIfAbsent(handle.getId(), handle);
    }

    public void display(DisplayHandle handle) {
        handle.getEntities().forEach(level::addFreshEntity);
        //handle.setVisible(true);
    }

    public void display(ShapedDisplayHandle handle, ShapeStyle style) {
        handle.getEntities(style).forEach(level::addWithUUID);
    }

    public void hide(DisplayHandle handle, boolean untrack) {
        handle.remove();
        if (untrack)
            handles.remove(handle.getId());
    }

    public void hide(DisplayHandle handle) {
        hide(handle, false);
    }

    public Set<DisplayHandle> all() {
        return new HashSet<>(handles.values());
    }

    public Set<DisplayHandle> visible() {
        return handles.values().stream()
                .filter(DisplayHandle::isVisible)
                .collect(Collectors.toSet());
    }

    public Set<String> displayNames() {
        return handles.values().stream().map(DisplayHandle::getName).collect(Collectors.toSet());
    }
}
