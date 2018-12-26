package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.preference.CheckBoxPreference
import android.util.AttributeSet

import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitchPreference : CheckBoxPreference {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        layoutResource = R.layout.ate_preference_custom_support
        widgetLayoutResource = R.layout.ate_preference_switch_support
    }
}