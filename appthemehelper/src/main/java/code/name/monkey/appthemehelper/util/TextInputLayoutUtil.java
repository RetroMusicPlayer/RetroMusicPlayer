package code.name.monkey.appthemehelper.util;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;

import java.lang.reflect.Field;

/**
 * @author Aidan Follestad (afollestad)
 */
public final class TextInputLayoutUtil {

    public static void setHint(@NonNull TextInputLayout view, @ColorInt int hintColor) {
        try {
            final Field mDefaultTextColorField = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
            mDefaultTextColorField.setAccessible(true);
            mDefaultTextColorField.set(view, ColorStateList.valueOf(hintColor));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to set TextInputLayout hint (collapsed) color: " + t.getLocalizedMessage(), t);
        }
    }

    public static void setAccent(@NonNull TextInputLayout view, @ColorInt int accentColor) {
        try {
            final Field mFocusedTextColorField = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
            mFocusedTextColorField.setAccessible(true);
            mFocusedTextColorField.set(view, ColorStateList.valueOf(accentColor));
        } catch (Throwable t) {
            throw new RuntimeException("Failed to set TextInputLayout accent (expanded) color: " + t.getLocalizedMessage(), t);
        }
    }

    private TextInputLayoutUtil() {
    }
}