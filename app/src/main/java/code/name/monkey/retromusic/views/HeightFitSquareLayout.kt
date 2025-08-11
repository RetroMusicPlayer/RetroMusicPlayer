package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class HeightFitSquareLayout  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr){
    private var forceSquare = true

    fun setForceSquare(forceSquare: Boolean) {
        this.forceSquare = forceSquare
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var i = widthMeasureSpec
        if (forceSquare) {
            i = heightMeasureSpec
        }
        super.onMeasure(i, heightMeasureSpec)
    }
}