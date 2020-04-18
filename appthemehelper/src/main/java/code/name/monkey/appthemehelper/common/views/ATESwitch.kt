package code.name.monkey.appthemehelper.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.ThemeStore

/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitch @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SwitchCompat(context, attrs, defStyleAttr) {

    init {
        ATH.setTint(this, ThemeStore.accentColor(getContext()))
    }

    override fun isShown(): Boolean {
        return parent != null && visibility == View.VISIBLE
    }
}
