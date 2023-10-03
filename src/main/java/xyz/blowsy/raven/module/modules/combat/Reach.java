package xyz.blowsy.raven.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.MouseEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.DoubleSliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

import java.util.List;

   public class Reach extends Module {
      public static DoubleSliderSetting reach;
      public static TickSetting sprintOnly, hitThroughBlocks;
      private static boolean moduleToggled = false;

      public Reach() {
         super("Reach", ModuleCategory.Combat, 0);
         this.registerSetting(new DescriptionSetting("Increases your reach."));
         this.registerSetting(reach = new DoubleSliderSetting("Reach", 3.0D, 3.15D, 3.0D, 6.0D, 0.05D));
         this.registerSetting(sprintOnly = new TickSetting("Sprint only", false));
         this.registerSetting(hitThroughBlocks = new TickSetting("Hit through blocks", false));
      }

      public void onDisable() {
         moduleToggled = false;
      }
      public void onEnable() {
         moduleToggled = true;
      }

      public static boolean callReach() {
         if (!Utils.Player.isPlayerInGame()) {
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

            double reach = Utils.Java.randomInt(Reach.reach.getMin(), Reach.reach.getMax()) / 100.0D;
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
         if (moduleToggled) {
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


      @SuppressWarnings("unused")
      @SubscribeEvent
      public void onMouseUpdate(MouseEvent e) {
         if (Utils.Player.isPlayerInGame() && e.getButton() == 0 && (!AutoClicker.moduleToggled || !Mouse.isButtonDown(0))) {
            callReach();
         }
      }
   }