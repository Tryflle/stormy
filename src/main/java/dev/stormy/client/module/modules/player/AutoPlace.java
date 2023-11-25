package dev.stormy.client.module.modules.player;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.utils.client.ClientUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import me.tryfle.stormy.events.DrawBlockHighlightEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.weavemc.loader.api.event.TickEvent;
import org.lwjgl.input.Mouse;

public class AutoPlace extends Module {
   public static SliderSetting frameDelay;
   private long l = 0L;
   private int f = 0;
   private MovingObjectPosition lastMovingObj = null;
   private BlockPos lastPos = null;

   public AutoPlace() {
      super("AutoPlace", ModuleCategory.Player, 0);
      this.registerSetting(new DescriptionSetting("Automatically places blocks under you."));
      this.registerSetting(frameDelay = new SliderSetting("Frame delay (fps/80)", 8.0D, 0.0D, 30.0D, 1.0D));
   }

   @Override
   public void onDisable() {
      this.setRightDelay(4);
   }

   @SubscribeEvent
   public void onTick(TickEvent tickEvent) {
      if (!PlayerUtils.isPlayerInGame()) return;
      if (Mouse.isButtonDown(1) && !mc.thePlayer.capabilities.isFlying && !Stormy.moduleManager.getModuleByClazz(FastPlace.class).isEnabled()) {
         ItemStack i = mc.thePlayer.getHeldItem();
         if (i == null || !(i.getItem() instanceof ItemBlock)) {
            return;
         }

         this.setRightDelay(mc.thePlayer.motionY > 0.0D ? 1 : 1000);
      }
   }

   @SubscribeEvent
   public void onHighlight(DrawBlockHighlightEvent e) {
      if (PlayerUtils.isPlayerInGame()) {
         if (mc.currentScreen == null && !mc.thePlayer.capabilities.isFlying) {
            ItemStack i = mc.thePlayer.getHeldItem();
            if (i != null && i.getItem() instanceof ItemBlock) {
               MovingObjectPosition movingObj = mc.objectMouseOver;
               if (movingObj != null && movingObj.typeOfHit == MovingObjectType.BLOCK && movingObj.sideHit != EnumFacing.UP && movingObj.sideHit != EnumFacing.DOWN) {
                  if (this.lastMovingObj != null && (double) this.f < frameDelay.getInput()) {
                     ++this.f;
                  } else {
                     this.lastMovingObj = movingObj;
                     BlockPos pos = movingObj.getBlockPos();
                     if (this.lastPos == null || pos.getX() != this.lastPos.getX() || pos.getY() != this.lastPos.getY() || pos.getZ() != this.lastPos.getZ()) {
                        Block b = mc.theWorld.getBlockState(pos).getBlock();
                        if (b != null && b != Blocks.air && !(b instanceof BlockLiquid)) {
                           if (Mouse.isButtonDown(1)) {
                              long n = System.currentTimeMillis();
                              if (n - this.l >= 25L) {
                                 this.l = n;
                                 if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, i, pos, movingObj.sideHit, movingObj.hitVec)) {
                                    ClientUtils.setMouseButtonState(1, false);
                                    mc.thePlayer.swingItem();
                                    mc.getItemRenderer().resetEquippedProgress();
                                    ClientUtils.setMouseButtonState(1, false);
                                    this.lastPos = pos;
                                    this.f = 0;
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void setRightDelay(int i) {
      mc.rightClickDelayTimer = i;
   }
}