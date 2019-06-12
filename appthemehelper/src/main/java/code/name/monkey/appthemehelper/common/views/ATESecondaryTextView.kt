package code.name.monkey.appthemehelper.common.views

import android.content.Context
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet

import code.name.monkey.appthemehelper.ThemeStore

/**
 * @author Aidan Follestad (afollestad)
 */
class ATESecondaryTextView : AppCompatTextView {

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
        setTextColor(ThemeStore.textColorSecondary(context))
    }
}
