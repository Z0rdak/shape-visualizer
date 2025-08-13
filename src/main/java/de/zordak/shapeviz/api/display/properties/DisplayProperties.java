package de.zordak.shapeviz.api.display.properties;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.Vec3;

public record DisplayProperties(
        Vec3 pos,
        float yRot,
        float xRot,
        Display.BillboardConstraints billboard,
        int brightness,
        float viewRange,
        // technically not a display entity properties (rather common entity properties)
        boolean glowing,
        ChatFormatting glowColor
) {
    public static final Codec<DisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("pos")
                    .forGetter(DisplayProperties::pos),
            Codec.FLOAT.fieldOf("yRot")
                    .orElse(0.0f)
                    .forGetter(DisplayProperties::yRot),
            Codec.FLOAT.fieldOf("xRot")
                    .orElse(0.0f)
                    .forGetter(DisplayProperties::xRot),
            Display.BillboardConstraints.CODEC.fieldOf("billboard")
                    .orElse(Display.BillboardConstraints.FIXED)
                    .forGetter(DisplayProperties::billboard),
            Codec.INT.fieldOf("brightness")
                    .orElse(0)
                    .forGetter(DisplayProperties::brightness),
            Codec.FLOAT.fieldOf("viewRange")
                    .orElse(64.0f)
                    .forGetter(DisplayProperties::viewRange),
            Codec.BOOL.fieldOf("glowing")
                    .orElse(false)
                    .forGetter(DisplayProperties::glowing),
            Codec.STRING.xmap(ChatFormatting::getByName, ChatFormatting::name)
                    .fieldOf("glowColor")
                    .orElse(ChatFormatting.WHITE)
                    .forGetter(p -> ChatFormatting.getByName(p.glowColor().name()))
    ).apply(instance, DisplayProperties::new));
}