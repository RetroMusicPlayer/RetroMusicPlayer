package code.name.monkey.appthemehelper.util

import android.content.Context
import android.content.res.ColorStateList
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil.isWindowBackgroundDark
import com.afollestad.materialdialogs.internal.ThemeSingleton

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
object MaterialDialogsUtil {
    fun updateMaterialDialogsThemeSingleton(context: Context) {
        val md = ThemeSingleton.get()
        md.titleColor = ThemeStore.textColorPrimary(context)
        md.contentColor = ThemeStore.textColorSecondary(context)
        md.itemColor = md.titleColor
        md.widgetColor = ThemeStore.accentColor(context)
        md.linkColor = ColorStateList.valueOf(md.widgetColor)
        md.positiveColor = ColorStateList.valueOf(md.widgetColor)
        md.neutralColor = ColorStateList.valueOf(md.widgetColor)
        md.negativeColor = ColorStateList.valueOf(md.widgetColor)
        md.darkTheme = isWindowBackgroundDark(context)
    }
}