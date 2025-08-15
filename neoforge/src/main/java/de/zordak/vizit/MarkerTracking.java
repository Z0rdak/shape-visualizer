package de.zordak.vizit;

import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

public class MarkerTracking {

    @SubscribeEvent
    public static void onStartEntityTracking(PlayerEvent.StartTracking event){
        if (!event.getEntity().level().isClientSide()) {
            // Check if entity is display entity from ShapeViz
            // check if player should see it based on tracking settings
            Entity trackedDisplay = event.getTarget();
            if (!(trackedDisplay instanceof Display.BlockDisplay blockDisplay)) {
                 return; // Not a display entity, skip
            }

            // Note: Deliberately not using neoforge attachments here - maybe this will be a multi-platform mod in the future
            EntityDataAccessor entityDataAccessor = new EntityDataAccessor(trackedDisplay);
            // check id or whatever inside



            var player = (ServerPlayer) event.getEntity();
            var isVisibleToPlayer = true; // Replace with actual visibility logic
            if (!isVisibleToPlayer) {
                blockDisplay.stopSeenByPlayer(player);
            }
        }
    }

    @SubscribeEvent
    public static void onStopEntityTracking(PlayerEvent.StopTracking event){
        // Check if entity is display entity from ShapeViz
        // check if player should stop seeing it based on tracking settings
    }
}
