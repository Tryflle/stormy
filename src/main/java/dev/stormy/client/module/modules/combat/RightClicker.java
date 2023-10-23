package dev.stormy.client.module.modules.combat;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.weavemc.loader.api.event.RenderHandEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;

@SuppressWarnings("unused")
public class RightClicker extends Module {
   public static SliderSetting rCPS;
   public static TickSetting eatFood;
   public boolean shouldClick = false;
   long lastClickTime = 0;
   long wow = 0;
   int rmb = mc.gameSettings.keyBindUseItem.getKeyCode();

   public boolean delaying = false;

   public RightClicker() {
      super("RightClicker", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Click automatically"));
      this.registerSetting(rCPS = new SliderSetting("CPS", 10.0D, 1.0D, 20.0D, 1.0D));
      this.registerSetting(eatFood = new TickSetting("Whitelist Food", false));
   }

   @SubscribeEvent
   public void bop(RenderHandEvent e) {
      randomizer();

      if (Utils.Player.isPlayerInGame() && Mouse.isButtonDown(1) && shouldClick && mc.currentScreen == null) {
         if (eatFood.isToggled() && mc.thePlayer.getHeldItem().getItem() instanceof ItemFood) {
            return;
         }
         long currentTime = System.currentTimeMillis();
         int delay = 1000 / (int) rCPS.getInput();

         if (currentTime - lastClickTime >= delay && !delaying) {
            lastClickTime = currentTime;
            KeyBinding.setKeyBindState(rmb, true);
            KeyBinding.onTick(rmb);
            delaying = true;
         }
         if (delaying) {
            finishDelay();
         }

      }
   }


   public void randomizer() {
      double random = Utils.Java.randomInt(0, 2);
      shouldClick = random >= 0.5;
   }
   public void finishDelay() {
      long currentTime = System.currentTimeMillis();
      int newdelay = Utils.Java.randomInt(20, 70);

      if (currentTime - lastClickTime >= newdelay) {
         lastClickTime = currentTime;
         KeyBinding.setKeyBindState(rmb, false);
         KeyBinding.onTick(rmb);
         delaying = false;
      }


   }
}
