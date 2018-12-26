package code.name.monkey.appthemehelper.common.prefs

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.PreferenceCategory
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

import code.name.monkey.appthemehelper.ThemeStore

class ATEPreferenceCategory @TargetApi(Build.VERSION_CODES.LOLLIPOP) constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : PreferenceCategory(context, attrs, defStyleAttr, defStyleRes) {

    override fun onBindView(view: View) {
        super.onBindView(view)
        val mTitle = view.findViewById<View>(android.R.id.title) as TextView
        mTitle.setTextColor(ThemeStore.accentColor(view.context))
    }
}
