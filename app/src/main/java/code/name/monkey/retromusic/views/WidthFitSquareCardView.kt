package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet

import com.google.android.material.card.MaterialCardView

/**
 * Created by hemanths on 3/18/19
 */
class WidthFitSquareCardView : MaterialCardView {

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        attrs: AttributeSet
    ) : super(context, attrs)

    constructor(
        context: Context, attrs:
        AttributeSet, defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }
}
