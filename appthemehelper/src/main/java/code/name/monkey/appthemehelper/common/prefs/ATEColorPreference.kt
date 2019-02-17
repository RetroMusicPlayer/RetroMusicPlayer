package code.name.monkey.appthemehelper.common.prefs

import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import android.view.View
import code.name.monkey.appthemehelper.R

/**
 * @author Aidan Follestad (afollestad)
 */
class ATEColorPreference : Preference {

    private var mView: View? = null
    private var color: Int = 0
    private var border: Int = 0

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
        widgetLayoutResource = R.layout.ate_preference_color
        isPersistent = false
    }

    override fun onBindView(view: View) {
        super.onBindView(view)
        mView = view
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