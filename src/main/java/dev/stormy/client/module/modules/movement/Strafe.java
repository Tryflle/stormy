package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.Module;
import dev.stormy.client.utils.MoveUtils;
import me.tryfle.stormy.events.MoveEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

public class Strafe extends Module {

    public Strafe() {
        super("Strafe", ModuleCategory.Movement, 0);
        registerSetting(new DescriptionSetting("Strafe"));
    }

    @SubscribeEvent
    public void onStrafe(MoveEvent e) {
        MoveUtils.strafe();
    }
}