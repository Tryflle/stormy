package dev.stormy.client.module.modules.movement;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.math.TimerUtils;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.player.PlayerUtils;
import me.tryfle.stormy.events.SlowdownEvent;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.weavemc.loader.api.event.SubscribeEvent;
import dev.stormy.client.module.Module;
import net.weavemc.loader.api.event.TickEvent;

public class NoSlow extends Module {
   public static SliderSetting speed;
   public static TickSetting autosprint, noweapons, noconsumables;
   public TimerUtils timer = new TimerUtils();
   int rmb = mc.gameSettings.keyBindUseItem.getKeyCode();
   int sprint = mc.gameSettings.keyBindSprint.getKeyCode();
   public boolean shouldFinishBlock = false;

   public NoSlow() {
      super("NoSlow", ModuleCategory.Movement, 0);
      this.registerSetting(new DescriptionSetting("Default is 80% slow."));
      this.registerSetting(speed = new SliderSetting("Slow %", 80.0D, 0.0D, 80.0D, 1.0D));
      this.registerSetting(autosprint = new TickSetting("Allow Sprint", false));
      this.registerSetting(noweapons = new TickSetting("Blacklist Weapons", false));
      this.registerSetting(noconsumables = new TickSetting("Blacklist Consumables", false));
   }

   @SubscribeEvent
   public void onSlowdown(SlowdownEvent e) {
      if (!PlayerUtils.isPlayerInGame()) return;
      if (noweapons.isToggled() && PlayerUtils.isPlayerHoldingWeapon()) return;
      if (noconsumables.isToggled() && consumableCheck()) return;
      e.setCancelled(true);
      mc.thePlayer.movementInput.moveForward *= (100.0F - (float) speed.getInput()) / 100.0F;
      mc.thePlayer.movementInput.moveStrafe *= (100.0F - (float) speed.getInput()) / 100.0F;
      if (autosprint.isToggled() && mc.gameSettings.keyBindSprint.isKeyDown() && !mc.thePlayer.isSprinting() && PlayerUtils.isPlayerMoving()) {
         KeyBinding.setKeyBindState(rmb, false);
         KeyBinding.onTick(rmb);
         if (timer.hasReached(100 + Utils.Java.randomInt(-10, 10))) {
            shouldFinishBlock = true;
            KeyBinding.setKeyBindState(sprint, true);
            KeyBinding.onTick(sprint);
            timer.reset();
         }
      }
   }
   @SubscribeEvent
   public void reBlock(TickEvent e) {
      if (shouldFinishBlock && timer.hasReached(100 + Utils.Java.randomInt(-10, 10))) {
         shouldFinishBlock = false;
         KeyBinding.setKeyBindState(rmb, true);
         KeyBinding.onTick(rmb);
         timer.reset();
         }
      }
   public static boolean consumableCheck() {
      if (mc.thePlayer.getHeldItem() != null) {
         return noconsumables.isToggled() && (mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion);
      } else return false;
   }
}
