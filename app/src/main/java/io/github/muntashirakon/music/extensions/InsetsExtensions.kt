package io.github.muntashirakon.music.extensions

import androidx.core.view.WindowInsetsCompat
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.RetroUtil

fun WindowInsetsCompat?.safeGetBottomInsets(): Int {
    return if (PreferenceUtil.isFullScreenMode) {
        return 0
    } else {
        this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: RetroUtil.navigationBarHeight
    }
}
