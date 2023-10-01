package xyz.blowsy.raven.module.modules.player;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.SliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

public class FastPlace extends Module {
   public static SliderSetting delaySlider;
   public static TickSetting blockOnly;

   public FastPlace() {
      super("FastPlace", ModuleCategory.Player, 0);
      this.registerSetting(delaySlider = new SliderSetting("Delay", 0.0D, 0.0D, 3.0D, 1.0D));
      this.registerSetting(blockOnly = new TickSetting("Blocks only", true));
   }

   @SubscribeEvent
   public void onPlayerTick(TickEvent.Post event) {
      if (Utils.Player.isPlayerInGame() && mc.inGameHasFocus) {
         if (blockOnly.isToggled()) {
            ItemStack item = mc.thePlayer.getHeldItem();
            if (item == null || !(item.getItem() instanceof ItemBlock)) {
               return;
            }
         }

         if ((int) delaySlider.getInput() == 0) {
            Minecraft.getMinecraft().rightClickDelayTimer = (int) delaySlider.getInput();
         } else {
            if ((int) delaySlider.getInput() == 4) {
               return;
            }

            if (Minecraft.getMinecraft().rightClickDelayTimer == 4) {
               Minecraft.getMinecraft().rightClickDelayTimer = (int) delaySlider.getInput();
            }
         }
      }
   }
}
