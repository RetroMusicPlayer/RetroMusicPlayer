package code.name.monkey.retromusic.util

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.PowerManager
import androidx.annotation.StyleRes
import code.name.monkey.retromusic.R

/**
 * @author Paolo Valerdi
 */
object ThemeManager {

    @StyleRes
    fun getThemeResValue(context: Context): Int = when (PreferenceUtil.getInstance(context).generalThemeValue) {
        "light" -> R.style.Theme_RetroMusic_Light
        "auto" -> if (isSystemDarkModeEnabled(context)) R.style.Theme_RetroMusic else R.style.Theme_RetroMusic_Light
        "black" -> R.style.Theme_RetroMusic_Black
        else -> R.style.Theme_RetroMusic
        /**
         * To add a toggle for amoled theme just add an if statement such as
         * if(PreferenceUtil.getInstance(context).useAmoled) blablabla
         */
    }

    private fun isSystemDarkModeEnabled(context: Context): Boolean {
        val isBatterySaverEnabled = (context.getSystemService(Context.POWER_SERVICE) as PowerManager?)?.isPowerSaveMode
                ?: false
        val isDarkModeEnabled = (context.resources.configuration.uiMode and UI_MODE_NIGHT_MASK) == UI_MODE_NIGHT_YES

        return isBatterySaverEnabled or isDarkModeEnabled
    }

}