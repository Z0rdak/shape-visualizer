package de.zordak.shapeviz.api.display.properties;

public class DisplayProperties {
    private String billboard = "fixed";
    private Integer blockLight = null;
    private Integer skyLight = null;
    private int glowColorOverride = -1;
    private float height = 0f;
    private float width = 0f;
    private int interpolationDuration = 0;
    private int teleportDuration = 0;
    private int startInterpolation = 0;
    private float shadowRadius = 0f;
    private float shadowStrength = 1f;
    private float viewRange = 1f;
    private DisplayTransform transform = DisplayTransform.IDENTITY;

    // Getters and setters omitted for brevity
}
