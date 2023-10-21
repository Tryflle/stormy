package xyz.blowsy.raven.module.modules.movement;

import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;
import net.minecraft.client.settings.KeyBinding;

public class Sprint extends Module {

    public final TickSetting Omni;

    public Sprint() {
        super("Sprint", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("Automatically sprint."));
        this.registerSetting(Omni = new TickSetting("Omni", false));
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void AutoSprint(TickEvent e) {
        if (Omni.isToggled() && Utils.Player.isPlayerInGame() && !mc.thePlayer.isSneaking()) {
            if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
                mc.thePlayer.setSprinting(true);
            }
        }
        if (!Omni.isToggled() && Utils.Player.isPlayerInGame() && !mc.thePlayer.isSneaking()) {
            if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }
    }
}