package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.*;
import dev.stormy.client.utils.math.MathUtils;
import dev.stormy.client.utils.math.TimerUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import me.tryfle.stormy.events.UpdateEvent;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.MouseEvent;

import java.awt.*;

@SuppressWarnings("unused")
public class ClickAssist extends Module {

   TimerUtils timer = new TimerUtils();
   public static SliderSetting chance;
   public static TickSetting cpsCheck;
   public static DoubleSliderSetting cd;
   public static ComboSetting<modes> mode;
   private boolean allowClick = false;
   public Robot robot;
   private int cps;

   public ClickAssist() {
      super("ClickAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Chance to double click."));
      this.registerSetting(chance = new SliderSetting("Chance", 50.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(cd = new DoubleSliderSetting("Delay", 40, 100, 0, 300, 1));
      this.registerSetting(cpsCheck = new TickSetting("Only if above 5 CPS", true));
      this.registerSetting(mode = new ComboSetting<>("Mode", modes.LMB));
   }

   public void onEnable() {
      try {
         this.robot = new Robot();
      } catch (AWTException ex) {
         mc.thePlayer.addChatMessage(new ChatComponentText("An error has occurred: Stacktrace printed to console."));
         ex.printStackTrace();
         this.disable();
      }
   }

   public int getButton() {
      if (mode.getMode() == modes.LMB) {
         return 16;
      } else {
         return 4;
      }
   }
   public int getButton2() {
      if (mode.getMode() == modes.LMB) {
         return 0;
      } else {
         return 1;
      }
   }

   @SubscribeEvent
   public void onClick(MouseEvent e) {
      if (!PlayerUtils.isPlayerInGame()) return;
      if (e.getButton() == getButton2()) {
         cps++;
         if (allowClick) {
            double ch = Math.random() * 100;
            if (ch >= chance.getInput()) {
               return;
            }
         }
         if (cpsCheck.isToggled() && cps < 5) {
            return;
         }
         int d = MathUtils.randomInt(cd.getInputMin(), cd.getInputMax());
         if (timer.hasReached(d)) {
            logic();
            timer.reset();
         }
      }
   }

   @SubscribeEvent
    public void onUpdate(UpdateEvent e) {
        if (timer.hasReached(1000) && cps > 0) {
            cps = 0;
            timer.reset();
        }
    }

   public void logic() {
      if (allowClick) {
         this.robot.mouseRelease(getButton());
         this.robot.mousePress(getButton());
         this.robot.mouseRelease(getButton());
         allowClick = false;
      } else {
         allowClick = true;
      }
   }

   public void onDisable() {
      cps = 0;
      this.robot = null;
   }

   public enum modes {
      LMB, RMB
   }
}