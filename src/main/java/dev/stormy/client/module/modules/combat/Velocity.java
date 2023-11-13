package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.module.setting.impl.ComboSetting;
import net.minecraft.client.Minecraft;
import net.weavemc.loader.api.event.PacketEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import me.tryfle.stormy.events.LivingUpdateEvent;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

@SuppressWarnings("unused")
public class Velocity extends Module {
   public static SliderSetting horizontal, vertical, chance, tickDelay;
   public static ComboSetting<velomode> velomodes;

   public Velocity() {
      super("Velocity", Module.ModuleCategory.Combat, 0);
      this.registerSetting(horizontal = new SliderSetting("Horizontal", 90.0D, 0.0D, 200.0D, 1.0D));
      this.registerSetting(vertical = new SliderSetting("Vertical", 100.0D, 0.0D, 200.0D, 1.0D));
      this.registerSetting(chance = new SliderSetting("Chance", 100.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(tickDelay = new SliderSetting("Tick Delay", 0.0D, 0.0D, 20.0D, 1.0D));
      this.registerSetting(velomodes = new ComboSetting<>("Mode", velomode.Normal));
   }

   @SubscribeEvent
   public void onTick (LivingUpdateEvent e){
      if (Utils.Player.isPlayerInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime - tickDelay.getInput() && e.type == LivingUpdateEvent.Type.PRE && velomodes.getMode() == velomode.Normal) {
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
   @SubscribeEvent
   public void onPacketReceive(PacketEvent.Receive e) {
      if (e.getPacket() instanceof S12PacketEntityVelocity s12 && velomodes.getMode() == velomode.Cancel) {
         if (Minecraft.getMinecraft().thePlayer != null && s12.getEntityID() == Minecraft.getMinecraft().thePlayer.entityId) {
            e.setCancelled(true);
         }
      }
   }

   public enum velomode {
      Normal, Cancel
   }
}