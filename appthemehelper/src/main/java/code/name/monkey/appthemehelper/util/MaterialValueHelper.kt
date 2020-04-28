package code.name.monkey.appthemehelper.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

import code.name.monkey.appthemehelper.R

object MaterialValueHelper {

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getPrimaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.primary_text_default_material_light)
        } else ContextCompat.getColor(context!!, R.color.primary_text_default_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getSecondaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.secondary_text_default_material_light)
        } else ContextCompat.getColor(context!!, R.color.secondary_text_default_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getPrimaryDisabledTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.primary_text_disabled_material_light)
        } else ContextCompat.getColor(context!!, R.color.primary_text_disabled_material_dark)
    }

    @SuppressLint("PrivateResource")
    @JvmStatic
    @ColorInt
    fun getSecondaryDisabledTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.secondary_text_disabled_material_light)
        } else ContextCompat.getColor(context!!, R.color.secondary_text_disabled_material_dark)
    }
}
