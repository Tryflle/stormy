package xyz.blowsy.raven.module.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import xyz.blowsy.raven.module.Module;
import xyz.blowsy.raven.module.setting.impl.ComboSetting;
import xyz.blowsy.raven.module.setting.impl.DescriptionSetting;

import static org.lwjgl.opengl.GL11.*;

public class Chams extends Module {

   public static ComboSetting<modes> mode;

   public Chams() {
      super("Chams", ModuleCategory.Render, 0);
      this.registerSetting(new DescriptionSetting("Show players through walls."));
      this.registerSetting(mode = new ComboSetting<>("Mode", modes.Bright));
   }

   @SubscribeEvent
   public void onPreLivingRender(RenderLivingEvent.Pre e) {
      if (e.getEntity() instanceof EntityPlayer) {
         if (mode.getMode() == modes.Normal) {
            glEnable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, -1100000.0F);
         } else if (mode.getMode() == modes.Bright) {
            glEnable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, -1100000.0F);
            glDisable(GL_LIGHTING);
         } else if (mode.getMode() == modes.Colored) {
            glEnable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, -1100000.0F);
            glColor4f(1, 1, 1, 1);
         }
      }
   }

   @SubscribeEvent
   public void onPostLivingRender(RenderLivingEvent.Post e) {
      if (e.getEntity() instanceof EntityPlayer) {
         if (mode.getMode() == modes.Normal) {
            glDisable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, 1100000.0F);
         } else if (mode.getMode() == modes.Bright) {
            glDisable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, 1100000.0F);
            glDisable(GL_LIGHTING);
         } else if (mode.getMode() == modes.Colored) {
            glDisable(GL_POLYGON_OFFSET_FILL);
            glPolygonOffset(1.0F, 1100000.0F);
            glColor4f(1, 1, 1, 1);
         }
      }
   }

   public enum modes {
      Normal, Bright, Colored
   }
}
