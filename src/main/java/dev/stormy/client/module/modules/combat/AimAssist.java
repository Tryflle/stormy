package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;

public class AimAssist extends Module {
   public static SliderSetting speed, fov, distance;
   public static TickSetting breakBlocks;

   public AimAssist() {
      super("AimAssist", ModuleCategory.Combat, 0);
      this.registerSetting(speed = new SliderSetting("Speed", 45.0D, 5.0D, 100.0D, 1.0D));
      this.registerSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 360.0D, 1.0D));
      this.registerSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(breakBlocks = new TickSetting("Break blocks", true));
   }
}
