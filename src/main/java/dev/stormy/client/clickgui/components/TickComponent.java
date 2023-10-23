package dev.stormy.client.clickgui.components;

import dev.stormy.client.clickgui.Component;
import dev.stormy.client.clickgui.Theme;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.setting.impl.TickSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TickComponent implements Component {
   private final Module mod;
   private final TickSetting cl1ckbUtt0n;
   private final ModuleComponent module;
   private int o;
   private int x;
   private int y;

   public TickComponent(Module mod, TickSetting op, ModuleComponent b, int o) {
      this.mod = mod;
      this.cl1ckbUtt0n = op;
      this.module = b;
      this.x = b.category.getX() + b.category.getWidth();
      this.y = b.category.getY() + b.o;
      this.o = o;
   }

   public static void e() {
      GL11.glDisable(2929);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glDepthMask(true);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      GL11.glHint(3155, 4354);
   }

   public void draw() {
      GL11.glPushMatrix();
      GL11.glScaled(0.5D, 0.5D, 0.5D);
      Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.cl1ckbUtt0n.isToggled() ? "[+]  " + this.cl1ckbUtt0n.getName() : "[-]  " + this.cl1ckbUtt0n.getName(), (float)((this.module.category.getX() + 4) * 2), (float)((this.module.category.getY() + this.o + 5) * 2), this.cl1ckbUtt0n.isToggled() ? Theme.getMainColor().getRGB() : -1);

      GL11.glPopMatrix();
   }

   public void setComponentStartAt(int n) {
      this.o = n;
   }

   @Override
   public int getHeight() {
      return 0;
   }

   public void update(int mousePosX, int mousePosY) {
      this.y = this.module.category.getY() + this.o;
      this.x = this.module.category.getX();
   }

   public void mouseDown(int x, int y, int b) {
      if (this.i(x, y) && b == 0 && this.module.po) {
         this.cl1ckbUtt0n.toggle();
         this.mod.guiButtonToggled(this.cl1ckbUtt0n);
      }

   }

   @Override
   public void mouseReleased(int x, int y, int m) {

   }

   @Override
   public void keyTyped(char t, int k) {

   }

   public boolean i(int x, int y) {
      return x > this.x && x < this.x + this.module.category.getWidth() && y > this.y && y < this.y + 11;
   }
}
