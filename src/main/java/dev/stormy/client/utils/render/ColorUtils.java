package dev.stormy.client.utils.render;

import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class ColorUtils {
    public static Color gradientDraw(Color color, int yLocation) {
        final double percent = Math.sin(System.currentTimeMillis() / 600.0D + yLocation * 0.06D) * 0.5D + 0.5D;
        return getColor(color, percent);
    }

    @NotNull
    private static Color getColor(Color color, double percent) {
        final double inverse_percent = 1.0 - percent;
        final int redPart = (int) (color.getRed() * percent + 255 * inverse_percent);
        final int greenPart = (int) (color.getGreen() * percent + 255 * inverse_percent);
        final int bluePart = (int) (color.getBlue() * percent + 255 * inverse_percent);
        return new Color(redPart, greenPart, bluePart);
    }

    public static Color reverseGradientDraw(Color color, int yLocation) {
        final double percent = Math.sin(System.currentTimeMillis() / 600.0D - yLocation * 0.06D) * 0.5D + 0.5D;
        return getColor(color, percent);
    }
}