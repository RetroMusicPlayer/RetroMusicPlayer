package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View

class StatusBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthMeasureSpec), getStatusBarHeight(
                resources
            )
        )
    }

    companion object {
        fun getStatusBarHeight(r: Resources): Int {
            var result = 0
            val resourceId = r.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = r.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}