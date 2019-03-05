package code.name.monkey.appthemehelper.common.prefs.supportv7

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import android.util.AttributeSet
import android.view.View

import code.name.monkey.appthemehelper.R
import code.name.monkey.appthemehelper.common.prefs.BorderCircleView

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEColorPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : Preference(context, attrs, defStyleAttr) {

    private var mView: View? = null
    private var color: Int = 0
    private var border: Int = 0

    constructor(context: Context) : this(context, null, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        init()
    }

    init {
        init()

    }

    private fun init() {
        layoutResource = R.layout.ate_preference_custom_support
        widgetLayoutResource = R.layout.ate_preference_color
        isPersistent = false
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        mView = holder.itemView
        invalidateColor()
    }

    fun setColor(color: Int, border: Int) {
        this.color = color
        this.border = border
        invalidateColor()
    }

    private fun invalidateColor() {
        if (mView != null) {
            val circle = mView!!.findViewById<View>(R.id.circle) as BorderCircleView
            if (this.color != 0) {
                circle.visibility = View.VISIBLE
                circle.setBackgroundColor(color)
                circle.setBorderColor(border)
            } else {
                circle.visibility = View.GONE
            }
        }
    }
}