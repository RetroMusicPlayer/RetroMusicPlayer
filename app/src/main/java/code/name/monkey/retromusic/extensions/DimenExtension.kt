package code.name.monkey.retromusic.extensions

import android.app.Activity
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.dimToPixel(@DimenRes dimenRes: Int): Int {
    return resources.getDimensionPixelSize(dimenRes)
}

fun Activity.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}

fun Fragment.dipToPix(dpInFloat: Float): Float {
    val scale = resources.displayMetrics.density
    return dpInFloat * scale + 0.5f
}
