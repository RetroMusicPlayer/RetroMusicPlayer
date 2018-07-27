package code.name.monkey.retromusic.util;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class ColorUtils {

    public static boolean isColorLight(@ColorInt int color) {
        return getColorDarkness(color) < 0.5;
    }

    private static double getColorDarkness(@ColorInt int color) {
        if (color == Color.BLACK)
            return 1.0;
        else if (color == Color.WHITE || color == Color.TRANSPARENT)
            return 0.0;
        else
            return (1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255);
    }

    @ColorInt
    public static int getInverseColor(@ColorInt int color) {
        return (0xFFFFFF - color) | 0xFFFFFFFF;
    }

    public static boolean isColorSaturated(@ColorInt int color) {
        double max = Math.max(0.299 * Color.red(color), Math.max(0.587 * Color.green(color), 0.114 * Color.blue(color)));
        double min = Math.min(0.299 * Color.red(color), Math.min(0.587 * Color.green(color), 0.114 * Color.blue(color)));
        double diff = Math.abs(max - min);
        return diff > 20;
    }

    @ColorInt
    public static int getMixedColor(@ColorInt int color1, @ColorInt int color2) {
        return Color.rgb(
                (Color.red(color1) + Color.red(color2)) / 2,
                (Color.green(color1) + Color.green(color2)) / 2,
                (Color.blue(color1) + Color.blue(color2)) / 2
        );
    }

    public static double getDifference(@ColorInt int color1, @ColorInt int color2) {
        double diff = Math.abs(0.299 * (Color.red(color1) - Color.red(color2)));
        diff += Math.abs(0.587 * (Color.green(color1) - Color.green(color2)));
        diff += Math.abs(0.114 * (Color.blue(color1) - Color.blue(color2)));
        return diff;
    }

    @ColorInt
    public static int getReadableText(@ColorInt int textColor, @ColorInt int backgroundColor) {
        return getReadableText(textColor, backgroundColor, 100);
    }

    @ColorInt
    public static int getReadableText(@ColorInt int textColor, @ColorInt int backgroundColor, int difference) {
        boolean isLight = isColorLight(backgroundColor);
        for (int i = 0; getDifference(textColor, backgroundColor) < difference && i < 100; i++) {
            textColor = getMixedColor(textColor, isLight ? Color.BLACK : Color.WHITE);
        }

        return textColor;
    }

}