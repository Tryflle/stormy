package dev.stormy.client.clickgui;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import dev.stormy.client.module.modules.client.ClickGuiModule;
import dev.stormy.utils.render.Colors;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.Iterator;

public class ClickGui extends GuiScreen {

    private final ArrayList<CategoryComponent> categoryList;

    public ClickGui() {
        categoryList = new ArrayList<>();
        
        // Initialize categories
        int topOffset = 5;
        for (Module.ModuleCategory category : Module.ModuleCategory.values()) {
            CategoryComponent component = new CategoryComponent(category);
            component.setY(topOffset);
            categoryList.add(component);
            topOffset += 20;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        
        for (CategoryComponent category : categoryList) {
            category.render(fontRendererObj);
            category.update(mouseX, mouseY);

            for (Component module : category.getModules()) {
                module.update(mouseX, mouseY);
            }
        }
    }

    @Override 
    public void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
        } else {
            for (CategoryComponent category : categoryList) {
                if (category.isOpened() && !category.getModules().isEmpty()) {
                    for (Component module : category.getModules()) {
                        module.keyTyped(typedChar, keyCode);
                    }
                }
            }
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (state == 0) {
            for (CategoryComponent category : categoryList) {
                category.mousePressed(false);
                
                if (category.isOpened() && !category.getModules().isEmpty()) {
                    for (Component module : category.getModules()) {
                        module.mouseReleased(mouseX, mouseY, state);
                    }
                }
            }
            
            if (Stormy.clientConfig != null) {
                Stormy.clientConfig.saveConfig();
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (CategoryComponent category : categoryList) {
            if (category.isInside(mouseX, mouseY) && mouseButton == 0) {
                category.mousePressed(true);
                category.setLastX(mouseX - category.getX());
                category.setLastY(mouseY - category.getY());
                
                if (category.isMousePressed(mouseX, mouseY)) {
                    category.setOpened(!category.isOpened());
                }
            }
            
            if (category.isOpened()) {
                for (Component module : category.getModules()) {
                    module.mouseClicked(mouseX, mouseY, mouseButton);
                }
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui(); 
    }

    @Override
    public void onGuiClosed() {
        Stormy.configManager.save();
        Stormy.clientConfig.saveConfig();
        
        Module clickGuiModule = Stormy.moduleManager.getModuleByClass(ClickGuiModule.class);
        if (clickGuiModule != null && clickGuiModule.isEnabled()) {
            clickGuiModule.disable(); 
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
