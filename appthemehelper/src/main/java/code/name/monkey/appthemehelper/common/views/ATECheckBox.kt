package code.name.monkey.appthemehelper.common.views

import android.content.Context
import androidx.appcompat.widget.AppCompatCheckBox
import android.util.AttributeSet

import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ThemeStore


/**
 * @author Aidan Follestad (afollestad)
 */
class ATECheckBox : AppCompatCheckBox {

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
}
