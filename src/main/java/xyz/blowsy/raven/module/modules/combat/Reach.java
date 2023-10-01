package xyz.blowsy.raven.module.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;
import xyz.blowsy.raven.module.setting.impl.DoubleSliderSetting;
import xyz.blowsy.raven.module.setting.impl.TickSetting;
import xyz.blowsy.raven.utils.Utils;

import java.util.List;
import java.util.Random;

public class Reach extends Module {
   public static DoubleSliderSetting reach;
   public static TickSetting sprintOnly;

   private static final Random rand = new Random();

   public Reach() {
      super("Reach", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Increases your reach."));
      this.registerSetting(reach = new DoubleSliderSetting("Reach", 3.0D, 3.15D, 3.0D, 6.0D, 0.05D));
      this.registerSetting(sprintOnly = new TickSetting("Sprint only", false));
   }

   @SubscribeEvent
   public void onRenderLiving(RenderLivingEvent event) {
      if (Utils.Player.isPlayerInGame() && Mouse.isButtonDown(0)) {
         callReach();
      }
   }

   public static boolean callReach() {
      if (!Utils.Player.isPlayerInGame()) {
         return false;
      } else if (sprintOnly.isToggled() && !mc.thePlayer.isSprinting()) {
         return false;
      } else {
         if (mc.objectMouseOver != null) {
            BlockPos p = mc.objectMouseOver.getBlockPos();
            if (p != null && mc.theWorld.getBlockState(p).getBlock() != Blocks.air) {
               return false;
            }
         }

         double reach = reachValue(rand);
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

   @Override
   public void guiUpdate() {
      if (reach.getInputMin() > reach.getInputMax()) {
         double p = reach.getInputMin();
         reach.setValueMin(reach.getInputMax());
         reach.setValueMax(p);
      }
   }

   public static double reachValue(Random r) {
      return reach.getInputMin() == reach.getInputMax() ? reach.getInputMin() : reach.getInputMin() + r.nextDouble() * (reach.getInputMax() - reach.getInputMin());
   }
}
