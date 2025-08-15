package de.zordak.vizit.api.display.properties;

public sealed interface DisplayTypeProperties permits BlockDisplayProperties, TextDisplayProperties {
    String type();
}
