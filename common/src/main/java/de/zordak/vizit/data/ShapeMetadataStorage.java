package de.zordak.vizit.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;

public class ShapeMetadataStorage extends SavedData {

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        return null;
    }

    private static final String NAME = "blockviz_shapes";



}
