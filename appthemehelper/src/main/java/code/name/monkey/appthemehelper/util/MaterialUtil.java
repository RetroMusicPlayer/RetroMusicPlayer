package code.name.monkey.appthemehelper.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import code.name.monkey.appthemehelper.R;
import code.name.monkey.appthemehelper.ThemeStore;

public class MaterialUtil {
    public static void setTint(@NonNull MaterialButton button, boolean background) {
        setTint(button, background, ThemeStore.accentColor(button.getContext()));
    }

    public static void setTint(@NonNull MaterialButton button, boolean background, int color) {
        //button.setPadding(48, 48, 48, 48);
        button.setAllCaps(false);
        final Context context = button.getContext();
        final ColorStateList colorState = ColorStateList.valueOf(color);
        final ColorStateList textColor = ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)));


        if (background) {
            button.setBackgroundTintList(colorState);
            button.setTextColor(textColor);
            button.setIconTint(textColor);
        } else {
            button.setStrokeColor(colorState);
            button.setTextColor(colorState);
            button.setIconTint(colorState);
        }

        Typeface font = ResourcesCompat.getFont(button.getContext(), R.font.product_sans);
        button.setTypeface(font);

    }

    public static void setTint(TextInputLayout textInputLayout, boolean background) {
        final Context context = textInputLayout.getContext();
        final int accentColor = ThemeStore.accentColor(context);
        final ColorStateList colorState = ColorStateList.valueOf(accentColor);

        if (background) {
            textInputLayout.setBackgroundTintList(colorState);
            textInputLayout.setDefaultHintTextColor(colorState);
        } else {
            textInputLayout.setBoxStrokeColor(accentColor);
            textInputLayout.setDefaultHintTextColor(colorState);
        }
    }
}
