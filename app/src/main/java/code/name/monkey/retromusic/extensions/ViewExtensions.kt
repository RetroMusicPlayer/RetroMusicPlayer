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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.getSystemService
import androidx.core.view.*
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dev.chrisbanes.insetter.applyInsetter

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes layout: Int): T {
    return LayoutInflater.from(context).inflate(layout, this, false) as T
}

fun View.show() {
    isVisible = true
}

fun View.hide() {
    isVisible = false
}

fun View.hidden() {
    isInvisible = true
}

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
                show()
                bringToFront()
            }
            doOnEnd {
                isGone = (value != 0f)
            }
            start()
        }
}

fun BottomSheetBehavior<*>.peekHeightAnimate(value: Int): Animator {
    return ObjectAnimator.ofInt(this, "peekHeight", value)
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
                    context.getSystemService<InputMethodManager>()
                imm?.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
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

/**
 * This will draw our view above the navigation bar instead of behind it by adding margins.
 */
fun View.drawAboveSystemBars(onlyPortrait: Boolean = true) {
    if (PreferenceUtil.isFullScreenMode) return
    if (onlyPortrait && RetroUtil.isLandscape) return
    applyInsetter {
        type(navigationBars = true) {
            margin()
        }
    }
}

/**
 * This will draw our view above the navigation bar instead of behind it by adding padding.
 */
fun View.drawAboveSystemBarsWithPadding() {
    if (PreferenceUtil.isFullScreenMode) return
    applyInsetter {
        type(navigationBars = true) {
            padding()
        }
    }
}

fun View.drawNextToNavbar() {
    if (PreferenceUtil.isFullScreenMode) return
    applyInsetter {
        type(statusBars = true, navigationBars = true) {
            padding(horizontal = true)
        }
    }
}

fun View.updateMargin(
    @Px left: Int = marginLeft,
    @Px top: Int = marginTop,
    @Px right: Int = marginRight,
    @Px bottom: Int = marginBottom
) {
    (layoutParams as ViewGroup.MarginLayoutParams).updateMargins(left, top, right, bottom)
}

fun View.applyBottomInsets() {
    if (PreferenceUtil.isFullScreenMode) return
    val initialPadding = recordInitialPaddingForView(this)

    ViewCompat.setOnApplyWindowInsetsListener(
        (this)
    ) { v: View, windowInsets: WindowInsetsCompat ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        v.updatePadding(
            bottom = initialPadding.bottom + insets.bottom
        )
        windowInsets
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

data class InitialPadding(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)