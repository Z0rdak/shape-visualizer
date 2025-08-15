package de.zordak.vizit.api;

import de.zordak.vizit.api.display.DisplayHandle;
import de.zordak.vizit.api.display.block.shape.ShapedDisplayHandle;
import de.zordak.vizit.shape.ShapeStyle;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.level.Level;

import java.util.*;

/**
 * Manages spawned display entities
 */
public final class BlockDisplayManager {
    private static final Map<ResourceKey<Level>, LevelDisplayManager> levelManagers = new HashMap<>();

    private BlockDisplayManager() {}

    public static LevelDisplayManager get(ServerLevel level) {
        return levelManagers.computeIfAbsent(level.dimension(), dim -> new LevelDisplayManager(level));
    }

    public static Optional<DisplayHandle> getDisplayHandle(ServerLevel level, Display.BlockDisplay entity) {
        return get(level).getByEntity(entity);
    }

    public static Optional<DisplayHandle> getDisplayHandle(ServerLevel level, String name) {
        return get(level).getByName(name);
    }

    public static Optional<DisplayHandle> getDisplayHandle(ServerLevel level, UUID uuid) {
        return get(level).get(uuid);
    }

    public static void track(ServerLevel level, DisplayHandle handle) {
        get(level).track(handle);
    }

    public static void untrack(ServerLevel level, DisplayHandle handle) {
        get(level).untrack(handle);
    }

    public static void untrack(ServerLevel level, String handleName) {
        get(level).untrack(handleName);
    }


    public static void track(ServerLevel level, DisplayHandle handle, boolean display) {
        track(level, handle);
        if (display) display(level, handle);
    }

    public static void display(ServerLevel level, DisplayHandle handle) {
        get(level).display(handle);
    }

    public static void display(ServerLevel level, ShapedDisplayHandle handle, ShapeStyle style) {
        get(level).display(handle, style);
    }

    public static void hide(ServerLevel level, DisplayHandle handle, boolean untrack) {
        get(level).hide(handle, untrack);

    }

    public static Collection<DisplayHandle> getAll(ServerLevel level) {
        return get(level).all();
    }
}
