package de.zordak.shapeviz.manager;


import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Marker;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class MarkerUtil {
    public static Entity createMarker(Level level, BlockPos pos, UUID shapeId) {
        Entity marker = new Marker(EntityType.MARKER, level) {};
        marker.moveTo(pos, 0, 0);
        marker.setCustomName(Text.literal("blockviz:" + shapeId));
        marker.addCommandTag("blockviz_anchor");
        marker.addCommandTag("shape_id:" + shapeId);
        return marker;
    }
}