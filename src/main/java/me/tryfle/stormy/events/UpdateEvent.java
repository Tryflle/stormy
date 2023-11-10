package me.tryfle.stormy.events;

import net.weavemc.loader.api.event.CancellableEvent;

public class UpdateEvent extends CancellableEvent {
    private double x, y, z;
    private float yaw, pitch;
    private boolean onGround;
    private final boolean pre;

    public UpdateEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean pre) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.pre = pre;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isPre() {
        return pre;
    }

    public static class Pre extends UpdateEvent {
        public Pre(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            super(x, y, z, yaw, pitch, onGround, true);
        }
    }

    public static class Post extends UpdateEvent {
        public Post(double x, double y, double z, float yaw, float pitch, boolean onGround) {
            super(x, y, z, yaw, pitch, onGround, false);
        }
    }

}