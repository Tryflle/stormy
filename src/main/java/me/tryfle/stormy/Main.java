package me.tryfle.stormy;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.StartGameEvent;
import dev.stormy.client.main.Stormy;

public class Main implements ModInitializer {
    @Override
    public void preInit() {
        EventBus.subscribe(StartGameEvent.Post.class, (startGameEvent) -> Stormy.init());
    }
}