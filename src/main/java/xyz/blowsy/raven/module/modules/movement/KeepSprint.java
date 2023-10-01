package xyz.blowsy.raven.module.modules.movement;

import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;

public class KeepSprint extends Module {
   public static SliderSetting speed;
   public static TickSetting sprint;

   public KeepSprint() {
      super("KeepSprint", ModuleCategory.Movement, 0);
      this.registerSetting(new DescriptionSetting("Default is 40% motion reduction."));
      this.registerSetting(speed = new SliderSetting("Slow %", 40.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(sprint = new TickSetting("Stop Sprint", true));
   }
}
