package code.name.monkey.appthemehelper.util

import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout


object MaterialUtil {

    @JvmOverloads
    fun setTint(button: MaterialButton, background: Boolean = true,
                color: Int = ThemeStore.accentColor(button.context)) {

        button.isAllCaps = false
        val context = button.context
        val colorState = ColorStateList.valueOf(color)
        val textColor = ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))


        if (background) {
            button.backgroundTintList = colorState
            button.setTextColor(textColor)
            button.iconTint = textColor
        } else {
            button.setTextColor(colorState)
            button.iconTint = colorState
        }

    }

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


    private fun setCursorPointerColor(view: EditText, @ColorInt color: Int) {
        try {
            //get the pointer resource id
            var field = TextView::class.java.getDeclaredField("mTextSelectHandleRes")
            field.isAccessible = true
            val drawableResId = field.getInt(view)

            //get the editor
            field = TextView::class.java.getDeclaredField("mEditor")
            field.isAccessible = true
            val editor = field.get(view)

            //tint drawable
            val drawable = ContextCompat.getDrawable(view.context, drawableResId)!!
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)

            //set the drawable
            field = editor.javaClass.getDeclaredField("mSelectHandleCenter")
            field.isAccessible = true
            field.set(editor, drawable)

        } catch (ex: Exception) {
        }
    }
}
