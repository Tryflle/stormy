package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.player.PlayerUtils;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import dev.stormy.client.module.Module;
import dev.stormy.client.utils.Utils;
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
        if (Omni.isToggled() && PlayerUtils.isPlayerInGame() && !mc.thePlayer.isSneaking()) {
            if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
                mc.thePlayer.setSprinting(true);
            }
        }
        if (!Omni.isToggled() && PlayerUtils.isPlayerInGame() && !mc.thePlayer.isSneaking()) {
            if (mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }
    }
}