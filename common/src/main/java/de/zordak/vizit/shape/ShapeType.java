package de.zordak.vizit.shape;

import com.mojang.serialization.MapCodec;
import de.zordak.vizit.shape.strategy.DisplayStrategy;

import java.util.Map;
import java.util.Set;

public record ShapeType<T extends Shape>(String id, MapCodec<T> codec, Map<ShapeStyle, DisplayStrategy> strategies) {
    public DisplayStrategy strategyFor(ShapeStyle style) {
        return strategies.get(style);
    }

    public Set<ShapeStyle> supportedStyles() {
        return strategies.keySet();
    }

}