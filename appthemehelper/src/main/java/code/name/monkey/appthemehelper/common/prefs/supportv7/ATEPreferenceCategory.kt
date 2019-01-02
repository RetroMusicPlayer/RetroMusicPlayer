package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceViewHolder
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore

class ATEPreferenceCategory : PreferenceCategory {

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


    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val mTitle = holder.itemView as TextView
        mTitle.setTextColor(ThemeStore.accentColor(context))
    }

    private fun init() {
        layoutResource = R.layout.ate_preference_category
    }
}
