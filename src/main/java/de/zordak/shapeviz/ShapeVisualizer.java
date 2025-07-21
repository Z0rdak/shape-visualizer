package de.zordak.shapeviz;

import de.zordak.shapeviz.api.display.block.ShapedBlockDisplay;
import de.zordak.shapeviz.api.display.block.ShapedBlockDisplayBuilder;
import de.zordak.shapeviz.api.display.block.SingleBlockDisplayBuilder;
import de.zordak.shapeviz.api.display.block.SingleBlockDisplayHandle;
import de.zordak.shapeviz.shape.CuboidShape;
import de.zordak.shapeviz.shape.ShapeStyle;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import org.slf4j.Logger;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.LoggerFactory;

import static de.zordak.shapeviz.ShapeVisualizer.MOD_ID;

@Mod(MOD_ID)
public class ShapeVisualizer {
    public static final String MOD_ID = "shapeviz";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public ShapeVisualizer(IEventBus modEventBus) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.register(this);

        LOGGER.info("ShapeVisualization mod initializing...");

    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("ShapeVisualization mod setup complete");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

        MinecraftServer server = event.getServer();
        ServerLevel level = server.overworld();

        var origin = new BlockPos(0, 0, 0);
        var a = new BlockPos(0, 0, 0);
        var b = new BlockPos(6, 6, 6);
        CuboidShape shape = new CuboidShape(a, b);

        var cTag = new CompoundTag();
        SingleBlockDisplayHandle singleBlockHandle = new SingleBlockDisplayBuilder(origin)
                .withBlock(Blocks.CYAN_STAINED_GLASS)
                .glowColor(ChatFormatting.RED)
                .lightLevel(15)
                .customData("yawp:data", cTag)
                .build(level);




        ShapedBlockDisplay handle = new ShapedBlockDisplayBuilder(shape)
                .withBlock(Blocks.RED_STAINED_GLASS)
                .glowing(true)
                .glowColor(ChatFormatting.AQUA)
                .lightLevel(12)
                .initialStyle(ShapeStyle.FRAME)
                .build(level);

        LOGGER.info("ShapeVisualization managers initialized");
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {


        LOGGER.info("ShapeVisualization managers cleaned up");
    }

}
