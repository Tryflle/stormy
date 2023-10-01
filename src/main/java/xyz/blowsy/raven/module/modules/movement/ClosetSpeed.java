package xyz.blowsy.raven.module.modules.movement;

import me.zircta.raven.events.LivingUpdateEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;

public class ClosetSpeed extends Module {

    public static TickSetting speed, timer, njd;

    public ClosetSpeed() {
        super("ClosetSpeed", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("For more minor speed cheats."));
        this.registerSetting(speed = new TickSetting("Legit Speed", true));
        this.registerSetting(timer = new TickSetting("Legit Timer", true));
        this.registerSetting(njd = new TickSetting("No Jump Delay", true));
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1.0f;
    }

    @Override
    public void guiUpdate() {
        if (mc.thePlayer != null) {
            if (timer.isToggled()) {
                mc.timer.timerSpeed = 1.004f;
            } else {
                mc.timer.timerSpeed = 1.0f;
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(LivingUpdateEvent e) {
        if (mc.thePlayer != null) {
            if (speed.isToggled()) {
                mc.thePlayer.movementInput.moveForward *= 1.02F;
                mc.thePlayer.movementInput.moveStrafe *= 1.02F;
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (mc.thePlayer != null) {
            if (njd.isToggled()) {
                mc.thePlayer.jumpTicks = 0;
            }
        }
    }
}
