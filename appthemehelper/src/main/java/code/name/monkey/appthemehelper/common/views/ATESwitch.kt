package code.name.monkey.appthemehelper.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore


/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitch : SwitchCompat {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        setThumbResource(R.drawable.toggle_switch)
        setTrackResource(R.drawable.ate_track)
        background = null
        ATH.setTint(this, ThemeStore.accentColor(context))
    }

    override fun isShown(): Boolean {
        return parent != null && visibility == View.VISIBLE
    }
}
