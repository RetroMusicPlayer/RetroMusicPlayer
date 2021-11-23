package code.name.monkey.retromusic.views.insets

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.Px
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.extensions.drawAboveSystemBarsWithPadding
import code.name.monkey.retromusic.util.RetroUtil

class InsetsRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    init {
        if (!RetroUtil.isLandscape())
            drawAboveSystemBarsWithPadding()
    }

    fun updatePadding(
        @Px left: Int = paddingLeft,
        @Px top: Int = paddingTop,
        @Px right: Int = paddingRight,
        @Px bottom: Int = paddingBottom
    ) {
        setPadding(left, top, right, bottom)
        if (!RetroUtil.isLandscape())
            drawAboveSystemBarsWithPadding()
    }
}