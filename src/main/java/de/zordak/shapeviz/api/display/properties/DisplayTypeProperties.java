package de.zordak.shapeviz.api.display.properties;

import com.mojang.serialization.Codec;

public sealed interface DisplayTypeProperties permits BlockDisplayProperties, TextDisplayProperties {
    String type();
}
