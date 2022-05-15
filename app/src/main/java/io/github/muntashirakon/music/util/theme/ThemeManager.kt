package io.github.muntashirakon.music.util.theme

import android.content.Context
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatDelegate
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.generalThemeValue
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.theme.ThemeMode.*

@StyleRes
fun Context.getThemeResValue(): Int =
    if (PreferenceUtil.materialYou) {
        if (generalThemeValue == BLACK) R.style.Theme_RetroMusic_MD3_Black
        else R.style.Theme_RetroMusic_MD3
    } else {
        when (generalThemeValue) {
            LIGHT -> R.style.Theme_RetroMusic_Light
            DARK -> R.style.Theme_RetroMusic_Base
            BLACK -> R.style.Theme_RetroMusic_Black
            AUTO -> R.style.Theme_RetroMusic_FollowSystem
        }
    }

fun Context.getNightMode(): Int = when (generalThemeValue) {
    LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
    DARK -> AppCompatDelegate.MODE_NIGHT_YES
    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
}