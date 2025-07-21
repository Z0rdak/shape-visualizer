package de.zordak.shapeviz.data.serialization.shape;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import de.zordak.shapeviz.shape.Shape;
import de.zordak.shapeviz.shape.ShapeTypes;

public class JsonShapeSerializer {

    public static JsonObject serialize(Shape shape) {
        return ShapeTypes.SHAPE_CODEC.encodeStart(JsonOps.INSTANCE, shape)
                .getOrThrow()
                .getAsJsonObject();
    }

    public static Shape deserialize(JsonObject obj) {
        return ShapeTypes.SHAPE_CODEC.parse(JsonOps.INSTANCE, obj)
                .getOrThrow();
    }
}