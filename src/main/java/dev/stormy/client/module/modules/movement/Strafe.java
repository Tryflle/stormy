package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.player.MoveUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import me.tryfle.stormy.events.MoveEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

public class Strafe extends Module {
    public TickSetting og, air;

    public Strafe() {
        super("Strafe", ModuleCategory.Movement, 0);
        registerSetting(new DescriptionSetting("Strafe"));
        registerSetting(og = new TickSetting("Ground", true));
        registerSetting(air = new TickSetting("Air", true));
    }

    @SubscribeEvent
    public void onStrafe(MoveEvent e) {
        if (!PlayerUtils.isPlayerInGame() || (!air.isToggled() && !mc.thePlayer.onGround) || (!og.isToggled() && mc.thePlayer.onGround)) return;
        MoveUtils.strafe();
    }
}