package dev.stormy.client.module.modules.combat;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.Utils;
import me.tryfle.stormy.events.LivingUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.input.Mouse;

@SuppressWarnings("unused")
public class AimAssist extends Module {
   public static SliderSetting speed, fov, distance;
   public static TickSetting clickAim, weaponOnly, aimInvis;

   public AimAssist() {
      super("AimAssist", ModuleCategory.Combat, 0);
      this.registerSetting(new DescriptionSetting("Aims at enemies."));
      this.registerSetting(speed = new SliderSetting("Speed", 45.0D, 1.0D, 100.0D, 1.0D));
      this.registerSetting(fov = new SliderSetting("FOV", 90.0D, 15.0D, 180.0D, 1.0D));
      this.registerSetting(distance = new SliderSetting("Distance", 4.5D, 1.0D, 10.0D, 0.5D));
      this.registerSetting(clickAim = new TickSetting("Clicking only", true));
      this.registerSetting(weaponOnly = new TickSetting("Weapon only", false));
      this.registerSetting(aimInvis = new TickSetting("Aim at invis", false));
   }


   @SubscribeEvent
   public void onUpdateCenter(LivingUpdateEvent e) {
      if (mc.thePlayer == null || mc.currentScreen != null || !mc.inGameHasFocus || (weaponOnly.isToggled() && Utils.Player.isPlayerHoldingWeapon())) return;
      if (!clickAim.isToggled() || (Stormy.moduleManager.getModuleByClazz(AutoClicker.class).isEnabled() && Mouse.isButtonDown(0))) {
         Entity en = this.getEnemy();
         if (en != null && en != mc.thePlayer) {
            double n = n(en);
            if (n > 1.0D || n < -1.0D) {
               float val = (float) (-(n / (101.0D - speed.getInput())));
               mc.thePlayer.rotationYaw += val / 2;
                  }}}}

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
   public static double n(Entity en) {
      return ((double) (mc.thePlayer.rotationYaw - m(en)) % 360.0D + 540.0D) % 360.0D - 180.0D;
   }

   public static float m(Entity ent) {
      double x = ent.posX - mc.thePlayer.posX;
      double z = ent.posZ - mc.thePlayer.posZ;
      double yaw = Math.atan2(x, z) * 57.2957795D;
      return (float) (yaw * -1.0D);
   }
}
