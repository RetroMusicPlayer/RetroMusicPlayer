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
package io.github.muntashirakon.music.extensions

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.CheckBox
import android.widget.SeekBar
import androidx.annotation.AttrRes
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import io.github.muntashirakon.music.App
import io.github.muntashirakon.music.R

fun Int.ripAlpha(): Int {
    return ColorUtil.stripAlpha(this)
}

fun Dialog.colorControlNormal() = resolveColor(android.R.attr.colorControlNormal)

fun Toolbar.backgroundTintList() {
    val surfaceColor = ATHUtil.resolveColor(context, R.attr.colorSurface, Color.BLACK)
    val colorStateList = ColorStateList.valueOf(surfaceColor)
    backgroundTintList = colorStateList
}

fun Context.accentColor() = ThemeStore.accentColor(this)

fun Fragment.accentColor() = ThemeStore.accentColor(requireContext())

fun Context.surfaceColor() = resolveColor(R.attr.colorSurface, Color.WHITE)

fun Fragment.surfaceColor() = resolveColor(R.attr.colorSurface, Color.WHITE)

fun Context.textColorSecondary() = resolveColor(android.R.attr.textColorSecondary)

fun Fragment.textColorSecondary() = resolveColor(android.R.attr.textColorSecondary)

fun Context.colorControlNormal() = resolveColor(android.R.attr.colorControlNormal)

fun Fragment.colorControlNormal() = resolveColor(android.R.attr.colorControlNormal)

fun Context.textColorPrimary() = resolveColor(android.R.attr.textColorPrimary)

fun Fragment.textColorPrimary() = resolveColor(android.R.attr.textColorPrimary)

fun Context.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(this, attr, fallBackColor)

fun Fragment.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(requireContext(), attr, fallBackColor)

fun Dialog.resolveColor(@AttrRes attr: Int, fallBackColor: Int = 0) =
    ATHUtil.resolveColor(context, attr, fallBackColor)

fun CheckBox.addAccentColor() {
    buttonTintList = ColorStateList.valueOf(ThemeStore.accentColor(context))
}

fun SeekBar.addAccentColor() {
    val colorState = ColorStateList.valueOf(ThemeStore.accentColor(context))
    progressTintList = colorState
    thumbTintList = colorState
}

fun Button.accentTextColor() = setTextColor(ThemeStore.accentColor(App.getContext()))

fun MaterialButton.accentBackgroundColor() {
    backgroundTintList = ColorStateList.valueOf(ThemeStore.accentColor(App.getContext()))
}

fun MaterialButton.accentOutlineColor() {
    val color = ThemeStore.accentColor(context)
    val colorStateList = ColorStateList.valueOf(color)
    iconTint = colorStateList
    strokeColor = colorStateList
    setTextColor(colorStateList)
    rippleColor = colorStateList
}

fun SeekBar.applyColor(@ColorInt color: Int) {
    thumbTintList = ColorStateList.valueOf(color)
    progressTintList = ColorStateList.valueOf(color)
    progressBackgroundTintList = ColorStateList.valueOf(color)
}

fun ExtendedFloatingActionButton.accentColor() {
    val color = ThemeStore.accentColor(context)
    val textColor = MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color))
    val colorStateList = ColorStateList.valueOf(color)
    val textColorStateList = ColorStateList.valueOf(textColor)
    backgroundTintList = colorStateList
    setTextColor(textColorStateList)
    iconTint = textColorStateList
}

fun FloatingActionButton.accentColor() {
    val color = ThemeStore.accentColor(context)
    val textColor = MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color))
    val colorStateList = ColorStateList.valueOf(color)
    val textColorStateList = ColorStateList.valueOf(textColor)
    backgroundTintList = colorStateList
    imageTintList = textColorStateList
}

fun MaterialButton.applyColor(color: Int) {
    val backgroundColorStateList = ColorStateList.valueOf(color)
    val textColorColorStateList = ColorStateList.valueOf(
        MaterialValueHelper.getPrimaryTextColor(
            context,
            ColorUtil.isColorLight(color)
        )
    )
    backgroundTintList = backgroundColorStateList
    setTextColor(textColorColorStateList)
    iconTint = textColorColorStateList
}

fun MaterialButton.accentColor() = applyColor(ThemeStore.accentColor(context))

fun MaterialButton.applyOutlineColor(color: Int) {
    val textColorColorStateList = ColorStateList.valueOf(color)
    setTextColor(textColorColorStateList)
    iconTint = textColorColorStateList
}

fun TextInputLayout.accentColor() {
    val accentColor = ThemeStore.accentColor(context)
    val colorState = ColorStateList.valueOf(accentColor)
    boxStrokeColor = accentColor
    defaultHintTextColor = colorState
    isHintAnimationEnabled = true
}

fun CircularProgressIndicator.accentColor() {
    val color = ThemeStore.accentColor(context)
    setIndicatorColor(color)
    trackColor = ColorUtil.withAlpha(color, 0.2f)
}

fun CircularProgressIndicator.applyColor(color: Int) {
    setIndicatorColor(color)
    trackColor = ColorUtil.withAlpha(color, 0.2f)
}

fun AppCompatImageView.accentColor(): Int = ThemeStore.accentColor(context)

@CheckResult
fun Drawable.tint(@ColorInt color: Int): Drawable {
    val tintedDrawable = DrawableCompat.wrap(this).mutate()
    DrawableCompat.setTint(this, color)
    return tintedDrawable
}

@CheckResult
fun Drawable.tint(context: Context, @ColorRes color: Int): Drawable =
    tint(context.getColorCompat(color))

@ColorInt
fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}
