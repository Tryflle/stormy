package xyz.blowsy.raven.module.modules.combat;

import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.MouseEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

import java.awt.*;

public class ClickAssist extends Module {
   public static SliderSetting chance;
   public static TickSetting cpsCheck;

   public ClickAssist() {
      super("ClickAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Chance to double click. | Don't use rn"));
      this.registerSetting(chance = new SliderSetting("Chance", 80.0D, 0.0D, 100.0D, 1.0D));
      this.registerSetting(cpsCheck = new TickSetting("Only if above 5 CPS", true));
   }

   @SuppressWarnings("unused")
   @SubscribeEvent
   public void whyClick(MouseEvent e) throws AWTException {
      if (e.getButton() == 0 && Utils.Player.isPlayerInGame()) {
         Robot robot = new Robot();
         robot.mousePress(16);
         robot.mouseRelease(16);
      }
   }
}