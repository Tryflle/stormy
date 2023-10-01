package xyz.blowsy.raven.module.modules.combat;

import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DoubleSliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;

public class RightClicker extends Module {
   public static TickSetting onlyBlocks;
   public static DoubleSliderSetting CPS;

   public RightClicker() {
      super("RightClicker", Module.ModuleCategory.Combat, 0);
      this.registerSetting(CPS = new DoubleSliderSetting("CPS", 12, 16, 1,60, 0.5));
      this.registerSetting(onlyBlocks = new TickSetting("Only blocks", false));

   }
}