package code.name.monkey.retromusic.extensions

import androidx.core.view.WindowInsetsCompat
import code.name.monkey.retromusic.util.RetroUtil

fun WindowInsetsCompat?.safeGetBottomInsets(): Int {
    // Get Navbar heights if insets are null
    return (this?.getInsets(WindowInsetsCompat.Type.systemBars())?.bottom ?: RetroUtil.getNavigationBarHeight())
}
