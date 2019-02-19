package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.util.AttributeSet
import androidx.preference.EditTextPreference


/**
 * @author Aidan Follestad (afollestad)
 */
class ATEEditTextPreference : EditTextPreference {

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
        layoutResource = code.name.monkey.appthemehelper.R.layout.ate_preference_custom
    }
}
