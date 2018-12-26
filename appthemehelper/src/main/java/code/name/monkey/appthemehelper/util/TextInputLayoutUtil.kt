package code.name.monkey.appthemehelper.util

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.textfield.TextInputLayout

import java.lang.reflect.Field

/**
 * @author Aidan Follestad (afollestad)
 */
object TextInputLayoutUtil {

    fun setHint(view: TextInputLayout, @ColorInt hintColor: Int) {
        try {
            val mDefaultTextColorField = TextInputLayout::class.java.getDeclaredField("mDefaultTextColor")
            mDefaultTextColorField.isAccessible = true
            mDefaultTextColorField.set(view, ColorStateList.valueOf(hintColor))
        } catch (t: Throwable) {
            throw RuntimeException("Failed to set TextInputLayout hint (collapsed) color: " + t.localizedMessage, t)
        }

    }

    fun setAccent(view: TextInputLayout, @ColorInt accentColor: Int) {
        try {
            val mFocusedTextColorField = TextInputLayout::class.java.getDeclaredField("mFocusedTextColor")
            mFocusedTextColorField.isAccessible = true
            mFocusedTextColorField.set(view, ColorStateList.valueOf(accentColor))
        } catch (t: Throwable) {
            throw RuntimeException("Failed to set TextInputLayout accent (expanded) color: " + t.localizedMessage, t)
        }

    }
}