package de.zordak.shapeviz.api.display.properties;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import de.zordak.shapeviz.api.display.text.TextDisplayUtil;
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
) implements DisplayTypeProperties {

    @Override
    public String type() {
        return "text";
    }
    public static final Codec<TextDisplayProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf(TextDisplayUtil.TAG_TEXT)
                    .orElse("<Your text here>")
                    .forGetter(TextDisplayProperties::text),
            Display.TextDisplay.Align.CODEC.fieldOf(TextDisplayUtil.TAG_ALIGNMENT)
                    .orElse(Display.TextDisplay.Align.CENTER)
                    .forGetter(TextDisplayProperties::alignment),
            Codec.INT.fieldOf(TextDisplayUtil.TAG_BACKGROUND)
                    .orElse(Display.TextDisplay.INITIAL_BACKGROUND)
                    .forGetter(TextDisplayProperties::background),
            Codec.BOOL.fieldOf(TextDisplayUtil.TAG_DEFAULT_BACKGROUND)
                    .orElse(false)
                    .forGetter(TextDisplayProperties::defaultBackground),
            Codec.SHORT.fieldOf(TextDisplayUtil.TAG_LINE_WIDTH)
                    .orElse((short) 200)
                    .forGetter(TextDisplayProperties::lineWidth),
            Codec.BOOL.fieldOf(TextDisplayUtil.TAG_SEE_THROUGH)
                    .orElse(false)
                    .forGetter(TextDisplayProperties::seeThrough),
            Codec.BOOL.fieldOf(TextDisplayUtil.TAG_SHADOW)
                    .orElse(false)
                    .forGetter(TextDisplayProperties::shadow),
            Codec.SHORT.fieldOf(TextDisplayUtil.TAG_OPACITY)
                    .orElse((short) -1)
                    .forGetter(TextDisplayProperties::opacity),
            Codec.STRING.fieldOf("type").orElse("text").forGetter(p -> "block")
    ).apply(instance, (text, alignment, background, defaultBackground, lineWidth, seeThrough, shadow, opacity, type) ->
            new TextDisplayProperties(text, alignment, background, defaultBackground, lineWidth, seeThrough, shadow, opacity)
    ));
}