package code.name.monkey.retromusic.extensions

import android.content.Context
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

val Context.isLandscape: Boolean get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.isTablet: Boolean get() = resources.configuration.smallestScreenWidthDp >= 600

fun Context.getTintedDrawable(@DrawableRes id: Int, @ColorInt color: Int): Drawable {
    return ContextCompat.getDrawable(this, id)?.tint(color)!!
}