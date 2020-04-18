package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import android.view.View

class StatusBarView(
    context: Context,
    attrs: AttributeSet
) : View(context, attrs) {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (isInEditMode){
            return
        }
        setOnApplyWindowInsetsListener { _, insets ->
            val height = insets?.systemWindowInsetTop ?: 0
            setHeight(height)
            insets
        }
    }

    private fun setHeight(px: Int) {
        val params = layoutParams ?: return
        params.height = px
        layoutParams = params
    }

}