package me.tryfle.stormy.events;

import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.Event;

public class DrawBlockHighlightEvent extends Event {
    public final BlockPos blockPos;

    public DrawBlockHighlightEvent(final BlockPos blockPos) {
        this.blockPos = blockPos;
    }
}
