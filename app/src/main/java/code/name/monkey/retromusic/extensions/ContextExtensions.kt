package code.name.monkey.retromusic.extensions

import android.content.Context
import androidx.annotation.DimenRes

@Suppress("NOTHING_TO_INLINE")
inline fun Context.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()

@Suppress("NOTHING_TO_INLINE")
inline fun Context.dimen(@DimenRes resource: Int): Int = resources.getDimensionPixelSize(resource)
