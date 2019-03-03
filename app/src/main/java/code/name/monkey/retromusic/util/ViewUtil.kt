/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.retromusic.util

import android.animation.Animator
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.View
import android.view.animation.PathInterpolator
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.view.ViewCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView

object ViewUtil {

    const val RETRO_MUSIC_ANIM_TIME = 1000

    fun createTextColorTransition(v: TextView, @ColorInt startColor: Int, @ColorInt endColor: Int): Animator {
        return createColorAnimator(v, "textColor", startColor, endColor)
    }

    fun createBackgroundColorTransition(v: View, @ColorInt startColor: Int, @ColorInt endColor: Int): Animator {
        return createColorAnimator(v, "backgroundColor", startColor, endColor)
    }

    fun setProgressDrawable(progressSlider: SeekBar, newColor: Int, thumbTint: Boolean = false) {

        if (thumbTint) {
            progressSlider.thumbTintList = ColorStateList.valueOf(newColor)
        }

        if (progressSlider.progressDrawable is LayerDrawable) {
            val ld = progressSlider.progressDrawable as LayerDrawable?

            if (ld != null) {
                val clipDrawableProgress = ld.findDrawableByLayerId(android.R.id.progress)
                clipDrawableProgress.setColorFilter(newColor, PorterDuff.Mode.SRC_IN)

                val clipDrawableBackground = ld.findDrawableByLayerId(android.R.id.background)
                clipDrawableBackground.setColorFilter(MaterialValueHelper.getPrimaryDisabledTextColor(progressSlider.context, ColorUtil.isColorLight(ThemeStore.primaryColor(progressSlider.context))), PorterDuff.Mode.SRC_IN)
            }
        } else {
            progressSlider.progressTintList = ColorStateList.valueOf(newColor)
        }
    }

    fun setProgressDrawable(progressSlider: ProgressBar, newColor: Int) {

        val ld = progressSlider.progressDrawable as LayerDrawable

        val clipDrawableProgress = ld.findDrawableByLayerId(android.R.id.progress)
        clipDrawableProgress.setColorFilter(newColor, PorterDuff.Mode.SRC_IN)

        val clipDrawableBackground = ld.findDrawableByLayerId(android.R.id.background)
        clipDrawableBackground.setColorFilter(MaterialValueHelper.getPrimaryDisabledTextColor(progressSlider.context, ColorUtil.isColorLight(ThemeStore.primaryColor(progressSlider.context))), PorterDuff.Mode.SRC_IN)

    }

    private fun createColorAnimator(target: Any, propertyName: String, @ColorInt startColor: Int, @ColorInt endColor: Int): Animator {
        val animator: ObjectAnimator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator = ObjectAnimator.ofArgb(target, propertyName, startColor, endColor)
        } else {
            animator = ObjectAnimator.ofInt(target, propertyName, startColor, endColor)
            animator.setEvaluator(ArgbEvaluator())
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            animator.interpolator = PathInterpolator(0.4f, 0f, 1f, 1f)
        }
        animator.duration = RETRO_MUSIC_ANIM_TIME.toLong()
        return animator
    }

    fun setStatusBarHeight(context: Context, statusBar: View) {
        val lp = statusBar.layoutParams
        lp.height = getStatusBarHeight(context)
        statusBar.requestLayout()
    }

    fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun hitTest(v: View, x: Int, y: Int): Boolean {
        val tx = (ViewCompat.getTranslationX(v) + 0.5f).toInt()
        val ty = (ViewCompat.getTranslationY(v) + 0.5f).toInt()
        val left = v.left + tx
        val right = v.right + tx
        val top = v.top + ty
        val bottom = v.bottom + ty

        return x >= left && x <= right && y >= top && y <= bottom
    }

    fun setUpFastScrollRecyclerViewColor(context: Context,
                                         recyclerView: FastScrollRecyclerView, accentColor: Int) {
        recyclerView.setPopupBgColor(accentColor)
        recyclerView.setPopupTextColor(
                MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(accentColor)))
        recyclerView.setThumbColor(accentColor)
        recyclerView.setTrackColor(Color.TRANSPARENT)
        //recyclerView.setTrackColor(ColorUtil.withAlpha(ATHUtil.resolveColor(context, R.attr.colorControlNormal), 0.12f));
    }

    fun convertDpToPixel(dp: Float, resources: Resources): Float {
        val metrics = resources.displayMetrics
        return dp * metrics.density
    }
}