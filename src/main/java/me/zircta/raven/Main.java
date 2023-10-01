package me.zircta.raven;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.StartGameEvent;
import xyz.blowsy.raven.main.Raven;

public class Main implements ModInitializer {
    @Override
    public void preInit() {
        EventBus.subscribe(StartGameEvent.Post.class, (startGameEvent) -> Raven.init());
    }
}