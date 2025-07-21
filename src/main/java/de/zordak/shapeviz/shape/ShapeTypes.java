package de.zordak.shapeviz.shape;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Lifecycle;
import de.zordak.shapeviz.ShapeVisualizer;
import de.zordak.shapeviz.shape.strategy.*;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class ShapeTypes {

    public static final ResourceKey<Registry<ShapeType<?>>> SHAPE_TYPE_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(ShapeVisualizer.MOD_ID, "shape_types"));

    public static final Registry<ShapeType<?>> REGISTRY = new MappedRegistry<>(SHAPE_TYPE_REGISTRY_KEY, Lifecycle.stable());

    public static final ShapeType<CuboidShape> CUBOID = register(new ShapeType<>("cuboid", CuboidShape.CODEC,
            Map.of(
                    ShapeStyle.FRAME, new CuboidFrameStrategy(),
                    ShapeStyle.HULL, new CuboidHullStrategy(),
                    ShapeStyle.MINIMAL, new CuboidMinimalStrategy(),
                    ShapeStyle.MARKED, new CuboidMarkedStrategy()
            )));
    public static final ShapeType<SphereShape> SPHERE = register(new ShapeType<>("sphere", SphereShape.CODEC,
            Map.of(
                    ShapeStyle.FRAME, new SphereFrameStrategy(),
                    ShapeStyle.HULL, new SphereHullStrategy()
            )));

    public static final Codec<Shape> SHAPE_CODEC = REGISTRY.byNameCodec()
            .dispatch("shapeType", Shape::getType, ShapeType::codec);

    public static <T extends Shape> ShapeType<T> register(ShapeType<T> shapeType) {
        return Registry.register(REGISTRY, ResourceLocation.fromNamespaceAndPath(ShapeVisualizer.MOD_ID, shapeType.id()), shapeType);
    }
}