package dev.stormy.client.module.modules.client;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

import java.util.HashMap;

public class AntiBot extends Module {
   private static final HashMap<EntityPlayer, Long> newEnt = new HashMap<>();
   public static TickSetting wait;

   public AntiBot() {
      super("AntiBot", ModuleCategory.Client, 0);
      this.registerSetting(wait = new TickSetting("Wait 80 ticks", false));
   }

   @Override
   public void onDisable() {
      newEnt.clear();
   }

   @SubscribeEvent
   public void onTick(TickEvent e) {
      if (wait.isToggled() && !newEnt.isEmpty()) {
         long now = System.currentTimeMillis();
         newEnt.values().removeIf((en) -> en < now - 4000L);
      }

   }

   public static boolean bot(Entity en) {
      if(!PlayerUtils.isPlayerInGame() || mc.currentScreen != null) return false;
      Module antiBot = Stormy.moduleManager.getModuleByClazz(AntiBot.class);
      if (antiBot != null && !antiBot.isEnabled()) {
         return false;
      } else if (wait.isToggled() && !newEnt.isEmpty() && newEnt.containsKey(en)) {
         return true;
      } else if (en.getName().startsWith("§c")) {
         return true;
      } else {
         String n = en.getDisplayName().getUnformattedText();
         if (n.contains("§")) {
            return n.contains("[NPC] ");
         } else {
            if (n.isEmpty() && en.getName().isEmpty()) {
               return true;
            }

            if (n.length() == 10) {
               int num = 0;
               int let = 0;
               char[] var4 = n.toCharArray();

               for (char c : var4) {
                  if (Character.isLetter(c)) {
                     if (Character.isUpperCase(c)) {
                        return false;
                     }

                     ++let;
                  } else {
                     if (!Character.isDigit(c)) {
                        return false;
                     }

                     ++num;
                  }
               }

               return num >= 2 && let >= 2;
            }
         }

         return false;
      }
   }
}
