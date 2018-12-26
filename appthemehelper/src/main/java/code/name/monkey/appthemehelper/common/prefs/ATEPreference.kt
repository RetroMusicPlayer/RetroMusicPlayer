package code.name.monkey.appthemehelper.common.prefs

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.Preference
import android.util.AttributeSet

import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEPreference @TargetApi(Build.VERSION_CODES.LOLLIPOP) constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : Preference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        layoutResource = R.layout.ate_preference_custom
    }
}