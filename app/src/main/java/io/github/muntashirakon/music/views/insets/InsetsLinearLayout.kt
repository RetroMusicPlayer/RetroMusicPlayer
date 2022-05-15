package io.github.muntashirakon.music.views.insets

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import io.github.muntashirakon.music.extensions.drawAboveSystemBarsWithPadding
import io.github.muntashirakon.music.util.RetroUtil

class InsetsLinearLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        if (!RetroUtil.isLandscape)
            drawAboveSystemBarsWithPadding()
    }
}