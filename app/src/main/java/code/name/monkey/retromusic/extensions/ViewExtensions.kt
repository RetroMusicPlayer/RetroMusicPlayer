/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.extensions

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.*
import androidx.core.view.WindowInsetsCompat.Type.navigationBars
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.utils.MDUtil.updatePadding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes layout: Int): T {
    return LayoutInflater.from(context).inflate(layout, this, false) as T
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.hidden() {
    visibility = View.INVISIBLE
}

fun View.showOrHide(show: Boolean) = if (show) show() else hide()

fun EditText.appHandleColor(): EditText {
    if (PreferenceUtil.materialYou) return this
    TintHelper.colorHandles(this, ThemeStore.accentColor(context))
    return this
}

fun View.translateYAnimate(value: Float): Animator {
    return ObjectAnimator.ofFloat(this, "translationY", value)
        .apply {
            duration = 300
            doOnStart {
                if (value == 0f) {
                    show()
                }
            }
            doOnEnd {
                if (value != 0f) {
                    hide()
                }
            }
            start()
        }
}

fun BottomSheetBehavior<*>.peekHeightAnimate(value: Int) {
    ObjectAnimator.ofInt(this, "peekHeight", value)
        .apply {
            duration = 300
            start()
        }
}

fun View.focusAndShowKeyboard() {
    /**
     * This is to be called when the window already has focus.
     */
    fun View.showTheKeyboardNow() {
        if (isFocused) {
            post {
                // We still post the call, just in case we are being notified of the windows focus
                // but InputMethodManager didn't get properly setup yet.
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }

    requestFocus()
    if (hasWindowFocus()) {
        // No need to wait for the window to get focus.
        showTheKeyboardNow()
    } else {
        // We need to wait until the window gets focus.
        viewTreeObserver.addOnWindowFocusChangeListener(
            object : ViewTreeObserver.OnWindowFocusChangeListener {
                override fun onWindowFocusChanged(hasFocus: Boolean) {
                    // This notification will arrive just before the InputMethodManager gets set up.
                    if (hasFocus) {
                        this@focusAndShowKeyboard.showTheKeyboardNow()
                        // It’s very important to remove this listener once we are done.
                        viewTreeObserver.removeOnWindowFocusChangeListener(this)
                    }
                }
            })
    }
}

fun ShapeableImageView.setCircleShape(boolean: Boolean) {
    addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        val radius = width / 2f
        shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(radius)
    }
}


/**
 * This will draw our view above the navigation bar instead of behind it by adding margins.
 */
fun View.drawAboveNavBar() {
    // Create a snapshot of the view's margin state
    val initialMargin = recordInitialMarginForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(
        (this)
    ) { v: View, insets: WindowInsetsCompat ->
        v.updateLayoutParams<MarginLayoutParams> {
            bottomMargin = initialMargin.bottom +
                insets.getInsets(navigationBars()).bottom
        }
        insets
    }
}

/**
 * This will draw our view above the navigation bar instead of behind it by adding padding.
 */
fun View.drawAboveNavBarWithPadding() {
    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(
        (this)
    ) { v: View, insets: WindowInsetsCompat ->
        val navBarHeight = insets.getInsets(navigationBars()).bottom
        v.updatePadding(bottom = initialPadding.bottom + navBarHeight)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        // We're already attached, just request as normal
        requestApplyInsets()
    } else {
        // We're not attached to the hierarchy, add a listener to
        // request when we are
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.removeOnAttachStateChangeListener(this)
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}


data class InitialMargin(val left: Int, val top: Int,
                         val right: Int, val bottom: Int)

private fun recordInitialMarginForView(view: View) = InitialMargin(
    view.marginLeft, view.marginTop, view.marginRight, view.marginBottom)



data class InitialPadding(val left: Int, val top: Int,
                         val right: Int, val bottom: Int)

private fun recordInitialPaddingForView(view: View) = InitialMargin(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom)
