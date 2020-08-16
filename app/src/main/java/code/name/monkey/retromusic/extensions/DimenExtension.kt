package code.name.monkey.retromusic.extensions

import androidx.fragment.app.Fragment

fun Fragment.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}