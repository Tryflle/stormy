package dev.stormy.client.module.modules.render;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.DescriptionSetting;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("unused")
public class Chams extends Module {


   public Chams() {
      super("Chams", ModuleCategory.Render, 0);
      this.registerSetting(new DescriptionSetting("Show players through walls."));
   }

   @SubscribeEvent
   public void onPreLivingRender(RenderLivingEvent.Pre e) {
      if (e.getEntity() instanceof EntityPlayer) {
         glEnable(GL_POLYGON_OFFSET_FILL);
         glPolygonOffset(1.0F, -1100000.0F);
      }
   }

   @SubscribeEvent
   public void onPostLivingRender(RenderLivingEvent.Post e) {
      if (e.getEntity() instanceof EntityPlayer) {
         glDisable(GL_POLYGON_OFFSET_FILL);
         glPolygonOffset(1.0F, 1100000.0F);
      }
   }
}
