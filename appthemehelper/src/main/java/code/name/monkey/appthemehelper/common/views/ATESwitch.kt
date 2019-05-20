package code.name.monkey.appthemehelper.common.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore


/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitch : SwitchCompat {

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
        setThumbResource(R.drawable.toggle_switch)
        setTrackResource(R.drawable.ate_track)
        background = null

        val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_checked), intArrayOf(android.R.attr.state_checked)),
                intArrayOf(ThemeStore.textColorSecondary(context), ThemeStore.accentColor(context)))

        thumbTintList = sl
        trackTintList = sl
    }

    override fun isShown(): Boolean {
        return parent != null && visibility == View.VISIBLE
    }
}
