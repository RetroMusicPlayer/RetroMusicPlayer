package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.R

class ColorIconsImageView : AppCompatImageView {

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
        // Load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorIconsImageView, 0, 0)
        setIconBackgroundColor(attributes.getColor(R.styleable.ColorIconsImageView_iconBackgroundColor, Color.RED))
        attributes.recycle()
    }

    private fun setIconBackgroundColor(color: Int) {
        setBackgroundResource(R.drawable.color_circle_gradient)
        backgroundTintList = ColorStateList.valueOf(ColorUtil.adjustAlpha(color, 0.3f))
        imageTintList = ColorStateList.valueOf(color)
        requestLayout()
        invalidate()
    }
}
