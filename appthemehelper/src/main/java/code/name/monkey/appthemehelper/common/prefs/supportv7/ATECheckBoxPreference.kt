package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import android.util.AttributeSet
import androidx.preference.CheckBoxPreference
import code.name.monkey.appthemehelper.R


/**
 * @author Aidan Follestad (afollestad)
 */
class ATECheckBoxPreference : CheckBoxPreference {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        layoutResource = R.layout.ate_preference_custom_support
        widgetLayoutResource = R.layout.ate_preference_checkbox
    }
}