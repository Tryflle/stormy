package dev.stormy.client.clickgui.components;

import dev.stormy.client.module.setting.impl.ComboSetting;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import dev.stormy.client.clickgui.Component;
import dev.stormy.client.clickgui.Theme;

public class ModeComponent implements Component {
    private final ComboSetting mode;
    private final ModuleComponent module;
    private int x;
    private int y;
    private int o;

    public ModeComponent(ComboSetting desc, ModuleComponent b, int o) {
        this.mode = desc;
        this.module = b;
        this.x = b.category.getX() + b.category.getWidth();
        this.y = b.category.getY() + b.o;
        this.o = o;
    }

    public void draw() {
        GL11.glPushMatrix();
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        int bruhWidth = (int) (Minecraft.getMinecraft().fontRendererObj.getStringWidth(this.mode.getName() + ": ") * 0.5);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(this.mode.getName() + ": ", (float) ((this.module.category.getX() + 4) * 2), (float) ((this.module.category.getY() + this.o + 4) * 2), 0xffffffff);
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(String.valueOf(this.mode.getMode()), (float) ((this.module.category.getX() + 4 + bruhWidth) * 2), (float) ((this.module.category.getY() + this.o + 4) * 2), Theme.getMainColor().getRGB());
        GL11.glPopMatrix();
    }

    public void update(int mousePosX, int mousePosY) {
        this.y = this.module.category.getY() + this.o;
        this.x = this.module.category.getX();
    }

    public void setComponentStartAt(int n) {
        this.o = n;
    }

    @Override
    public int getHeight() {
        return 0;
    }


    public void mouseDown(int x, int y, int b) {
        if(i(x, y))
            this.mode.nextMode();
    }

    @Override
    public void mouseReleased(int x, int y, int m) {

    }

    @Override
    public void keyTyped(char t, int k) {

    }

    private boolean i(int x, int y) {
        return x > this.x && x < this.x + this.module.category.getWidth() && y > this.y && y < this.y + 11;
    }
}