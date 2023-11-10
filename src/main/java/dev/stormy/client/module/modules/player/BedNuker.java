package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;

public class BedNuker extends Module {

   public BedNuker() {
      super("BedNuker", ModuleCategory.Player, 0);
      this.registerSetting(new DescriptionSetting("No logic YET"));
   }

}
