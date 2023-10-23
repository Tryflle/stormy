package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.module.Module;

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
