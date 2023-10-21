package xyz.blowsy.raven.module.modules.combat;

import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.TickEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.DoubleSliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

public class AutoClicker extends Module {
    public static TickSetting weaponOnly, breakBlocks, inventoryFill;
    public static DoubleSliderSetting leftCPS;

    public boolean shouldClick = false;

    public AutoClicker() {
        super("AutoClicker", ModuleCategory.Combat, 0);
        this.registerSetting(new DescriptionSetting("Click automatically"));
        this.registerSetting(leftCPS = new DoubleSliderSetting("CPS", 9, 13, 1, 24, 0.5));
        this.registerSetting(inventoryFill = new TickSetting("Inventory fill", false));
        this.registerSetting(weaponOnly = new TickSetting("Weapon only", false));
        this.registerSetting(breakBlocks = new TickSetting("Break blocks", false));
    }

    @SubscribeEvent
    public void willThisWorkIDontKnowTryItAndSee(MouseEvent e) {
        if (e.getButton() == 0 && e.getButtonState()) {
             shouldClick = true;
        }
    }
}
