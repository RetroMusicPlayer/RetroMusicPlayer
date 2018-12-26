package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.util.AttributeSet

import com.afollestad.materialdialogs.prefs.MaterialListPreference
import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEMultiSelectPreference(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : MaterialListPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init()
    }

    private fun init() {
        layoutResource = R.layout.ate_preference_custom
    }
}
