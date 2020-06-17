package io.github.muntashirakon.music.util.theme

import android.content.Context
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.generalThemeValue
import io.github.muntashirakon.music.util.theme.ThemeMode.*

object ThemeManager {

    @StyleRes
    fun getThemeResValue(
        context: Context
    ): Int = when (context.generalThemeValue) {
        LIGHT -> R.style.Theme_RetroMusic_Light
        DARK -> R.style.Theme_RetroMusic_Base
        BLACK -> R.style.Theme_RetroMusic_Black
        AUTO -> R.style.Theme_RetroMusic_FollowSystem
    }

    fun getNightMode(
        context: Context
    ): Int = when (context.generalThemeValue) {
        LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        DARK,
        BLACK -> AppCompatDelegate.MODE_NIGHT_YES
        AUTO -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
}

