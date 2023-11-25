package dev.stormy.client.module.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;

public class ClosetSpeed extends Module {

    public static TickSetting njd, jump;

    public ClosetSpeed() {
        super("ClosetSpeed", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("For more minor speed cheats."));
        this.registerSetting(njd = new TickSetting("No Jump Delay", true));
        this.registerSetting(jump = new TickSetting("Hold Jump", true));
    }

    @SubscribeEvent
    public void onTick(TickEvent e) {
        if (mc.thePlayer != null) {
            if (njd.isToggled()) {
                mc.thePlayer.jumpTicks = 0;
            }
        }
    }

    @SubscribeEvent
    public void legitJump(TickEvent e) {
        if (mc.thePlayer != null) {
            if (jump.isToggled() && !mc.thePlayer.isSneaking())  {
                if (mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() ||mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown()) {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), true);
                } else {
                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
                }
            }
        }
    }
}
