package xyz.blowsy.raven.module.modules.render;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.weavemc.loader.api.event.RenderWorldEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.clickgui.Theme;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.utils.Utils;

import java.util.Iterator;

public class ChestESP extends Module {
   public ChestESP() {
      super("ChestESP", ModuleCategory.Render, 0);
   }

   @SubscribeEvent
   public void onRenderWorldLast(RenderWorldEvent e) {
      if (Utils.Player.isPlayerInGame()) {
         Iterator<TileEntity> var3 = mc.theWorld.loadedTileEntityList.iterator();

         while(true) {
            TileEntity te;
            do {
               if (!var3.hasNext()) {
                  return;
               }

               te = var3.next();
            } while(!(te instanceof TileEntityChest) && !(te instanceof TileEntityEnderChest));

            Utils.HUD.re(te.getPos(), Theme.getMainColor().getRGB(), true);
         }
      }
   }
}
