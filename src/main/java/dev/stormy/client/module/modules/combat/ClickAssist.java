package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.game.MouseManager;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.MouseEvent;

import java.awt.*;

//note to self: make this work
public class ClickAssist extends Module {
   public static SliderSetting chance;
   public static TickSetting cpsCheck;
   private boolean allowClick = false;
   public Robot robot;


   public ClickAssist() {
      super("ClickAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Chance to double click."));
      this.registerSetting(new DescriptionSetting("locked at 100%, don't use."));
      this.registerSetting(chance = new SliderSetting("Chance", 50.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(cpsCheck = new TickSetting("Only if above 5 CPS", true));
   }

   public void onEnable() {
      try {
         this.robot = new Robot();
      } catch (AWTException awtexc) {
         mc.thePlayer.addChatMessage(new ChatComponentText("An error has occurred and ClickAssist had to be disabled. Stacktrace printed to console."));
         awtexc.printStackTrace();
         this.disable();
      }
   }

   @SuppressWarnings("unused")
   @SubscribeEvent
   public void whyClick(MouseEvent e) {
      if (PlayerUtils.isPlayerInGame()) {
         if (cpsCheck.isToggled()) {
            if (e.getButtonState() && e.getButton() == 0 && chance.getInput() == 100.0D && MouseManager.getLeftClickCounter() >= 5) {
               logic();
            }
            if (e.getButtonState() && e.getButton() == 0 && chance.getInput() != 100.0D) {
               double ch = Math.random();
               if (ch >= chance.getInput() / 100.0D) {
                  logic();
               }
            }
         } else {
            if (e.getButtonState() && e.getButton() == 0 && chance.getInput() == 100.0D) {
               logic();
            }
            if (e.getButtonState() && e.getButton() == 0 && chance.getInput() != 100.0D) {
               double ch = Math.random();
               if (ch >= chance.getInput() / 100.0D) {
                  logic();
               }
            }
         }
      }
   }

   public void logic() {
      if (allowClick) {
         this.robot.mouseRelease(16);
         this.robot.mousePress(16);
         this.robot.mouseRelease(16);
         allowClick = false;
      } else {
         allowClick = true;
      }
   }
      public void onDisable() {
         this.robot = null;
      }
   }