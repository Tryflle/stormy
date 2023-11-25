package dev.stormy.client.module.modules.player;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import dev.stormy.client.utils.world.WorldUtils;
import me.tryfle.stormy.events.UpdateEvent;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.weavemc.loader.api.event.SubscribeEvent;

public class BedNuker extends Module {

   public BedNuker() {
      super("BedNuker", ModuleCategory.Player, 0);
      this.registerSetting(new DescriptionSetting("Breaks beds."));
   }

   @SubscribeEvent
   public void onUpdate(UpdateEvent e) {
      if (mc.thePlayer != null) {
         breakBlock(WorldUtils.findNearestBedPos(mc.theWorld, mc.thePlayer.getPosition(), 6));
      }
   }

   private void breakBlock(BlockPos pos) {
      if (mc.thePlayer != null) {
         try {
            mc.playerController.onPlayerDamageBlock(pos, EnumFacing.NORTH);
            mc.thePlayer.swingItem();
         } catch (Exception ignored) {}
      }
   }
}