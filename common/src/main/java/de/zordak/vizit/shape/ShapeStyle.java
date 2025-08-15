package de.zordak.vizit.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public enum ShapeStyle {
    FRAME("Frame"),
    HULL("Hull"),
    MINIMAL("Minimal"),
    MARKED("Marked");

    public final String name;

    ShapeStyle(String name) {
        this.name = name;
    }

    public static Set<String> entries(){
        return Arrays.stream(values())
                .map(ShapeStyle::toString)
                .collect(Collectors.toSet());
    }


    public static final Codec<ShapeStyle> CODEC = Codec.STRING.comapFlatMap(
            s -> {
                try {
                    return DataResult.success(ShapeStyle.of(s));
                } catch (Exception e) {
                    return DataResult.error(() -> "Invalid ShapeStyle: " + s);
                }
            },
            style -> style.name.toLowerCase(Locale.ROOT)
    );


    public static ShapeStyle of(String displayType) {
        return switch (displayType.toLowerCase()) {
            case "frame" -> FRAME;
            case "hull" -> HULL;
            case "minimal" -> MINIMAL;
            case "marked" -> MARKED;
            default -> throw new IllegalStateException("Unexpected value: " + displayType.toLowerCase());
        };
    }

    @Override
    public String toString() {
        return name;
    }
}
