package de.zordak.vizit.api.display.text;

import de.zordak.vizit.api.display.properties.DisplayEntityUtils;
import de.zordak.vizit.api.display.properties.DisplayProperties;
import de.zordak.vizit.api.display.properties.TextDisplayProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.Vec3;

import static de.zordak.vizit.api.display.properties.DisplayEntityUtils.setEntityData;
import static de.zordak.vizit.api.display.properties.DisplayEntityUtils.setPosition;

public final class TextDisplayUtil {

    public static final String TAG_TEXT = "text";
    public static final String TAG_LINE_WIDTH = "line_width";
    public static final String TAG_OPACITY = "text_opacity";
    public static final String TAG_BACKGROUND = "background";
    public static final String TAG_SHADOW = "shadow";
    public static final String TAG_SEE_THROUGH = "see_through";
    public static final String TAG_DEFAULT_BACKGROUND = "default_background";
    public static final String TAG_ALIGNMENT = "alignment";
    
    private TextDisplayUtil() {}

    public static Display.TextDisplay createTextDisplay(DisplayProperties displayProps, TextDisplayProperties properties, Vec3 pos, float yRot, float xRot, ServerLevel level) {
        var textDisplay = DisplayEntityUtils.createText(level);
        DisplayEntityUtils.setDisplayProperties(textDisplay, displayProps);
        return setPosition(textDisplay, pos, yRot, xRot);

    }

    public static Display.TextDisplay setText(Display.TextDisplay display, String text) {
        CompoundTag tag = new CompoundTag();
        tag.putString(TAG_TEXT, text);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setAlignment(Display.TextDisplay display, Display.TextDisplay.Align alignment) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_ALIGNMENT, alignment.ordinal());
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setBackground(Display.TextDisplay display, int background) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(TAG_BACKGROUND, background);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setDefaultBackground(Display.TextDisplay display, boolean defaultBackground) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_DEFAULT_BACKGROUND, defaultBackground);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setLineWidth(Display.TextDisplay display, short lineWidth) {
        CompoundTag tag = new CompoundTag();
        tag.putShort(TAG_LINE_WIDTH, lineWidth);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setSeeThrough(Display.TextDisplay display, boolean seeThrough) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_SEE_THROUGH, seeThrough);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setShadow(Display.TextDisplay display, boolean shadow) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(TAG_SHADOW, shadow);
        return setEntityData(display, tag);
    }

    public static Display.TextDisplay setOpacity(Display.TextDisplay display, short opacity) {
        CompoundTag tag = new CompoundTag();
        tag.putShort(TAG_OPACITY, opacity);
        return setEntityData(display, tag);
    }


}
