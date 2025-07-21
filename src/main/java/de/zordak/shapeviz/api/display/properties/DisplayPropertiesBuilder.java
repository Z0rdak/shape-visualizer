package de.zordak.shapeviz.api.display.properties;

public class DisplayPropertiesBuilder {
    public static DisplayPropertiesBuilder builder() {
        return new DisplayPropertiesBuilder();
    }

    private final DisplayProperties props = new DisplayProperties();

    public DisplayPropertiesBuilder billboard(String mode) {
        props.setBillboard(mode);
        return this;
    }

    public DisplayPropertiesBuilder blockLight(int value) {
        props.setBlockLight(value);
        return this;
    }

    public DisplayPropertiesBuilder transform(DisplayTransform transform) {
        props.setTransform(transform);
        return this;
    }

    // ... other methods ...

    public DisplayProperties build() {
        return props;
    }
}