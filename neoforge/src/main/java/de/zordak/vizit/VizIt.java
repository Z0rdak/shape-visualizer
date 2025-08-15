package de.zordak.vizit;

import de.zordak.vizit.commands.CommandBuilder;
import de.zordak.vizit.commands.CommandRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;

import static de.zordak.vizit.Constants.*;
import static de.zordak.vizit.Constants.MOD_ID;

@Mod(MOD_ID)
public class VizIt {

    public VizIt(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.addListener(CommandRegistry::register);

        LOGGER.info("Registering commands...");
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("ShapeVisualization mod setup complete");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        LOGGER.info("ShapeVisualization managers initialized");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {


        LOGGER.info("ShapeVisualization managers cleaned up");
    }

}
