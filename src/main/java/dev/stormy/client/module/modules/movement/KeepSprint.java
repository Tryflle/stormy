package dev.stormy.client.module.modules.movement;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.modules.combat.Reach;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.module.Module;
import me.tryfle.stormy.events.HitSlowDownEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

public class KeepSprint extends Module {
   public static SliderSetting speed;
   public static TickSetting reduce, sprint;

   public KeepSprint() {
      super("KeepSprint", ModuleCategory.Movement, 0);
      this.registerSetting(new DescriptionSetting("Default is 40% motion reduction"));
      this.registerSetting(new DescriptionSetting("and stopping sprint."));
      this.registerSetting(speed = new SliderSetting("Slow %", 40.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(reduce = new TickSetting("Only reduce reach hits", false));
      this.registerSetting(sprint = new TickSetting("Stop Sprint", true));
   }

   @SubscribeEvent
   public void onHitSlowDown(HitSlowDownEvent e) {
      double dist;
      Module reach = Stormy.moduleManager.getModuleByClazz(Reach.class);
      if (reduce.isToggled() && reach != null && reach.isEnabled() && !mc.thePlayer.capabilities.isCreativeMode) {
         dist = mc.objectMouseOver.hitVec.distanceTo(mc.getRenderViewEntity().getPositionEyes(1.0F));
         double val;
         if (dist > 3.0D) {
            val = (100.0D - (double) ((float) speed.getInput())) / 100.0D;
         } else {
            val = 0.6D;
         }

         e.setSlowDown(val);
      } else {
         double val;
         val = (100.0D - (double) ((float) speed.getInput())) / 100.0D;
         e.setSlowDown(val);
      }
      if (sprint.isToggled()) {
         e.setSprinting(false);
      }
   }
}