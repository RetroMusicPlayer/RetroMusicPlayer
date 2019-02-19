package code.name.monkey.appthemehelper.common.prefs

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.preference.Preference
import android.preference.SwitchPreference
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.appcompat.widget.SwitchCompat

import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.views.ATESwitch

import java.lang.reflect.Field

/**
 * @author Aidan Follestad (afollestad)
 */
class ATESwitchPreference : SwitchPreference {

    private var mSwitch: ATESwitch? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        layoutResource = R.layout.ate_preference_custom
        if (COMPAT_MODE) {
            widgetLayoutResource = R.layout.ate_preference_switch
        } else {
            try {
                val canRecycleLayoutField = Preference::class.java.getDeclaredField("mCanRecycleLayout")
                canRecycleLayoutField.isAccessible = true
                canRecycleLayoutField.setBoolean(this, true)
            } catch (ignored: Exception) {
            }

            try {
                val hasSpecifiedLayout = Preference::class.java.getDeclaredField("mHasSpecifiedLayout")
                hasSpecifiedLayout.isAccessible = true
                hasSpecifiedLayout.setBoolean(this, true)
            } catch (ignored: Exception) {
            }

        }
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        if (COMPAT_MODE) {
            mSwitch = view.findViewById<View>(R.id.switchWidget) as ATESwitch
            mSwitch!!.isChecked = isChecked
        } else {
            val parentSwitch = findSwitchView(view)
            if (parentSwitch != null) {
                ATH.setTint(parentSwitch, ThemeStore.accentColor(view.context))
            }
        }
    }

    private fun findSwitchView(view: View): View? {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is Switch || child is SwitchCompat) {
                    return child
                } else if (child is ViewGroup) {
                    val potentialSwitch = findSwitchView(child)
                    if (potentialSwitch != null) return potentialSwitch
                }
            }
        } else if (view is Switch || view is SwitchCompat) {
            return view
        }
        return null
    }

    override fun setChecked(checked: Boolean) {
        super.setChecked(checked)
        if (COMPAT_MODE) {
            if (mSwitch != null) {
                mSwitch!!.isChecked = checked
            }
        }
    }

    companion object {

        internal val COMPAT_MODE = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    }
}
