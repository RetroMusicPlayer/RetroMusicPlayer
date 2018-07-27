package code.name.monkey.appthemehelper.util;

import android.content.Context;
import android.content.res.ColorStateList;

import com.afollestad.materialdialogs.internal.ThemeSingleton;
import code.name.monkey.appthemehelper.ThemeStore;


public final class MaterialDialogsUtil {

    public static void updateMaterialDialogsThemeSingleton(Context context) {
        final ThemeSingleton md = ThemeSingleton.get();
        md.titleColor = ThemeStore.textColorPrimary(context);
        md.contentColor = ThemeStore.textColorSecondary(context);
        md.itemColor = md.titleColor;
        md.widgetColor = ThemeStore.accentColor(context);
        md.linkColor = ColorStateList.valueOf(md.widgetColor);
        md.positiveColor = ColorStateList.valueOf(md.widgetColor);
        md.neutralColor = ColorStateList.valueOf(md.widgetColor);
        md.negativeColor = ColorStateList.valueOf(md.widgetColor);
        md.darkTheme = ATHUtil.isWindowBackgroundDark(context);
    }

    private MaterialDialogsUtil() {
    }
}
