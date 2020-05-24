package code.name.monkey.appthemehelper.util

import android.content.res.ColorStateList
import code.name.monkey.appthemehelper.ThemeStore
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

object MaterialUtil {

    @JvmOverloads
    @JvmStatic
    fun setTint(
        button: MaterialButton, background: Boolean = true,
        color: Int = ThemeStore.accentColor(button.context)
    ) {

        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor =
            ColorStateList.valueOf(
                MaterialValueHelper.getPrimaryTextColor(
                    context,
                    ColorUtil.isColorLight(color)
                )
            )


        if (background) {
            button.backgroundTintList = colorState
            button.setTextColor(textColor)
            button.iconTint = textColor
        } else {
            button.setTextColor(colorState)
            button.iconTint = colorState
        }
    }

    @JvmOverloads
    @JvmStatic
    fun setTint(textInputLayout: TextInputLayout, background: Boolean = true) {
        val context = textInputLayout.context
        val accentColor = ThemeStore.accentColor(context)
        val colorState = ColorStateList.valueOf(accentColor)

        if (background) {
            textInputLayout.backgroundTintList = colorState
            textInputLayout.defaultHintTextColor = colorState
        } else {
            textInputLayout.boxStrokeColor = accentColor
            textInputLayout.defaultHintTextColor = colorState
            textInputLayout.isHintAnimationEnabled = true
        }
    }
}
