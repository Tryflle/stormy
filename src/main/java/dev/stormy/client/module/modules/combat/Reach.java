package dev.stormy.client.module.modules.combat;


import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.setting.impl.DoubleSliderSetting;
import dev.stormy.client.utils.math.MathUtils;
import dev.stormy.client.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.MouseEvent;
import org.lwjgl.input.Mouse;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import net.weavemc.loader.api.event.SubscribeEvent;

import java.util.List;
import java.util.Random;

public class Reach extends Module {
   public static DoubleSliderSetting reachDist;
   public static TickSetting movingOnly, sprintOnly, hitThroughBlocks;

   public Reach() {
      super("Reach", ModuleCategory.Combat , 0);
      this.registerSetting(new DescriptionSetting("Increases your reach."));
      this.registerSetting(reachDist = new DoubleSliderSetting("Reach", 3.0D, 3.15D, 3.0D, 6.0D, 0.05D));
      this.registerSetting(movingOnly = new TickSetting("Moving only", false));
      this.registerSetting(sprintOnly = new TickSetting("Sprint only", false));
      this.registerSetting(hitThroughBlocks = new TickSetting("Hit through blocks", false));
   }

   public static double mmVal(DoubleSliderSetting a, Random r) {
      return a.getInputMin() == a.getInputMax() ? a.getInputMin() : a.getInputMin() + r.nextDouble() * (a.getInputMax() - a.getInputMin());
   }

   public static boolean callReach() {
      if (!PlayerUtils.isPlayerInGame()) {
         return false;
      } else if (movingOnly.isToggled() && (double) mc.thePlayer.moveForward == 0.0D && (double) mc.thePlayer.moveStrafing == 0.0D) {
         return false;
      } else if (sprintOnly.isToggled() && !mc.thePlayer.isSprinting()) {
         return false;
      } else {
         if (!hitThroughBlocks.isToggled() && mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();
            if (p != null && mc.theWorld.getBlockState(p).getBlock() != Blocks.air) {
               return false;
            }
         }

         double reach = mmVal(reachDist, MathUtils.rand());
         Object[] object = findEntitiesWithinReach(reach);
         if (object == null) {
            return false;
         } else {
            Entity en = (Entity) object[0];
            mc.objectMouseOver = new MovingObjectPosition(en, (Vec3) object[1]);
            mc.pointedEntity = en;
            return true;
         }
      }
   }

   private static Object[] findEntitiesWithinReach(double reach) {
      Module longArms = Stormy.moduleManager.getModuleByClazz(Reach.class);
      if (longArms != null && !longArms.isEnabled()) {
         reach = mc.playerController.extendedReach() ? 6.0D : 3.0D;
      }

      Entity renderView = mc.getRenderViewEntity();
      Entity target = null;
      if (renderView == null) {
         return null;
      } else {
         mc.mcProfiler.startSection("pick");
         Vec3 eyePosition = renderView.getPositionEyes(1.0F);
         Vec3 playerLook = renderView.getLook(1.0F);
         Vec3 reachTarget = eyePosition.addVector(playerLook.xCoord * reach, playerLook.yCoord * reach, playerLook.zCoord * reach);
         Vec3 targetHitVec = null;
         List<Entity> targetsWithinReach = mc.theWorld.getEntitiesWithinAABBExcludingEntity(renderView, renderView.getEntityBoundingBox().addCoord(playerLook.xCoord * reach, playerLook.yCoord * reach, playerLook.zCoord * reach).expand(1.0D, 1.0D, 1.0D));
         double adjustedReach = reach;

         for (Entity entity : targetsWithinReach) {
            if (entity.canBeCollidedWith()) {
               float ex = (float) ((double) entity.getCollisionBorderSize());
               AxisAlignedBB entityBoundingBox = entity.getEntityBoundingBox().expand(ex, ex, ex);
               MovingObjectPosition targetPosition = entityBoundingBox.calculateIntercept(eyePosition, reachTarget);
               if (entityBoundingBox.isVecInside(eyePosition)) {
                  if (0.0D < adjustedReach || adjustedReach == 0.0D) {
                     target = entity;
                     targetHitVec = targetPosition == null ? eyePosition : targetPosition.hitVec;
                     adjustedReach = 0.0D;
                  }
               } else if (targetPosition != null) {
                  double distanceToVec = eyePosition.distanceTo(targetPosition.hitVec);
                  if (distanceToVec < adjustedReach || adjustedReach == 0.0D) {
                     if (entity == renderView.ridingEntity) {
                        if (adjustedReach == 0.0D) {
                           target = entity;
                           targetHitVec = targetPosition.hitVec;
                        }
                     } else {
                        target = entity;
                        targetHitVec = targetPosition.hitVec;
                        adjustedReach = distanceToVec;
                     }
                  }
               }
            }
         }

         if (adjustedReach < reach && !(target instanceof EntityLivingBase) && !(target instanceof EntityItemFrame)) {
            target = null;
         }

         mc.mcProfiler.endSection();
         if (target != null && targetHitVec != null) {
            return new Object[]{target, targetHitVec};
         } else {
            return null;
         }
      }
   }

   public void guiUpdate() {
      Utils.Java.randomInt(reachDist.getMin(), reachDist.getMax());
   }
   @SuppressWarnings("unused")
   @SubscribeEvent
   public void longArmHaving(MouseEvent e) {
      if (PlayerUtils.isPlayerInGame() && Mouse.isButtonDown(0)) {
         callReach();
      }
   }
}