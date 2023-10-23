package me.tryfle.stormy.events;

import net.weavemc.loader.api.event.Event;

public class LivingUpdateEvent extends Event {
    public Type type;

    public LivingUpdateEvent(Type type) {
        this.type = type;
    }

    public enum Type {
        PRE,
        POST
    }
}
