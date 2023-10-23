package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import me.tryfle.stormy.events.SlowdownEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import dev.stormy.client.module.Module;

public class NoSlow extends Module {
   public static SliderSetting speed;
   public static TickSetting autosprint;

   public NoSlow() {
      super("NoSlow", ModuleCategory.Movement, 0);
      this.registerSetting(new DescriptionSetting("Default is 80% slow."));
      this.registerSetting(speed = new SliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
      this.registerSetting(autosprint = new TickSetting("Sprint", false));
   }

   @SubscribeEvent
   public void onSlowdown(SlowdownEvent e) {
      e.setCancelled(true);
      mc.thePlayer.movementInput.moveForward *= (100.0F - (float) speed.getInput()) / 100.0F;
      mc.thePlayer.movementInput.moveStrafe *= (100.0F - (float) speed.getInput()) / 100.0F;
      if (autosprint.isToggled()) {
         mc.thePlayer.setSprinting(true);
      }
   }
}
