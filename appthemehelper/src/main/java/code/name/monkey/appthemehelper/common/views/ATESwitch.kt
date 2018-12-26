package code.name.monkey.appthemehelper.common.views

import android.content.Context
import androidx.appcompat.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View

import code.name.monkey.appthemehelper.ATH
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
        ATH.setTint(this, ThemeStore.accentColor(context))
    }

    override fun isShown(): Boolean {
        return parent != null && visibility == View.VISIBLE
    }
}
