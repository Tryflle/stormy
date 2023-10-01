package xyz.blowsy.raven.module.modules.combat;

import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;

public class ClickAssist extends Module {
   public static SliderSetting chance;
   public static TickSetting left, right, blocksOnly, weaponOnly;

   public ClickAssist() {
      super("ClickAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Boost your CPS."));
      this.registerSetting(chance = new SliderSetting("Chance", 80.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(left = new TickSetting("Left click", true));
      this.registerSetting(weaponOnly = new TickSetting("Weapon only", true));
      this.registerSetting(right = new TickSetting("Right click", false));
      this.registerSetting(blocksOnly = new TickSetting("Blocks only", true));
   }
}