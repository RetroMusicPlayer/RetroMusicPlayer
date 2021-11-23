package code.name.monkey.retromusic.views.insets

import android.content.Context
import android.util.AttributeSet
import android.view.WindowInsets
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.WindowInsetsCompat
import code.name.monkey.retromusic.extensions.drawAboveSystemBarsWithPadding
import code.name.monkey.retromusic.extensions.recordInitialPaddingForView
import code.name.monkey.retromusic.extensions.requestApplyInsetsWhenAttached
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialdialogs.utils.MDUtil.updatePadding

class InsetsConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    init {
        if (!RetroUtil.isLandscape())
            drawAboveSystemBarsWithPadding()
    }
}