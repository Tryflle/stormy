package me.tryfle.stormy.events;

import net.weavemc.loader.api.event.Event;
public final class MoveEvent extends Event {
    public MoveEvent(double x, double y, double z) {
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    private double x, y, z;
}
