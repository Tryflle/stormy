package dev.stormy.client.clickgui.components;

import dev.stormy.client.module.setting.impl.DescriptionSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import dev.stormy.client.clickgui.Component;
import dev.stormy.client.clickgui.Theme;

public class DescriptionComponent implements Component {
   private final DescriptionSetting desc;
   private final ModuleComponent p;
   private int o;

   public DescriptionComponent(DescriptionSetting desc, ModuleComponent b, int o) {
      this.desc = desc;
      this.p = b;
      this.o = o;
   }

   public void draw() {
      GL11.glPushMatrix();
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.desc.getDesc(), (float)((this.p.category.getX() + 4) * 2), (float)((this.p.category.getY() + this.o + 4) * 2), Theme.getMainColor().getRGB());
      GL11.glPopMatrix();
   }

   @Override
   public void update(int mousePosX, int mousePosY) {

   }

   @Override
   public void mouseDown(int x, int y, int b) {

   }

   @Override
   public void mouseReleased(int x, int y, int m) {

   }

   @Override
   public void keyTyped(char t, int k) {

   }

   public void setComponentStartAt(int n) {
      this.o = n;
   }

   @Override
   public int getHeight() {
      return 0;
   }
}
