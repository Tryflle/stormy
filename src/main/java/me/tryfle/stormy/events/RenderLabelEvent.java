package me.tryfle.stormy.events;

import net.minecraft.entity.Entity;
import net.weavemc.loader.api.event.CancellableEvent;

public class RenderLabelEvent extends CancellableEvent {
    private final Entity target;
    private final double x;
    private final double y;
    private final double z;

    public RenderLabelEvent(Entity target, double x, double y, double z) {
        this.target = target;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Entity getTarget() {
        return target;
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
}
