package de.zordak.shapeviz.api.display.block;

import net.minecraft.world.entity.Display;

import java.util.Set;

public record CompositeBlockDisplay(Set<BlockDisplay> displays) {
}