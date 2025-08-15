package de.zordak.vizit.data.serialization.shape;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import de.zordak.vizit.shape.Shape;
import de.zordak.vizit.shape.ShapeTypes;

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