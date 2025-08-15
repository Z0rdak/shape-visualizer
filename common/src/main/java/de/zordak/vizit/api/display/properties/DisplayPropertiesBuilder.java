package de.zordak.vizit.api.display.properties;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Display;
import net.minecraft.world.phys.Vec3;

public class DisplayPropertiesBuilder {
    private Vec3 pos = Vec3.ZERO;
    private float yRot = 0.0f;
    private float xRot = 0.0f;
    private Display.BillboardConstraints billboard = Display.BillboardConstraints.FIXED;
    private int brightness = 15;
    private float viewRange = 64.0f;
    private boolean glowing = false;
    private ChatFormatting glowColor = ChatFormatting.WHITE;

    public static DisplayPropertiesBuilder builder(BlockPos pos) {
        return new DisplayPropertiesBuilder()
                .pos(new Vec3(pos.getX(), pos.getY(), pos.getZ()));
    }

    public static DisplayPropertiesBuilder builder(Vec3 pos, float yRot, float xRot) {
        return new DisplayPropertiesBuilder()
                .pos(pos)
                .rotation(yRot, xRot);
    }

    public static DisplayPropertiesBuilder builder(Vec3 pos) {
        return new DisplayPropertiesBuilder().pos(pos);
    }

    public DisplayPropertiesBuilder pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public DisplayPropertiesBuilder yRot(float yRot) {
        this.yRot = yRot;
        return this;
    }

    public DisplayPropertiesBuilder rotation(float yRot, float xRot) {
        this.yRot = yRot;
        this.xRot = xRot;
        return this;
    }

    public DisplayPropertiesBuilder xRot(float xRot) {
        this.xRot = xRot;
        return this;
    }

    public DisplayPropertiesBuilder billboard(Display.BillboardConstraints billboard) {
        this.billboard = billboard;
        return this;
    }

    public DisplayPropertiesBuilder brightness(int brightness) {
        this.brightness = brightness;
        return this;
    }

    public DisplayPropertiesBuilder viewRange(float viewRange) {
        this.viewRange = viewRange;
        return this;
    }

    public DisplayPropertiesBuilder glowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    public DisplayPropertiesBuilder glowColor(ChatFormatting glowColor) {
        this.glowColor = glowColor;
        return this;
    }

    public DisplayProperties build() {
        return new DisplayProperties(
                pos,
                yRot,
                xRot,
                billboard,
                brightness,
                viewRange,
                glowing,
                glowColor
        );
    }
}