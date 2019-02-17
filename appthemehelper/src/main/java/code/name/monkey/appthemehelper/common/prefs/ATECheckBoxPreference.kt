package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.preference.CheckBoxPreference
import android.preference.Preference
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import code.name.monkey.appthemehelper.ATH
import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.ThemeStore

/**
 * @author Aidan Follestad (afollestad)
 */
class ATECheckBoxPreference : CheckBoxPreference {

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


    private fun init() {
        layoutResource = R.layout.ate_preference_custom

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

    override fun onBindView(view: View) {
        super.onBindView(view)

        val parentCheckbox = findCheckboxView(view)
        if (parentCheckbox != null) {
            ATH.setTint(parentCheckbox, ThemeStore.accentColor(view.context))
        }
    }

    private fun findCheckboxView(view: View): View? {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                if (child is CheckBox) {
                    return child
                } else if (child is ViewGroup) {
                    val potentialCheckbox = findCheckboxView(child)
                    if (potentialCheckbox != null) return potentialCheckbox
                }
            }
        } else if (view is CheckBox) {
            return view
        }
        return null
    }
}