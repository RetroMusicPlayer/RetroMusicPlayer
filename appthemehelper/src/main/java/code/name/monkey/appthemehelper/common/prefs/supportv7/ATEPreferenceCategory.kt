package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import android.util.AttributeSet
import android.widget.TextView

import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore

class ATEPreferenceCategory : PreferenceCategory {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val mTitle = holder.itemView as TextView
        mTitle.setTextColor(ThemeStore.accentColor(context))
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        layoutResource = R.layout.ate_preference_category
    }
}
