package dev.stormy.client.clickgui;

import dev.stormy.client.module.modules.client.ClickGuiModule;

import java.awt.*;

public class Theme {
    public static Color getMainColor() {
        String themeColor = ClickGuiModule.clientTheme.getMode().toString();

        switch (themeColor) {
            case "PastelPink":
                return new Color(237, 138, 209);
            case "Pink":
                return new Color(232, 100, 195);
            case "Tryfle":
                return new Color(216, 65, 100);
            case "Sassan":
                return new Color(255, 105, 105);
            case "Gold":
                return new Color(255, 215, 0);
            case "Steel":
                return new Color(52, 152, 219);
            case "Emerald":
                return new Color(46, 204, 113);
            case "Orange":
                return new Color(255, 165, 0);
            case "Amethyst":
                return new Color(155, 89, 182);
            case "Lily":
                return new Color(76, 56, 108);
            default: // UNUSED
                return new Color(255, 255, 255);
        }
    }

    public static Color getBackColor() {
        return new Color(0, 0, 0, 100);
    }

}