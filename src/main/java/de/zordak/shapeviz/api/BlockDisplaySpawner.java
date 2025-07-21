package de.zordak.shapeviz.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Handles spawning display entities based on a shape's computed positions.
 */
public class BlockDisplaySpawner {
    private static final String TAG_PREFIX = "blockviz";

    /**
     *
     * @param regionName a marker, stored in custom entity data for identification later
     */
    public static Optional<Entity> createBlockDisplayEntity(ServerLevel level, String regionName, BlockPos pos, BlockDisplayProperties displayProperties) {
        Entity blockDisplay = EntityType.BLOCK_DISPLAY.create(level, (e) -> {
            .initBlockDisplayProperties(e, regionName, displayProperties);
            e.moveOrInterpolateTo(new Vec3(pos), 0, 0);
        }, pos, EntitySpawnReason.COMMAND, false, false);
        return blockDisplay == null ? Optional.empty() : Optional.of(blockDisplay);
    }


    public static void spawnDisplayBlocks(ServerLevel world, ShapeMetadata meta, Set<BlockPos> positions) {
        for (BlockPos pos : positions) {
            Display.BlockDisplay display = new Display.BlockDisplay(EntityType.BLOCK_DISPLAY, world);
            //display.setBlockState(Blocks.LIGHT.getDefaultState());
            //display.setPosition(Vec3d.ofCenter(pos));
            //display.addCommandTag(TAG_PREFIX);
            //display.addCommandTag("shape_id:" + meta.shapeId());
            //world.spawnEntity(display);
        }
    }

    public static void removeOldDisplayBlocks(ServerLevel world, UUID shapeId) {

    }
}
