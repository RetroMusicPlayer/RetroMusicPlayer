package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.preference.PreferenceCategory
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import code.name.monkey.appthemehelper.ThemeStore

class ATEPreferenceCategory : PreferenceCategory {
    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {

    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        val mTitle = view.findViewById<View>(android.R.id.title) as TextView
        mTitle.setTextColor(ThemeStore.accentColor(view.context))
    }
}
