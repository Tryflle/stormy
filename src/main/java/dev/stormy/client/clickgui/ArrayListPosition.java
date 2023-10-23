package dev.stormy.client.clickgui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import dev.stormy.client.module.modules.client.ArrayListModule;
import dev.stormy.client.utils.Utils;

import java.awt.*;
import java.util.Comparator;

public class ArrayListPosition extends GuiScreen {
    final String hudTextExample = "This is an-Example-HUD";
    GuiButton resetPosButton;
    boolean mouseDown = false;
    int textBoxStartX = 0;
    int textBoxStartY = 0;
    ScaledResolution sr;
    int textBoxEndX = 0;
    int textBoxEndY = 0;
    int marginX = 5;
    int marginY = 70;
    int lastMousePosX = 0;
    int lastMousePosY = 0;
    int sessionMousePosX = 0;
    int sessionMousePosY = 0;

    public void initGui() {
        super.initGui();
        this.buttonList.add(this.resetPosButton = new GuiButton(1, this.width - 90, 5, 85, 20, "Reset position"));
        this.marginX = ArrayListModule.hudX;
        this.marginY = ArrayListModule.hudY;
        sr = new ScaledResolution(mc);
        ArrayListModule.positionMode = Utils.HUD.getPostitionMode(marginX, marginY, sr.getScaledWidth(), sr.getScaledHeight());
    }

    public void drawScreen(int mX, int mY, float pt) {
        drawRect(0, 0, this.width, this.height, -1308622848);
        drawRect(0, this.height /2, this.width, this.height /2 + 1, 0x9936393f);
        drawRect(this.width /2, 0, this.width /2 + 1, this.height, 0x9936393f);
        int textBoxStartX = this.marginX;
        int textBoxStartY = this.marginY;
        int textBoxEndX = textBoxStartX + 50;
        int textBoxEndY = textBoxStartY + 32;
        this.drawArrayList(this.mc.fontRendererObj, this.hudTextExample);
        this.textBoxStartX = textBoxStartX;
        this.textBoxStartY = textBoxStartY;
        this.textBoxEndX = textBoxEndX;
        this.textBoxEndY = textBoxEndY;
        ArrayListModule.hudX = textBoxStartX;
        ArrayListModule.hudY = textBoxStartY;
        ScaledResolution res = new ScaledResolution(this.mc);
        int descriptionOffsetX = res.getScaledWidth() / 2 - 84;
        int descriptionOffsetY = res.getScaledHeight() / 2 - 20;
        Utils.mc.fontRendererObj.drawStringWithShadow("Edit the HUD position by dragging.", '-', descriptionOffsetX, descriptionOffsetY);

        this.handleInput();

        super.drawScreen(mX, mY, pt);
    }

    private void drawArrayList(FontRenderer fr, String t) {
        int x = this.textBoxStartX;
        int gap = this.textBoxEndX - this.textBoxStartX;
        int y = this.textBoxStartY;
        double marginY = fr.FONT_HEIGHT + 2;
        String[] var4 = t.split("-");
        java.util.ArrayList<String> var5 = Utils.Java.toArrayList(var4);
        if (ArrayListModule.positionMode == Utils.HUD.PositionMode.UPLEFT || ArrayListModule.positionMode == Utils.HUD.PositionMode.UPRIGHT) {
            var5.sort((o1, o2) -> Utils.mc.fontRendererObj.getStringWidth(o2) - Utils.mc.fontRendererObj.getStringWidth(o1));
        }
        else if(ArrayListModule.positionMode == Utils.HUD.PositionMode.DOWNLEFT || ArrayListModule.positionMode == Utils.HUD.PositionMode.DOWNRIGHT) {
            var5.sort(Comparator.comparingInt(o2 -> Utils.mc.fontRendererObj.getStringWidth(o2)));
        }

        if(ArrayListModule.positionMode == Utils.HUD.PositionMode.DOWNRIGHT || ArrayListModule.positionMode == Utils.HUD.PositionMode.UPRIGHT){
            for (String s : var5) {
                fr.drawString(s, (float) x + (gap - fr.getStringWidth(s)), (float) y, Color.white.getRGB(), true);
                y += (int) marginY;
            }
        } else {
            for (String s : var5) {
                fr.drawString(s, (float) x, (float) y, Color.white.getRGB(), true);
                y += (int) marginY;
            }
        }
    }

    public void mouseClickMove(int mousePosX, int mousePosY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mousePosX, mousePosY, clickedMouseButton, timeSinceLastClick);
        if (clickedMouseButton == 0) {
            if (this.mouseDown) {
                this.marginX = this.lastMousePosX + (mousePosX - this.sessionMousePosX);
                this.marginY = this.lastMousePosY + (mousePosY - this.sessionMousePosY);
                sr = new ScaledResolution(mc);
                ArrayListModule.positionMode = Utils.HUD.getPostitionMode(marginX, marginY,sr.getScaledWidth(), sr.getScaledHeight());

                //in the else if statement, we check if the mouse is clicked AND inside the "text box"
            } else if (mousePosX > this.textBoxStartX && mousePosX < this.textBoxEndX && mousePosY > this.textBoxStartY && mousePosY < this.textBoxEndY) {
                this.mouseDown = true;
                this.sessionMousePosX = mousePosX;
                this.sessionMousePosY = mousePosY;
                this.lastMousePosX = this.marginX;
                this.lastMousePosY = this.marginY;
            }

        }
    }

    public void mouseReleased(int mX, int mY, int state) {
        super.mouseReleased(mX, mY, state);
        if (state == 0) {
            this.mouseDown = false;
        }

    }

    public void actionPerformed(GuiButton b) {
        if (b == this.resetPosButton) {
            this.marginX = ArrayListModule.hudX = 5;
            this.marginY = ArrayListModule.hudY = 70;
        }

    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
