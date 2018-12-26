package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import androidx.preference.ListPreference
import android.util.AttributeSet

import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEListPreference : ListPreference {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        layoutResource = R.layout.ate_preference_custom_support
        if (summary == null || summary.toString().trim { it <= ' ' }.isEmpty())
            summary = "%s"
    }
}
