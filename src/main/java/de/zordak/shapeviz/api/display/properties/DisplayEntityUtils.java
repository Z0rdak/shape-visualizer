package de.zordak.shapeviz.api.display.properties;

import de.zordak.shapeviz.ShapeVisualizer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.commands.data.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;

public class DisplayEntityUtils {

    public static final String TAG_DEFAULT_CUSTOM_DATA = ShapeVisualizer.MOD_ID+":data";

    public static <D extends Display> D setCustomData(D entity, String tagKey, CompoundTag dataTag) {
        CompoundTag tag = new CompoundTag();
        tag.put(tagKey, dataTag);
        return setEntityData(entity, tag);
    }

    public static <D extends Display> D setCustomData(D entity, CompoundTag dataTag) {
        return setCustomData(entity, TAG_DEFAULT_CUSTOM_DATA, dataTag);
    }

    public static <D extends Display> D setPosition(D entity, Vec3 pos) {
        setPosition(entity, pos, entity.getYRot(), entity.getXRot());
        return entity;
    }

    public static <D extends Display> D setPosition(D entity, Vec3 pos, float yRot, float xRot) {
        entity.moveTo(pos.x(), pos.y(), pos.z(), yRot, xRot);
        return entity;
    }

    public static <D extends Display> D setRotation(D entity, float yRot, float xRot) {
        entity.moveTo(entity.position().x(), entity.position().y(), entity.position().z(), yRot, xRot);
        return entity;
    }

    public static <D extends Display> D rotate(D entity, Rotation rotation) {
        entity.rotate(rotation);
        return entity;
    }

    public static <D extends Display> D rotateCw(D entity) {
        entity.rotate(Rotation.CLOCKWISE_90);
        return entity;
    }

    public static <D extends Display> D rotateCCw(D entity) {
        entity.rotate(Rotation.COUNTERCLOCKWISE_90);
        return entity;
    }


    public static <D extends Display> D updateGlow(D entity, boolean glow) {
        entity.setGlowingTag(glow);
        return entity;
    }

    public static <D extends Display> D setGlowColor(D entity, ChatFormatting glowColor) {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag tag = entityDataAccessor.getData();
        int color = glowColor.getColor() != null ? glowColor.getColor() : 16777215;
        tag.putInt("glow_color_override", color);
        setEntityData(entity, tag);
        return entity;
    }

    public static <D extends Display> D setEntityData(D entity, CompoundTag dataTag) {
        var entityDataAccessor = new EntityDataAccessor(entity);
        CompoundTag entityTag = entityDataAccessor.getData();
        var uuid = entity.getUUID();
        entityTag.merge(dataTag);
        entity.load(entityTag);
        entity.setUUID(uuid);
        return entity;
    }

    public static <D extends Display> CompoundTag getEntityData(D entity) {
        var entityDataAccessor = new EntityDataAccessor(entity);
        return entityDataAccessor.getData();
    }


    public static <D extends Display> D setBrightness(D entity, int lightLevel) {
        var tag = new CompoundTag();
        var brightnessTag = new CompoundTag();
        brightnessTag.putInt("sky", lightLevel);
        brightnessTag.putInt("block", lightLevel);
        tag.put("brightness", brightnessTag);
        return setEntityData(entity, brightnessTag);
    }

    public static Display.TextDisplay createText(ServerLevel level) {
        return new Display.TextDisplay(EntityType.TEXT_DISPLAY, level);
    }

    public static Display.TextDisplay createBlock(ServerLevel level) {
        return new Display.TextDisplay(EntityType.BLOCK_DISPLAY, level);
    }

    public static Display.TextDisplay createItem(ServerLevel level) {
        return new Display.TextDisplay(EntityType.ITEM_DISPLAY, level);
    }

    public static <D extends Display> D setDisplayProperties(D displayEntity, DisplayProperties displayProperties) {
        var res = setBrightness(displayEntity, displayProperties.brightness());
        return res;
    }
}
