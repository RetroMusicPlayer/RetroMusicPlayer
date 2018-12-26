package code.name.monkey.appthemehelper.util

import android.content.Context
import android.content.res.ColorStateList

import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

import code.name.monkey.appthemehelper.ThemeStore

object MaterialUtil {

    fun setTint(button: MaterialButton) {
        setTint(button, ThemeStore.accentColor(button.context))
    }

    private fun setTint(button: MaterialButton, accentColor: Int) {
        val context = button.context
        val textColor = ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(accentColor)))
        button.setTextColor(textColor)
    }

    @JvmOverloads
    fun setTint(button: MaterialButton, background: Boolean, color: Int = ThemeStore.accentColor(button.context)) {
        //button.setPadding(48, 48, 48, 48);
        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor = ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))


        if (background) {
            button.backgroundTintList = colorState
            button.setTextColor(textColor)
            button.iconTint = textColor
        } else {
            button.strokeColor = colorState
            button.setTextColor(colorState)
            button.iconTint = colorState
        }

    }

    fun setTint(textInputLayout: TextInputLayout, background: Boolean) {
        val context = textInputLayout.context
        val accentColor = ThemeStore.accentColor(context)
        val colorState = ColorStateList.valueOf(accentColor)

        if (background) {
            textInputLayout.backgroundTintList = colorState
            textInputLayout.defaultHintTextColor = colorState
        } else {
            textInputLayout.boxStrokeColor = accentColor
            textInputLayout.defaultHintTextColor = colorState
        }
    }
}
