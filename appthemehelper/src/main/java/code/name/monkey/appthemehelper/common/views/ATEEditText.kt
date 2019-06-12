package code.name.monkey.appthemehelper.common.views

import android.content.Context
import android.util.AttributeSet
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ThemeStore
import com.google.android.material.textfield.TextInputEditText


/**
 * @author Aidan Follestad (afollestad)
 */
class ATEEditText : TextInputEditText {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        ATH.setTint(this, ThemeStore.accentColor(context))
        setTextColor(ThemeStore.textColorPrimary(context))
    }
}
