package io.github.muntashirakon.music.views.insets

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.muntashirakon.music.extensions.drawAboveSystemBarsWithPadding
import io.github.muntashirakon.music.util.RetroUtil

class InsetsConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        if (!RetroUtil.isLandscape)
            drawAboveSystemBarsWithPadding()
    }
}