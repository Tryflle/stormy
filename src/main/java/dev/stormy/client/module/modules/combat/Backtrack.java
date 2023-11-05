package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;

public class Backtrack extends Module {

    public static DoubleSliderSetting anvalue;

    public Backtrack() {
        super("Backtrack", Module.ModuleCategory.Combat, 0);
        this.registerSetting(anvalue = new DoubleSliderSetting("value", 5, 10, 0, 20, 1));
    }
}