package dev.stormy.client.module.modules.render;

import dev.stormy.client.module.Module;
import dev.stormy.client.module.modules.client.AntiBot;
import dev.stormy.client.module.setting.impl.SliderSetting;
import dev.stormy.client.module.setting.impl.TickSetting;
import dev.stormy.client.utils.math.MathUtils;
import me.tryfle.stormy.events.RenderLabelEvent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.weavemc.loader.api.event.RenderLivingEvent;
import net.weavemc.loader.api.event.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class Nametags extends Module {
   public static SliderSetting opacity;
   public static TickSetting shadow, showHealth;

   public Nametags() {
      super("Nametags", ModuleCategory.Render, 0);
      this.registerSetting(opacity = new SliderSetting("Opacity", 0.25D, 0.00D, 1.00D, 0.05D));
      this.registerSetting(shadow = new TickSetting("Shadow", false));
      this.registerSetting(showHealth = new TickSetting("Show health", true));
   }

   @SubscribeEvent
   public void onRenderLivingEvent(RenderLivingEvent.Pre event) {
      if (event.getEntity() instanceof EntityPlayer && event.getEntity() != mc.thePlayer && event.getEntity().deathTime == 0) {
         EntityPlayer en = (EntityPlayer) event.getEntity();
         if (AntiBot.bot(en) || en.getDisplayName().getUnformattedText().isEmpty()) return;

         String str = en.getDisplayName().getFormattedText();
         if (showHealth.isToggled()) {
            double r = en.getHealth() / en.getMaxHealth();
            String h = (r < 0.3D ? "\u00A7c" : (r < 0.5D ? "\u00A76" : (r < 0.7D ? "\u00A7e" : "\u00A7a"))) + MathUtils.round(en.getHealth(), 1);
            str = str + " " + h;
         }

         GlStateManager.pushMatrix();
         GlStateManager.translate((float) event.getX() + 0.0F, (float) event.getY() + en.height + 0.5F, (float) event.getZ());
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
         GlStateManager.rotate(mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
         float f1 = 0.02666667F;
         GlStateManager.scale(-f1, -f1, f1);
         if (en.isSneaking()) {
            GlStateManager.translate(0.0F, 9.374999F, 0.0F);
         }

         GlStateManager.disableLighting();
         GlStateManager.depthMask(false);
         GlStateManager.disableDepth();
         GlStateManager.enableBlend();
         GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
         Tessellator tessellator = Tessellator.getInstance();
         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
         int j = mc.fontRendererObj.getStringWidth(str) / 2;
         GlStateManager.disableTexture2D();
         worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
         worldrenderer.pos(-j - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, (float) opacity.getInput()).endVertex();
         worldrenderer.pos(-j - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, (float) opacity.getInput()).endVertex();
         worldrenderer.pos(j + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, (float) opacity.getInput()).endVertex();
         worldrenderer.pos(j + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, (float) opacity.getInput()).endVertex();
         tessellator.draw();

         GlStateManager.enableTexture2D();
         if (shadow.isToggled()) {
            mc.fontRendererObj.drawStringWithShadow(str, (float) -mc.fontRendererObj.getStringWidth(str) / 2, 0, -1); // IntelliJ wanted me to cast it to Float for some reason ¯\_(ツ)_/¯
         } else {
            mc.fontRendererObj.drawString(str, -mc.fontRendererObj.getStringWidth(str) / 2, 0, -1);
         }
         GlStateManager.enableDepth();
         GlStateManager.depthMask(true);
         GlStateManager.enableLighting();
         GlStateManager.disableBlend();
         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
         GlStateManager.popMatrix();
      }
   }

   @SubscribeEvent
   public void onRenderLabel(RenderLabelEvent e) {
      if(e.getTarget() instanceof EntityPlayer) {
         e.setCancelled(true);
      }
   }
}
