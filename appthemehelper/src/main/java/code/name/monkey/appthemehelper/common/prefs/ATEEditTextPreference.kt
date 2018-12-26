package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.util.AttributeSet

import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEEditTextPreference(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : MaterialEditTextPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init()
    }

    private fun init() {
        layoutResource = code.name.monkey.appthemehelper.R.layout.ate_preference_custom
    }
}
