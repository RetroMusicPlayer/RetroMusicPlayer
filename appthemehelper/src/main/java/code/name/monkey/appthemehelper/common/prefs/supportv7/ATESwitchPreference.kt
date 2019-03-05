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
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        layoutResource = R.layout.ate_preference_custom_support
        widgetLayoutResource = R.layout.ate_preference_switch_support
    }
}