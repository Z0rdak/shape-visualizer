package de.zordak.vizit.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.zordak.vizit.Constants;
import de.zordak.vizit.api.BlockDisplayManager;
import de.zordak.vizit.api.display.DisplayHandle;
import de.zordak.vizit.api.display.block.BlockDisplayUtil;
import de.zordak.vizit.api.display.block.shape.ShapedBlockDisplay;
import de.zordak.vizit.api.display.block.shape.ShapedBlockDisplayBuilder;
import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.ShapeStyle;
import de.zordak.vizit.shape.SphereShape;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import java.util.Optional;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;


public final class CommandRegistry {


    private CommandRegistry() {
    }
    
    public static void register(RegisterCommandsEvent event) {
        if (event.getCommandSelection() == Commands.CommandSelection.DEDICATED
                || event.getCommandSelection() == Commands.CommandSelection.INTEGRATED) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            CommandBuildContext buildContext = event.getBuildContext();

            dispatcher.register(CommandBuilder.build(buildContext));


        }
    }
}
