package code.name.monkey.retromusic.extensions

import androidx.core.view.WindowInsetsCompat
import code.name.monkey.retromusic.util.PreferenceUtil

fun WindowInsetsCompat?.safeGetBottomInsets(): Int {
    return if (PreferenceUtil.isFullScreenMode) {
        return 0
    } else {
        this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: 0
    }
}
