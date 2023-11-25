package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;

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
      if (PlayerUtils.isPlayerInGame() && mc.inGameHasFocus) {
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
