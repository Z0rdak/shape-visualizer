package de.zordak.shapeviz.shape;

import de.zordak.shapeviz.shape.strategy.DisplayStrategy;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class StrategyRegistry {
    private final Map<ShapeClass, Map<ShapeStyle, DisplayStrategy>> registry = new HashMap<>();

    public void register(ShapeClass type, ShapeStyle style, DisplayStrategy strategy) {
        registry.computeIfAbsent(type, k -> new HashMap<>()).put(style, strategy);
    }

    public Optional<DisplayStrategy> get(ShapeClass type, ShapeStyle style) {
        return Optional.ofNullable(registry.getOrDefault(type, Map.of()).get(style));
    }

    public Set<ShapeClass> getRegisteredTypes() {
        return registry.keySet();
    }

    public Set<ShapeStyle> getStylesFor(ShapeClass type) {
        return registry.getOrDefault(type, Map.of()).keySet();
    }
}
