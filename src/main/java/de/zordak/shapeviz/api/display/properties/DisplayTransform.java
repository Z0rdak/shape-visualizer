package de.zordak.shapeviz.api.display.properties;

import org.joml.Quaternionf;
import org.joml.Vector3f;


public class DisplayTransform {
    public static final DisplayTransform IDENTITY = new DisplayTransform();

    private Quaternionf rightRotation = new Quaternionf(); // JOML
    private Vector3f scale = new Vector3f(1, 1, 1);
    private Quaternionf leftRotation = new Quaternionf();
    private Vector3f translation = new Vector3f();

    // Getters, setters, and constructors omitted
}