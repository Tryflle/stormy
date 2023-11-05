package dev.stormy.client.module.modules.combat;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.ComboSetting;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import me.tryfle.stormy.events.LivingUpdateEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.weavemc.loader.api.event.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Mouse;
//some crazy hybrid of fractal and legit-ish

@SuppressWarnings("unused")
public class AimAssist extends Module {
   public static SliderSetting speed, fov, distance;
   public static TickSetting clickAim, weaponOnly, aimInvis;
   public static ComboSetting<aaModes> aamode;
   private boolean isAiming = false;

   public AimAssist() {
      super("AimAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Aims at enemies, HitboxEdge mode is beta."));
      this.registerSetting(speed = new SliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
      this.registerSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
      this.registerSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(clickAim = new TickSetting("Clicking only", true));
      this.registerSetting(weaponOnly = new TickSetting("Weapon only", false));
      this.registerSetting(aimInvis = new TickSetting("Aim at invis", false));
      this.registerSetting(aamode = new ComboSetting<>("Mode", aaModes.Center));
   }


   @SubscribeEvent
   public void onUpdateCenter(LivingUpdateEvent e) {
      if (mc.thePlayer == null || mc.currentScreen != null || !mc.inGameHasFocus || (weaponOnly.isToggled() && Utils.Player.isPlayerHoldingWeapon()) || aamode.getMode() == aaModes.HitboxEdge) return;
      if (!clickAim.isToggled() || (Stormy.moduleManager.getModuleByClazz(AutoClicker.class).isEnabled() && Mouse.isButtonDown(0))) {
         Entity en = this.getEnemy();
         if (en != null && en != mc.thePlayer) {
            double n = fovFromEntity((EntityPlayer) en);
            if (n > 1.0D || n < -1.0D) {
               float val = (float) (-(n / (101.0D - speed.getInput())));
               mc.thePlayer.rotationYaw += val / 2;
                  }}}}

   @SubscribeEvent
   public void onUpdateEdge(LivingUpdateEvent e) {
      if (mc.thePlayer == null || mc.currentScreen != null || !mc.inGameHasFocus || (weaponOnly.isToggled() && Utils.Player.isPlayerHoldingWeapon()) || aamode.getMode() != aaModes.HitboxEdge) {
         isAiming = false;
         return;
      }
      if (!clickAim.isToggled() || (Stormy.moduleManager.getModuleByClazz(AutoClicker.class).isEnabled() && Mouse.isButtonDown(0))) {
         EntityPlayer en = (EntityPlayer) this.getEnemy();
         if (en != null && en != mc.thePlayer) {
            if (!isAiming) {
               float edgeYaw = yawToEdgeOfHitbox(en);
               mc.thePlayer.rotationYaw = edgeYaw;
               isAiming = true;
            }
         }
      } else {
         isAiming = false;
      }
   }


   private float yawToEdgeOfHitbox(EntityPlayer entity) {
      Vec3 hitboxEdge = hitboxEval(entity);
      float yawToEdge = yawOnTarget(hitboxEdge);
      float yawDifference = yawToEdge - mc.thePlayer.rotationYaw;

      return mc.thePlayer.rotationYaw + yawDifference;
   }




   public Entity getEnemy() {
      int fov = (int) AimAssist.fov.getInput();
      for (EntityPlayer en : mc.theWorld.playerEntities) {
         if (!aimInvis.isToggled() && en.isInvisible()) continue;
         else if ((double) mc.thePlayer.getDistanceToEntity(en) > distance.getInput()) continue;
         else if (!fov(en, (float) fov)) continue;
         return en;
      }
      return null;
   }

   public static boolean fov(Entity entity, float fov) {
      fov = (float) ((double) fov * 0.5D);
      double v = ((double) (mc.thePlayer.rotationYaw - m(entity)) % 360.0D + 540.0D) % 360.0D - 180.0D;
      return v > 0.0D && v < (double) fov || (double) (-fov) < v && v < 0.0D;
   }
   public static float m(Entity ent) {
      double x = ent.posX - mc.thePlayer.posX;
      double z = ent.posZ - mc.thePlayer.posZ;
      double yaw = Math.atan2(x, z) * 57.2957795D;
      return (float) (yaw * -1.0D);
   }
   public static float[] gr(Entity q) {
      if (q == null) {
         return null;
      } else {
         double diffX = q.posX - mc.thePlayer.posX;
         double diffY;
         if (q instanceof EntityLivingBase en) diffY = en.posY + (double) en.getEyeHeight() * 0.9D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
            else diffY = (q.getEntityBoundingBox().minY + q.getEntityBoundingBox().maxY) / 2.0D - (mc.thePlayer.posY + (double) mc.thePlayer.getEyeHeight());
         double diffZ = q.posZ - mc.thePlayer.posZ;
         double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
         float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / 3.141592653589793D) - 90.0F;
         float pitch = (float) (-(Math.atan2(diffY, dist) * 180.0D / 3.141592653589793D));
         return new float[]{mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw), mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)};
      }
   }
   private Vec3 hitboxEval(final EntityPlayer entity) {
      AxisAlignedBB hitbox = entity.getEntityBoundingBox();

      double x = mc.thePlayer.posX;
      double y = mc.thePlayer.posY + mc.thePlayer.getEyeHeight();
      double z = mc.thePlayer.posZ;

      double closestX = Math.max(hitbox.minX, Math.min(x, hitbox.maxX));
      double closestY = Math.max(hitbox.minY, Math.min(y, hitbox.maxY));
      double closestZ = Math.max(hitbox.minZ, Math.min(z, hitbox.maxZ));

      return new Vec3(closestX, closestY, closestZ);
   }
   private float fovToEntity(final EntityPlayer entity) {
      double posX = entity.posX;
      double posZ = entity.posZ;

      double x = (float) posX - mc.thePlayer.posX;
      double z = (float) posZ - mc.thePlayer.posZ;

      double yaw = Math.atan2(x, z) * 57.5;

      return (float) (yaw * -1.0D);
   }
   private float yawOnTarget(final Vec3 targetPosition) {
      double x = targetPosition.xCoord - mc.thePlayer.posX;
      double z = targetPosition.zCoord - mc.thePlayer.posZ;

      double yaw = Math.atan2(x, z) * 57.5;

      return (float) (yaw * -1.0D);
   }
   private double fovFromEntity(final EntityPlayer entity) {
      return ((mc.thePlayer.rotationYaw - ((
              (aamode.getMode() == aaModes.HitboxEdge)
              ? yawOnTarget(hitboxEval(entity))
              : fovToEntity(entity))
      ) % 360.0D + 540.0D) % 360.0D - 180.0D);
   }
   public enum aaModes {
      Center, HitboxEdge
   }
}
