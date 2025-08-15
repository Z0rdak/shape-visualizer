package de.zordak.vizit.api.display.properties;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public record DisplayProperties(
        Vec3 pos,
        float xRot,
        float yRot,
        Display.BillboardConstraints billboard,
        int brightness,
        float viewRange,
        // technically not a display entity properties (rather common entity properties)
        boolean glowing,
        ChatFormatting glowColor
) {
    public static final String TAG_POS = "Pos";
    public static final String TAG_ROTATION = "Rotation";
    public static final String TAG_BILLBOARD = Display.TAG_BILLBOARD;
    public static final String TAG_BRIGHTNESS = Display.TAG_BRIGHTNESS;
    public static final String TAG_VIEW_RANGE = Display.TAG_VIEW_RANGE;
    public static final String TAG_GLOWING = "Glowing";
    public static final String TAG_GLOW_COLOR = Display.TAG_GLOW_COLOR_OVERRIDE;

    public static final Codec<DisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf(TAG_POS)
                    .forGetter(DisplayProperties::pos),
            Codec.list(Codec.FLOAT).fieldOf(TAG_ROTATION)
                    .orElse(new ArrayList<>(List.of(0.0f, 0.0f)))
                    .forGetter((e) -> new ArrayList<>(List.of(e.xRot, e.yRot))),
            Display.BillboardConstraints.CODEC.fieldOf(TAG_BILLBOARD)
                    .orElse(Display.BillboardConstraints.FIXED)
                    .forGetter(DisplayProperties::billboard),
            Codec.INT.fieldOf(TAG_BRIGHTNESS)
                    .orElse(0)
                    .forGetter(DisplayProperties::brightness),
            Codec.FLOAT.fieldOf(TAG_VIEW_RANGE)
                    .orElse(64.0f)
                    .forGetter(DisplayProperties::viewRange),
            Codec.BOOL.fieldOf(TAG_GLOWING)
                    .orElse(false)
                    .forGetter(DisplayProperties::glowing),
            Codec.STRING.xmap(ChatFormatting::getByName, ChatFormatting::name)
                    .fieldOf(TAG_GLOW_COLOR)
                    .orElse(ChatFormatting.WHITE)
                    .forGetter(p -> ChatFormatting.getByName(p.glowColor().name()))
    ).apply(instance, (pos, rot, billboard, brightness, viewRange, glowing, glowColor) ->
            new DisplayProperties(pos, rot.getFirst(), rot.getLast(), billboard, brightness, viewRange, glowing, glowColor)));


    public static DisplayProperties defaultFor(Vec3 pos) {
        return new DisplayProperties(pos, 0.0f, 0.0f, Display.BillboardConstraints.FIXED,
                15, 1.0f, false, ChatFormatting.WHITE
        );
    }
}