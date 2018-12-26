package code.name.monkey.appthemehelper.common.views

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView

import android.text.Layout
import android.util.AttributeSet

import code.name.monkey.appthemehelper.ThemeStore


/**
 * @author Aidan Follestad (afollestad)
 */
class ATEPrimaryTextView : AppCompatTextView {

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
        setTextColor(ThemeStore.textColorPrimary(context))
    }
}
