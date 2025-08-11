package code.name.monkey.retromusic.util

import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.View
import android.widget.ProgressBar
import android.widget.SeekBar
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper

object ViewUtil {

    const val RETRO_MUSIC_ANIM_TIME = 1000

    fun setProgressDrawable(progressSlider: SeekBar, newColor: Int, thumbTint: Boolean = false) {

        if (thumbTint) {
            progressSlider.thumbTintList = ColorStateList.valueOf(newColor)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val layerDrawable = progressSlider.progressDrawable as LayerDrawable
            val progressDrawable = layerDrawable.findDrawableByLayerId(android.R.id.progress)
            progressDrawable.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(newColor, SRC_IN)
        } else {
            progressSlider.progressTintList = ColorStateList.valueOf(newColor)
        }
    }


    fun setProgressDrawable(progressSlider: ProgressBar, newColor: Int) {

        val layerDrawable = progressSlider.progressDrawable as LayerDrawable

        val progress = layerDrawable.findDrawableByLayerId(android.R.id.progress)
        progress.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(newColor, SRC_IN)

        val background = layerDrawable.findDrawableByLayerId(android.R.id.background)
        val primaryColor =
            ATHUtil.resolveColor(progressSlider.context, android.R.attr.windowBackground)
        background.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            MaterialValueHelper.getPrimaryDisabledTextColor(
                progressSlider.context,
                ColorUtil.isColorLight(primaryColor)
            ), SRC_IN
        )

        val secondaryProgress = layerDrawable.findDrawableByLayerId(android.R.id.secondaryProgress)
        secondaryProgress?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                ColorUtil.withAlpha(
                    newColor,
                    0.65f
                ), SRC_IN
            )
    }

    fun hitTest(v: View, x: Int, y: Int): Boolean {
        val tx = (v.translationX + 0.5f).toInt()
        val ty = (v.translationY + 0.5f).toInt()
        val left = v.left + tx
        val right = v.right + tx
        val top = v.top + ty
        val bottom = v.bottom + ty

        return x in left..right && y >= top && y <= bottom
    }

    fun convertDpToPixel(dp: Float, resources: Resources): Float {
        val metrics = resources.displayMetrics
        return dp * metrics.density
    }
}