package de.zordak.shapeviz.api.display.text;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Display;

public record TextDisplayProperties(
        String text,
        Display.TextDisplay.Align alignment,
        int background,
        boolean defaultBackground,
        short lineWidth,
        boolean seeThrough,
        boolean shadow,
        short opacity
) {
    public static final Codec<TextDisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("text")
                    .orElse("<Your text here>")
                    .forGetter(TextDisplayProperties::text),
            Display.TextDisplay.Align.CODEC.fieldOf("alignment")
                    .orElse(Display.TextDisplay.Align.CENTER)
                    .forGetter(TextDisplayProperties::alignment),
            Codec.INT.fieldOf("background")
                    .orElse(Display.TextDisplay.INITIAL_BACKGROUND)
                    .forGetter(TextDisplayProperties::background),
            Codec.BOOL.fieldOf("defaultBackground")
                    .orElse(false)
                    .forGetter(TextDisplayProperties::defaultBackground),
            Codec.SHORT.fieldOf("lineWidth")
                    .orElse((short) 200)
                    .forGetter(TextDisplayProperties::lineWidth),
            Codec.BOOL.fieldOf("seeThrough")
                    .orElse(false)
                    .forGetter(TextDisplayProperties::seeThrough),
            Codec.BOOL.fieldOf("shadow")
                    .orElse(false)
                    .forGetter(TextDisplayProperties::shadow),
            Codec.SHORT.fieldOf("opacity")
                    .orElse((short) -1)
                    .forGetter(TextDisplayProperties::opacity)
    ).apply(instance, TextDisplayProperties::new));
}