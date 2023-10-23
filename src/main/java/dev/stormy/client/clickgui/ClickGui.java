package dev.stormy.client.clickgui;

import dev.stormy.client.main.Stormy;
import dev.stormy.client.module.Module;
import net.minecraft.client.gui.GuiScreen;
import dev.stormy.client.clickgui.components.CategoryComponent;
import dev.stormy.client.module.modules.client.ClickGuiModule;

import java.util.ArrayList;
import java.util.Iterator;

public class ClickGui extends GuiScreen {
    private final  ArrayList<CategoryComponent> categoryList;

    public ClickGui() {
        this.categoryList = new ArrayList<>();
        int topOffset = 5;
        Module.ModuleCategory[] values;
        int categoryAmount = (values = Module.ModuleCategory.values()).length;

        for(int category = 0; category < categoryAmount; ++category) {
            Module.ModuleCategory moduleCategory = values[category];
            CategoryComponent currentModuleCategory = new CategoryComponent(moduleCategory);
            currentModuleCategory.setY(topOffset);
            categoryList.add(currentModuleCategory);
            topOffset += 20;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void drawScreen(int x, int y, float p) {
        drawRect(0, 0, this.width, this.height, -1308622848);
        for (CategoryComponent category : categoryList) {
            category.rf(this.fontRendererObj);
            category.up(x, y);

            for (Component module : category.getModules()) {
                module.update(x, y);
            }
        }
    }

    @Override
    public void mouseClicked(int x, int y, int mouseButton) {
        Iterator<CategoryComponent> btnCat = categoryList.iterator();

        while(true) {
            CategoryComponent category;
            do {
                do {
                    if (!btnCat.hasNext()) {
                        return;
                    }

                    category = btnCat.next();
                    if (category.insideArea(x, y) && !category.i(x, y) && !category.mousePressed(x, y) && mouseButton == 0) {
                        category.mousePressed(true);
                        category.xx = x - category.getX();
                        category.yy = y - category.getY();
                    }

                    if (category.mousePressed(x, y) && mouseButton == 0) {
                        category.setOpened(!category.isOpened());
                    }

                    if (category.i(x, y) && mouseButton == 0) {
                        category.cv(!category.p());
                    }
                } while(!category.isOpened());
            } while(category.getModules().isEmpty());

            for (Component c : category.getModules()) {
                c.mouseDown(x, y, mouseButton);
            }
        }
    }

    @Override
    public void mouseReleased(int x, int y, int s) {
        if (s == 0) {
            Iterator<CategoryComponent> btnCat = categoryList.iterator();

            CategoryComponent c4t;
            while(btnCat.hasNext()) {
                c4t = btnCat.next();
                c4t.mousePressed(false);
            }

            btnCat = categoryList.iterator();

            while(true) {
                do {
                    do {
                        if (!btnCat.hasNext()) {
                            return;
                        }

                        c4t = btnCat.next();
                    } while(!c4t.isOpened());
                } while(c4t.getModules().isEmpty());

                for (Component c : c4t.getModules()) {
                    c.mouseReleased(x, y, s);
                }
            }
        }
        if(Stormy.clientConfig != null){
            Stormy.clientConfig.saveConfig();
        }
    }

    @Override
    public void keyTyped(char t, int k) {
        if (k == 1) {
            this.mc.displayGuiScreen(null);
        } else {
            Iterator<CategoryComponent> btnCat = categoryList.iterator();

            while(true) {
                CategoryComponent cat;
                do {
                    do {
                        if (!btnCat.hasNext()) {
                            return;
                        }

                        cat = btnCat.next();
                    } while(!cat.isOpened());
                } while(cat.getModules().isEmpty());

                for (Component c : cat.getModules()) {
                    c.keyTyped(t, k);
                }
            }
        }
    }

    @Override
    public void onGuiClosed() {
        Stormy.configManager.save();
        Stormy.clientConfig.saveConfig();
        Module cgui = Stormy.moduleManager.getModuleByClazz(ClickGuiModule.class);
        if (cgui != null && cgui.isEnabled()) {
            cgui.disable();
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public ArrayList<CategoryComponent> getCategoryList() {
        return categoryList;
    }
}

