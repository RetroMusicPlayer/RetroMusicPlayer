package code.name.monkey.retromusic.util.theme

import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.generalThemeValue
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.theme.ThemeMode.*

object ThemeManager {

    @StyleRes
    fun getThemeResValue(): Int =
        if (PreferenceUtil.materialYou) {
            R.style.Theme_RetroMusic_MD3
        } else {
            when (App.getContext().generalThemeValue) {
                LIGHT -> R.style.Theme_RetroMusic_Light
                DARK -> R.style.Theme_RetroMusic_Base
                BLACK -> R.style.Theme_RetroMusic_Black
                AUTO -> R.style.Theme_RetroMusic_FollowSystem
            }
        }

    fun getNightMode(): Int = when (App.getContext().generalThemeValue) {
        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        DARK -> AppCompatDelegate.MODE_NIGHT_YES
        BLACK -> {
            if (PreferenceUtil.baseTheme == "dark") {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }
        else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}