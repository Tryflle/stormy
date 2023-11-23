package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.client.ClientUtils;
import dev.stormy.client.utils.math.CoolDown;
import dev.stormy.client.utils.Utils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Keyboard;

public class SafeWalk extends Module {
   public static TickSetting blocksOnly, shiftOnJump;
   public static TickSetting onHold, lookDown;
   public static DoubleSliderSetting pitchRange, shiftTime;

   private static boolean shouldBridge = false;
   private static boolean isShifting = false;
   private final CoolDown shiftTimer = new CoolDown(0);

   public SafeWalk() {
      super("SafeWalk",ModuleCategory.Player, 0);
      this.registerSetting(shiftOnJump = new TickSetting("Shift during jumps", false));
      this.registerSetting(shiftTime = new DoubleSliderSetting("Shift time: (s)", 140, 200, 0, 280, 5));
      this.registerSetting(onHold = new TickSetting("On shift hold", false));
      this.registerSetting(blocksOnly = new TickSetting("Blocks only", true));
      this.registerSetting(lookDown = new TickSetting("Only when looking down", true));
      this.registerSetting(pitchRange = new DoubleSliderSetting("Pitch min range:", 70D, 85, 0D, 90D, 1D));
   }

   @Override
   public void onDisable() {
      this.setShift(false);
      shouldBridge = false;
      isShifting = false;
   }

   @SubscribeEvent
   public void onTick(TickEvent e) {
      if(ClientUtils.currentScreenMinecraft()) return;

      if (!PlayerUtils.isPlayerInGame()) return;

      boolean shiftTimeSettingActive = shiftTime.getInputMax() > 0;
      if(lookDown.isToggled()) {
         if(mc.thePlayer.rotationPitch < pitchRange.getInputMin() || mc.thePlayer.rotationPitch > pitchRange.getInputMax()) {
            shouldBridge = false;
            if(Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
               setShift(true);
            }
            return;
         }
      }
      if (onHold.isToggled()) {
         if  (!Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            shouldBridge = false;
            return;
         }
      }

      if (blocksOnly.isToggled()) {
         ItemStack i = mc.thePlayer.getHeldItem();
         if (i == null || !(i.getItem() instanceof ItemBlock)) {
            if (isShifting) {
               isShifting = false;
               this.setShift(false);
            }

            return;
         }
      }

      if (mc.thePlayer.onGround) {
         if (PlayerUtils.playerOverAir()) {
            if(shiftTimeSettingActive){
               shiftTimer.setCooldown(Utils.Java.randomInt(shiftTime.getInputMin(), shiftTime.getInputMax() + 0.1));
               shiftTimer.start();
            }

            isShifting = true;
            this.setShift(true);
            shouldBridge = true;
         } else if (mc.thePlayer.isSneaking() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) {
            isShifting = false;
            shouldBridge = false;
            this.setShift(false);
         } else if(onHold.isToggled() && !Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
            isShifting = false;
            shouldBridge = false;
            this.setShift(false);
         } else if(mc.thePlayer.isSneaking() && (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()) && onHold.isToggled()) && (!shiftTimeSettingActive|| shiftTimer.hasFinished())) {
            isShifting = false;
            this.setShift(false);
            shouldBridge = true;
         } else if(mc.thePlayer.isSneaking() && !onHold.isToggled()  && (!shiftTimeSettingActive|| shiftTimer.hasFinished())) {
            isShifting = false;
            this.setShift(false);
            shouldBridge = true;
         }
      } else if (shouldBridge && mc.thePlayer.capabilities.isFlying) {
         this.setShift(false);
         shouldBridge = false;
      } else if (shouldBridge && blockRelativeToPlayer(0, -1, 0) instanceof BlockAir && shiftOnJump.isToggled()) {
         isShifting = true;
         this.setShift(true);
      } else {
         isShifting = false;
         this.setShift(false);
      }
   }

   public Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
      return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
   }

   private void setShift(boolean sh) {
      KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), sh);
   }
}