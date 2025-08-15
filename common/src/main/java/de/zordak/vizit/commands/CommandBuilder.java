package de.zordak.vizit.commands;


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
import de.zordak.vizit.api.display.block.SingleBlockDisplay;
import de.zordak.vizit.api.display.block.SingleBlockDisplayBuilder;
import de.zordak.vizit.api.display.block.shape.ShapedBlockDisplay;
import de.zordak.vizit.api.display.block.shape.ShapedBlockDisplayBuilder;
import de.zordak.vizit.api.display.text.TextDisplayUtil;
import de.zordak.vizit.shape.CuboidShape;
import de.zordak.vizit.shape.ShapeStyle;
import de.zordak.vizit.shape.SphereShape;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public final class CommandBuilder {


    private CommandBuilder() {
    }


    // --- Text command string constants ---
    private static final String TEXT = "text";
    private static final String INFO = "info";
    private static final String SET = "set";
    private static final String CREATE = "create";
    private static final String CUBOID = "cuboid";
    private static final String SPHERE = "sphere";
    private static final String NAME = "name";
    private static final String ALIGNMENT = "alignment";
    private static final Set<String> VALID_ALIGNMENTS = Set.of("center", "left", "right");
    private static final String BACKGROUND = "background";
    private static final String DEFAULT_BACKGROUND = "default_background";
    private static final String LINE_WIDTH = "line_width";
    private static final String SEE_THROUGH = "see_through";
    private static final String SHADOW = "shadow";
    private static final String TEXT_VALUE = "text";
    private static final String OPACITY = "opacity";
    private static final String VALUE = "value";
    public static String ARG_NAME_TEXT = "text";
    public static String POS = "pos";
    public static String ROT_X = "xRot";
    public static String ROT_Y = "yRot";
    public static String NAMED = "named";
    public static final String BLOCK = "block";
    public static final String BLOCK_STATE = "block_state";
    public static final String GLOW = "glow";
    public static final String GLOW_COLOR = "glow_color";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";
    private static final String BRIGHTNESS = "brightness";
    private static final String VIEW_RANGE = "view_range";
    private static final String BILLBOARD = "billboard";
    private static final String CUSTOM_NAME = "custom_name";
    private static final Set<String> VALID_BILLBOARDS = Set.of("fixed", "vertical", "horizontal", "center");

    public static LiteralArgumentBuilder<CommandSourceStack> build(CommandBuildContext buildContext) {
        return literal(Constants.MOD_ID)
                .executes(ctx -> promptHelp(ctx.getSource()))
                .then(literal("help").executes(ctx -> promptHelp(ctx.getSource())))

                .requires(CommandBuilder::hasCmdPermission)

                .then(textSubCmd())
                .then(blockSubCmd(buildContext))
                .then(shapeSubCmd())
                .then(showDisplayCmd())
                .then(hideDisplayCmd())
                .then(literal(CREATE)
                        .then(createCuboidShapeCmd())
                        .then(createSphereShapeCmd())
                        .then(createTextDisplayCmd())
                        .then(createBlockDisplayCmd())
                );
    }
    private static LiteralArgumentBuilder<CommandSourceStack> textSubCmd() {
        return literal(TEXT)
                .then(literal(INFO)
                        .then(argument(NAME, StringArgumentType.word())
                                .executes(ctx -> showTextInfo(ctx, StringArgumentType.getString(ctx, NAME)))
                        )
                )
                .then(buildCommonSetSubcommands())
                .then(literal(SET)
                        .then(argument(NAME, StringArgumentType.word())
                                .then(literal(ALIGNMENT)
                                        .then(argument(VALUE, StringArgumentType.word())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(VALID_ALIGNMENTS, builder))
                                                .executes(ctx -> setAlignment(ctx, StringArgumentType.getString(ctx, NAME), StringArgumentType.getString(ctx, VALUE)))
                                        )
                                )
                                .then(literal(BACKGROUND)
                                        .then(argument(VALUE, IntegerArgumentType.integer())
                                                .executes(ctx -> setBackground(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                        )
                                )
                                .then(literal(DEFAULT_BACKGROUND)
                                        .then(argument(VALUE, BoolArgumentType.bool())
                                                .executes(ctx -> setDefaultBackground(ctx, StringArgumentType.getString(ctx, NAME), BoolArgumentType.getBool(ctx, VALUE)))
                                        )
                                )
                                .then(literal(LINE_WIDTH)
                                        .then(argument(VALUE, IntegerArgumentType.integer())
                                                .executes(ctx -> setLineWidth(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                        )
                                )
                                .then(literal(SEE_THROUGH)
                                        .then(argument(VALUE, BoolArgumentType.bool())
                                                .executes(ctx -> setSeeThrough(ctx, StringArgumentType.getString(ctx, NAME), BoolArgumentType.getBool(ctx, VALUE)))
                                        )
                                )
                                .then(literal(SHADOW)
                                        .then(argument(VALUE, BoolArgumentType.bool())
                                                .executes(ctx -> setShadow(ctx, StringArgumentType.getString(ctx, NAME), BoolArgumentType.getBool(ctx, VALUE)))
                                        )
                                )
                                .then(literal(TEXT_VALUE)
                                        .then(argument(VALUE, StringArgumentType.string())
                                                .executes(ctx -> setText(ctx, StringArgumentType.getString(ctx, NAME), StringArgumentType.getString(ctx, VALUE)))
                                        )
                                )
                                .then(literal(OPACITY)
                                        .then(argument(VALUE, IntegerArgumentType.integer(0, 255))
                                                .executes(ctx -> setOpacity(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                        )
                        )
                ));
    }

    private static LiteralArgumentBuilder<CommandSourceStack> buildCommonSetSubcommands() {
        return literal(SET)
                .then(argument(NAME, StringArgumentType.word())
                        .then(literal(POSITION)
                                .then(argument(VALUE, BlockPosArgument.blockPos())
                                        .executes(ctx -> setPosition(ctx, StringArgumentType.getString(ctx, NAME), BlockPosArgument.getLoadedBlockPos(ctx, VALUE)))
                                )
                        )
                        .then(literal(ROTATION)
                                .then(argument(VALUE, StringArgumentType.string())
                                        .executes(ctx -> setRotation(ctx, StringArgumentType.getString(ctx, NAME), StringArgumentType.getString(ctx, VALUE)))
                                )
                        )
                        .then(literal(BRIGHTNESS)
                                .then(argument(VALUE, IntegerArgumentType.integer(0, 15))
                                        .executes(ctx -> setBrightness(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                )
                        )
                        .then(literal(VIEW_RANGE)
                                .then(argument(VALUE, IntegerArgumentType.integer(1))
                                        .executes(ctx -> setViewRange(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                )
                        )
                        .then(literal(BILLBOARD)
                                .then(argument(VALUE, StringArgumentType.word())
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(VALID_BILLBOARDS, builder))
                                        .executes(ctx -> setBillboard(ctx, StringArgumentType.getString(ctx, NAME), StringArgumentType.getString(ctx, VALUE)))
                                )
                        )
                        .then(literal(CUSTOM_NAME)
                                .then(argument(VALUE, StringArgumentType.string())
                                        .executes(ctx -> setCustomName(ctx, StringArgumentType.getString(ctx, NAME), StringArgumentType.getString(ctx, VALUE)))
                                )
                        )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> blockSubCmd(CommandBuildContext buildContext) {
        return literal(BLOCK)
                .then(literal(INFO)
                        .then(argument(NAME, StringArgumentType.word())
                                .executes(ctx -> showBlockInfo(ctx, StringArgumentType.getString(ctx, NAME)))
                        )
                )
                .then(buildCommonSetSubcommands())
                .then(literal(SET)
                        .then(argument(NAME, StringArgumentType.word())
                                .then(literal(BLOCK_STATE)
                                        .then(argument(VALUE, BlockStateArgument.block(buildContext))
                                                .executes(ctx -> setBlockState(ctx, StringArgumentType.getString(ctx, NAME), BlockStateArgument.getBlock(ctx, VALUE)))
                                        )
                                )
                                .then(literal(GLOW)
                                        .then(argument(VALUE, BoolArgumentType.bool())
                                                .executes(ctx -> setGlow(ctx, StringArgumentType.getString(ctx, NAME), BoolArgumentType.getBool(ctx, VALUE)))
                                        )
                                )
                                .then(literal(GLOW_COLOR)
                                        .then(argument(VALUE, IntegerArgumentType.integer(0, 0xFFFFFF))
                                                .executes(ctx -> setGlowColor(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                        )
                                )
                                .then(literal(BRIGHTNESS)
                                        .then(argument(VALUE, IntegerArgumentType.integer(0, 15))
                                                .executes(ctx -> setLightLevel(ctx, StringArgumentType.getString(ctx, NAME), IntegerArgumentType.getInteger(ctx, VALUE)))
                                        )
                                )
                        )
                );
    }



    private static boolean hasCmdPermission(CommandSourceStack css) {
        return css.hasPermission(2);
    }

    private static LiteralArgumentBuilder<CommandSourceStack> shapeSubCmd() {
        return literal("shape");
    }

    private static LiteralArgumentBuilder<CommandSourceStack> hideDisplayCmd() {
        return literal("hide")
                .then(argument("display", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            var level = ctx.getSource().getLevel();
                            var entries = BlockDisplayManager.get(level).visible().stream()
                                    .map(DisplayHandle::getName)
                                    .toList();
                            return SharedSuggestionProvider.suggest(entries, builder);
                        })
                        .executes(ctx -> hide(ctx, StringArgumentType.getString(ctx, "display")))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> showDisplayCmd() {
        return literal("show")
                .then(argument("display", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            var level = ctx.getSource().getLevel();
                            var entries = BlockDisplayManager.get(level).displayNames();
                            return SharedSuggestionProvider.suggest(entries, builder);
                        })
                        .executes(ctx -> show(ctx, StringArgumentType.getString(ctx, "display")))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> removeDisplayCmd() {
        return literal("remove")
                .then(argument("display", StringArgumentType.word())
                        .suggests((ctx, builder) -> {
                            var level = ctx.getSource().getLevel();
                            var entries = BlockDisplayManager.get(level).displayNames();
                            return SharedSuggestionProvider.suggest(entries, builder);
                        })
                        .executes(ctx -> remove(ctx, StringArgumentType.getString(ctx, "display")))
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createSphereShapeCmd() {
        return  literal("sphere")
                .then(argument("center", Vec3Argument.vec3())
                        .then(argument("radius", IntegerArgumentType.integer(0))
                                .then(argument("named", StringArgumentType.word())
                                        .executes((ctx) -> {
                                            var from = Vec3Argument.getVec3(ctx, "center");
                                            var to = IntegerArgumentType.getInteger(ctx, "radius");
                                            var name = StringArgumentType.getString(ctx, "named");
                                            return createSphere(ctx, from, to, name);
                                        })
                                )
                        )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createCuboidShapeCmd() {
        return literal(CUBOID)
                .then(argument("from", Vec3Argument.vec3())
                        .then(argument("to", Vec3Argument.vec3())
                                .then(argument("named", StringArgumentType.word())
                                        .executes((ctx) -> {
                                            var from = Vec3Argument.getVec3(ctx, "from");
                                            var to = Vec3Argument.getVec3(ctx, "to");
                                            var name = StringArgumentType.getString(ctx, "named");
                                            return createCuboid(ctx, from, to, name);
                                        })
                                )
                        )
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createBlockDisplayCmd() {
        return literal("block")
                .then(argument("named", StringArgumentType.word())
                        .executes(ctx -> {
                            var name = StringArgumentType.getString(ctx, "named");

                          //  return createBlockDisplay(ctx, name);
                          return 0;
                        })
                );
    }

    private static LiteralArgumentBuilder<CommandSourceStack> createTextDisplayCmd() {
        return literal(TEXT)
                .then(argument(ARG_NAME_TEXT, StringArgumentType.string())
                        .then(argument(POS, Vec3Argument.vec3())
                                .then(argument(ROT_X, IntegerArgumentType.integer(-180, 180))
                                        .then(argument(ROT_Y, IntegerArgumentType.integer(-90, 90))
                                                .then(argument(NAMED, StringArgumentType.word())
                                                        .executes(ctx -> {
                                                            var text = StringArgumentType.getString(ctx, ARG_NAME_TEXT);
                                                            var pos = Vec3Argument.getVec3(ctx, POS);
                                                            var yaw = IntegerArgumentType.getInteger(ctx, ROT_X);
                                                            var pitch = IntegerArgumentType.getInteger(ctx, ROT_Y);
                                                            var name = StringArgumentType.getString(ctx, NAMED);
                                                            return createTextDisplay(ctx, name, text, pos, yaw, pitch);
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(argument("named", StringArgumentType.word())
                                .executes(ctx -> {
                                    var text = StringArgumentType.getString(ctx, "text");
                                    var name = StringArgumentType.getString(ctx, "named");
                                    return createTextDisplay(ctx, name, text);
                                })
                        )
                );
    }

    private static int setAlignment(CommandContext<CommandSourceStack> ctx, String name, String value) {
        if (!VALID_ALIGNMENTS.contains(value.toLowerCase())) {
            ctx.getSource().sendFailure(Component.literal("Invalid alignment: '" + value + "'. Valid values are: center, left, right."));
            return -1;
        }
        // TODO: Implement actual logic to set alignment for the text display
        ctx.getSource().sendSystemMessage(Component.literal("Set alignment for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setBackground(CommandContext<CommandSourceStack> ctx, String name, int value) {
        // Accept any int, but warn if alpha < 26
        int alpha = (value >>> 24) & 0xFF;
        if (alpha < 26) {
            ctx.getSource().sendSystemMessage(Component.literal("Warning: Background alpha < 26, will be fully transparent in vanilla shader."));
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set background for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setDefaultBackground(CommandContext<CommandSourceStack> ctx, String name, boolean value) {

        ctx.getSource().sendSystemMessage(Component.literal("Set default background for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setLineWidth(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 1) {
            ctx.getSource().sendFailure(Component.literal("Line width must be at least 1."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set line width for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setSeeThrough(CommandContext<CommandSourceStack> ctx, String name, boolean value) {
        ctx.getSource().sendSystemMessage(Component.literal("Set see-through for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setShadow(CommandContext<CommandSourceStack> ctx, String name, boolean value) {
        ctx.getSource().sendSystemMessage(Component.literal("Set shadow for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setText(CommandContext<CommandSourceStack> ctx, String name, String value) {
        if (value == null || value.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Text cannot be empty."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set text for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int setOpacity(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 0 || value > 255) {
            ctx.getSource().sendFailure(Component.literal("Text opacity must be between 0 and 255."));
            return -1;
        }
        if (value <= 3) {
            ctx.getSource().sendSystemMessage(Component.literal("Opacity values 0-3 are treated as fully opaque (255)."));
        } else if (value < 26) {
            ctx.getSource().sendSystemMessage(Component.literal("Opacity values 4-25 will be discarded by vanilla rendering."));
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set text opacity for '" + name + "' to '" + value + "'."));
        return 1;
    }

    private static int showTextInfo(CommandContext<CommandSourceStack> ctx, String name) {
        var level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);        var blockSubCmd = literal("block")
                .then(literal("set")

                );
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Text display with name '" + name + "' does not exist."));
            return -1;
        }
        var display = dhOpt.get();
        // Replace with actual info extraction for your text display
        ctx.getSource().sendSystemMessage(Component.literal("Info for text display '" + name + "': " + display.toString()));
        return 1;
    }

    private static int setBlockState(CommandContext<CommandSourceStack> ctx, String name, BlockInput blockArg) {
        // TODO: Implement actual logic to set block state for the block display
        ctx.getSource().sendSystemMessage(Component.literal("Set block state for '" + name + "' to '" + blockArg.getState() + "'."));
        return 1;
    }
    private static int setGlow(CommandContext<CommandSourceStack> ctx, String name, boolean value) {
        ctx.getSource().sendSystemMessage(Component.literal("Set glow for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int setGlowColor(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 0 || value > 0xFFFFFF) {
            ctx.getSource().sendFailure(Component.literal("Glow color must be a valid RGB integer (0x000000 - 0xFFFFFF)."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set glow color for '" + name + "' to '" + String.format("#%06X", value) + "'."));
        return 1;
    }
    private static int setLightLevel(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 0 || value > 15) {
            ctx.getSource().sendFailure(Component.literal("Light level must be between 0 and 15."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set light level for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int showBlockInfo(CommandContext<CommandSourceStack> ctx, String name) {
        var level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Block display with name '" + name + "' does not exist."));
            return -1;
        }
        var display = dhOpt.get();
        ctx.getSource().sendSystemMessage(Component.literal("Info for block display '" + name + "': " + display.toString()));
        return 1;
    }

    private static int show(CommandContext<CommandSourceStack> ctx, String displayName) throws CommandSyntaxException {
        var level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(displayName);
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.show.block.fail.not-existent", "Display entity with name '%s' does not exist.", displayName));
            return -1;
        }
        DisplayHandle displayHandle = dhOpt.get();
        if (displayHandle.isVisible()) {
            ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.show.block.info.visible", "Display entity with name '%s' is already visible.", displayName));
            return 0;
        }
        BlockDisplayManager.track(level, displayHandle, true);
        return 0;
    }

    private static int showShape(CommandContext<CommandSourceStack> ctx, String shapeName) {
        return showShape(ctx, shapeName, ShapeStyle.FRAME);
    }

    private static int showShape(CommandContext<CommandSourceStack> ctx, String displayName, ShapeStyle style) {
        var level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(displayName);
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.show.block.fail.not-existent", "Display entity with name '%s' does not exist.", displayName));
            return -1;
        }
        var displayHandle = (ShapedBlockDisplay) dhOpt.get();
        if (displayHandle.isVisible(style)) {
            ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.show.block.info.visible", "Display entity with name '%s' is already visible.", displayName));
            return 0;
        }
        BlockDisplayManager.display(level, displayHandle, style);
        return 0;
    }

    private static int remove(CommandContext<CommandSourceStack> ctx, String named) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(named);
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.remove.display.fail.not-existent", "Display entity with name '%s' does not exist.", named));
            return -1;
        }

        // TODO: Delete
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.display.cuboid.success", "Created cuboid shape display with name '%s'.", named));
        return 0;
    }

    private static int createSphere(CommandContext<CommandSourceStack> ctx, Vec3 center, int radius, String named) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(named);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.block.fail.existent", "Display entity with name '%s' does already exist.", named));
            return -1;
        }
        SphereShape shape = new SphereShape(center, radius);
        ShapedBlockDisplay display = new ShapedBlockDisplayBuilder(shape, named)
                .initialStyle(ShapeStyle.FRAME)
                .build(level);
        BlockDisplayManager.track(level, display);
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.block.sphere.success", "Created sphere shape display with name '%s'.", named));
        return 1;
    }

    // TODO: Test if this works with Vec3 args when looking at blocks
    private static int createCuboid(CommandContext<CommandSourceStack> ctx, Vec3 from, Vec3 to, String named) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(named);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.block.fail.existent", "Display entity with name '%s' does already exist.", named));
            return -1;
        }
        CuboidShape shape = new CuboidShape(from, to);
        ShapedBlockDisplay display = new ShapedBlockDisplayBuilder(shape, named)
                .initialStyle(ShapeStyle.FRAME)
                .build(level);
        BlockDisplayManager.track(level, display);
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.block.cuboid.success", "Created cuboid shape display with name '%s'.", named));
        return 1;
    }


    private static int createTextDisplay(CommandContext<CommandSourceStack> ctx, String name, String text, Vec3 pos, float yaw, float pitch) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.text.fail.existent", "Text display with name '%s' already exists.", name));
            return -1;
        }
        // Display.TextDisplay textDisplay = TextDisplayUtil.createTextDisplay(text, pos, yaw, pitch, level);
        // BlockDisplayManager.track(level, textDisplay);
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.text.success", "Created text display with name '%s' at %s.", name, pos));
        return 1;
    }

    private static int createTextDisplay(CommandContext<CommandSourceStack> ctx, String name, String text) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.text.fail.existent", "Text display with name '%s' already exists.", name));
            return -1;
        }
        if (!(ctx.getSource().getEntity() instanceof ServerPlayer player)) {
            ctx.getSource().sendFailure(Component.literal("Command must be executed by a player."));
            return -1;
        }
        var hitResult = player.pick(20, 0, false);
        Vec3 pos;
        float yaw, pitch;
        if (hitResult.getType() == Type.BLOCK) {
            var blockHit = (BlockHitResult) hitResult;
            var face = blockHit.getDirection();
            pos = Vec3.atCenterOf(blockHit.getBlockPos().relative(face));
            yaw = face.getStepX() * 90f;
            pitch = face.getStepY() * -90f;
        } else {
            // Not looking at a block: place 1 block in the direction the player is looking
            var lookVec = player.getLookAngle();
            var playerPos = player.position();
            pos = playerPos.add(lookVec);
            yaw = player.getYRot();
            pitch = player.getXRot();
        }
        return createTextDisplay(ctx, name, text, pos, yaw, pitch);
    }


    private static int createBlockDisplay(CommandContext<CommandSourceStack> ctx, String name, Vec3 pos, BlockInput blockInput) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.block.fail.existent", "Block display with name '%s' already exists.", name));
            return -1;
        }
        SingleBlockDisplay display = new SingleBlockDisplayBuilder(pos, name)
                .withState(blockInput.getState())
                .build(level);
        BlockDisplayManager.track(level, display);
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.block.success", "Created block display with name '%s' at %s.", name, pos));
        return 1;
    }

    private static int createBlockDisplay(CommandContext<CommandSourceStack> ctx, String name, BlockInput blockInput) {
        ServerLevel level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(name);
        if (dhOpt.isPresent()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.create.block.fail.existent", "Block display with name '%s' already exists.", name));
            return -1;
        }
        BlockPos pos = getPlayerLookingPos(ctx);
        if (pos == null) {
            ctx.getSource().sendFailure(Component.literal("Could not determine a valid position to place the block display. Look at a block or provide a position."));
            return -1;
        }
        // If not looking at a block, use overload
        boolean alignedToBlock = false;
        if (ctx.getSource().getEntity() instanceof ServerPlayer player) {
            var hitResult = player.pick(20, 0, false);
            if (hitResult.getType() == Type.BLOCK) {
                alignedToBlock = true;
            }
        }
        if (!alignedToBlock) {
            return createBlockDisplay(ctx, name, Vec3.atCenterOf(pos), blockInput);
        }
        var display = new SingleBlockDisplayBuilder(Vec3.atCenterOf(pos), name)
                .withState(blockInput.getState())
                .build(level);
        
        BlockDisplayManager.track(level, display);
        ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.create.block.success", "Created block display with name '%s' at %s.", name, pos));
        return 1;
    }


    // Utility: get the position the player is looking at (block face or hit pos), or null if not a player or not looking at anything
    private static BlockPos getPlayerLookingPos(CommandContext<CommandSourceStack> ctx) {
        try {
            var player = ctx.getSource().getPlayerOrException();
            var hitResult = player.pick(20, 0, false);
            if (hitResult.getType() == Type.BLOCK) {
                var blockHit = (BlockHitResult) hitResult;
                // Place on the face the player is looking at
                return blockHit.getBlockPos().relative(blockHit.getDirection());
            }
            return null;
        } catch (CommandSyntaxException e) {
            return null;
        }
    }


    private static int hide(CommandContext<CommandSourceStack> ctx, String displayName) {
        var level = ctx.getSource().getLevel();
        Optional<DisplayHandle> dhOpt = BlockDisplayManager.get(level).getByName(displayName);
        if (dhOpt.isEmpty()) {
            ctx.getSource().sendFailure(Component.translatableWithFallback("cli.hide.block.fail.not-existent", "Display entity with name '%s' does not exist.", displayName));
            return -1;
        }
        DisplayHandle displayHandle = dhOpt.get();
        if (!displayHandle.isVisible()) {
            ctx.getSource().sendSystemMessage(Component.translatableWithFallback("cli.hide.block.info.not-visible", "Display entity with name '%s' is not visible.", displayName));
            return 0;
        }
        BlockDisplayManager.hide(level, displayHandle, true);
        return 0;
    }


    private static int promptHelp(CommandSourceStack src) {
        src.sendSystemMessage(Component.translatableWithFallback("cli.help.header", "== ShapeVisualizer help =="));
        MutableComponent wikiHint = Component.translatableWithFallback("help.tooltip.wiki.detail", "The in-game help is under construction. Visit the online wiki for a guide on how to use the mod.");
        // MutableComponent wikiText = Component.translatableWithFallback("help.tooltip.wiki", "Online-Wiki");
        //sendCmdFeedback(src, wikiHint);


        //sendCmdFeedback(src, Messages.substitutable("%s: %s", wikiText, buildWikiLink()));
        //sendCmdFeedback(src, Messages.substitutable(" => %s", buildHelpStartComponent()));


        return 0;
    }

    private static int setPosition(CommandContext<CommandSourceStack> ctx, String name, BlockPos pos) {
        ctx.getSource().sendSystemMessage(Component.literal("Set position for '" + name + "' to '" + pos + "'."));
        return 1;
    }
    private static int setRotation(CommandContext<CommandSourceStack> ctx, String name, String value) {
        // TODO: Validate and parse rotation string (e.g., "0 0 0" or "yaw pitch roll")
        ctx.getSource().sendSystemMessage(Component.literal("Set rotation for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int setBrightness(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 0 || value > 15) {
            ctx.getSource().sendFailure(Component.literal("Brightness must be between 0 and 15."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set brightness for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int setViewRange(CommandContext<CommandSourceStack> ctx, String name, int value) {
        if (value < 1) {
            ctx.getSource().sendFailure(Component.literal("View range must be at least 1."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set view range for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int setBillboard(CommandContext<CommandSourceStack> ctx, String name, String value) {
        if (!VALID_BILLBOARDS.contains(value.toLowerCase())) {
            ctx.getSource().sendFailure(Component.literal("Invalid billboard: '" + value + "'. Valid values are: fixed, vertical, horizontal, center."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set billboard for '" + name + "' to '" + value + "'."));
        return 1;
    }
    private static int setCustomName(CommandContext<CommandSourceStack> ctx, String name, String value) {
        if (value == null || value.isEmpty()) {
            ctx.getSource().sendFailure(Component.literal("Custom name cannot be empty."));
            return -1;
        }
        ctx.getSource().sendSystemMessage(Component.literal("Set custom name for '" + name + "' to '" + value + "'."));
        return 1;
    }
}
