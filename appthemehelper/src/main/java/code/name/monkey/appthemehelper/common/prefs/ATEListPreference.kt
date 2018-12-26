package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.util.AttributeSet

import com.afollestad.materialdialogs.prefs.MaterialListPreference
import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEListPreference : MaterialListPreference {

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
        layoutResource = R.layout.ate_preference_custom
        if (summary == null || summary.toString().trim { it <= ' ' }.isEmpty())
            summary = "%s"
    }
}
