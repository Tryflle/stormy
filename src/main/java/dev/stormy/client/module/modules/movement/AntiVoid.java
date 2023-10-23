package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.ComboSetting;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

public class AntiVoid extends Module {

    public static ComboSetting<modes> mode;
    public final SliderSetting fallDist;
    public static TickSetting AutoDisable;


    public AntiVoid() {
        super("AntiVoid", ModuleCategory.Movement, 0);
        this.registerSetting(new DescriptionSetting("Prevents falling in the void."));
        this.registerSetting(mode = new ComboSetting<>("Mode", modes.AutoFlag));
        this.registerSetting(fallDist = new SliderSetting("AutoFlag Fall Distance", 5.0D, 1.0D, 50.0D, 1.0D));
        this.registerSetting(AutoDisable = new TickSetting("AutoDisable", true));
    }
    @SuppressWarnings("unused")
    @SubscribeEvent
    public void imFlagging(TickEvent e) {
        if (Utils.Player.isPlayerInGame() && mode.getMode() == modes.AutoFlag) {
            if (mc.thePlayer.fallDistance >= fallDist.getInput()) {
                mc.thePlayer.motionY = 0;
                mc.thePlayer.fallDistance = 0;
                if (AutoDisable.isToggled()) {
                    this.toggle();
                }
            }
        }
        if (Utils.Player.isPlayerInGame() && mode.getMode() == modes.ManualFlag) {
            mc.thePlayer.motionY = 0;
            if (AutoDisable.isToggled()) {
                this.toggle();
            }
        }
    }

    public enum modes {
        ManualFlag, AutoFlag
    }
}

