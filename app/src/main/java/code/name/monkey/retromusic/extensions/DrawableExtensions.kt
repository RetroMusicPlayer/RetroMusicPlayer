package code.name.monkey.retromusic.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.toBitmap

fun Context.scaledDrawableResources(
    @DrawableRes id: Int,
    @DimenRes width: Int,
    @DimenRes height: Int,
): Drawable {
    val w = resources.getDimension(width).toInt()
    val h = resources.getDimension(height).toInt()
    return scaledDrawable(id, w, h)
}

fun Context.scaledDrawable(@DrawableRes id: Int, width: Int, height: Int): Drawable {
    val bmp = BitmapFactory.decodeResource(resources, id)
    val bmpScaled = Bitmap.createScaledBitmap(bmp, width, height, false)
    return BitmapDrawable(resources, bmpScaled)
}

fun Drawable.toBitmap(scaleFactor: Float, config: Bitmap.Config? = null): Bitmap {
    return toBitmap((intrinsicHeight*scaleFactor).toInt(), (intrinsicWidth*scaleFactor).toInt(), config)
}

fun Drawable.getBitmapDrawable(): Bitmap {
    val bmp = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bmp)
    draw(canvas)
    return bmp
}