package xyz.blowsy.raven.module.modules.combat;

import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;

public class Velocity extends Module {
   public static SliderSetting horizontal, vertical, chance;

   public Velocity() {
      super("Velocity", ModuleCategory.Combat, 0);
      this.registerSetting(horizontal = new SliderSetting("Horizontal", 90.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(vertical = new SliderSetting("Vertical", 100.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(chance = new SliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
   }
}
