package xyz.blowsy.raven.module.modules.combat;

import net.weavemc.loader.api.event.SubscribeEvent;
import me.zircta.raven.events.LivingUpdateEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.utils.Utils;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;

public class Velocity extends Module {
   public static SliderSetting horizontal, vertical, chance, tickDelay;
   //skidded from legit-ish

   public Velocity() {
      super("Velocity", Module.ModuleCategory.Combat, 0);
      this.registerSetting(horizontal = new SliderSetting("Horizontal", 90.0D, 0.0D, 200.0D, 1.0D));
      this.registerSetting(vertical = new SliderSetting("Vertical", 100.0D, 0.0D, 200.0D, 1.0D));
      this.registerSetting(chance = new SliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(tickDelay = new SliderSetting("Tick Delay", 0.0D, 0.0D, 20.0D, 1.0D));
   }
   @SuppressWarnings("unused")
   @SubscribeEvent
   public void onTick(LivingUpdateEvent e) {
      if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime - tickDelay.getInput() && e.type == LivingUpdateEvent.Type.PRE) {
         if (chance.getInput() != 100.0D) {
            double ch = Math.random();
            if (ch >= chance.getInput() / 100.0D) {
               return;
            }
         }

         if (horizontal.getInput() != 100.0D) {
            mc.thePlayer.motionX *= horizontal.getInput() / 100.0D;
            mc.thePlayer.motionZ *= horizontal.getInput() / 100.0D;
         }

         if (vertical.getInput() != 100.0D) {
            mc.thePlayer.motionY *= vertical.getInput() / 100.0D;
         }
      }
   }
}