package code.name.monkey.retromusic.helper

import android.content.Context
import android.view.ViewGroup
import code.name.monkey.retromusic.R

object HorizontalAdapterHelper {

    val LAYOUT_RES
        get() = R.layout.item_image

    private const val TYPE_FIRST = 1
    private const val TYPE_MIDDLE = 2
    private const val TYPE_LAST = 3

    fun applyMarginToLayoutParams(
        context: Context,
        layoutParams: ViewGroup.MarginLayoutParams,
        viewType: Int
    ) {
        val listMargin = context.resources
            .getDimensionPixelSize(R.dimen.now_playing_top_margin)
        if (viewType == TYPE_FIRST) {
            layoutParams.leftMargin = listMargin
        } else if (viewType == TYPE_LAST) {
            layoutParams.rightMargin = listMargin
        }
    }

    fun getItemViewType(position: Int, itemCount: Int): Int {
        return when (position) {
            0 -> TYPE_FIRST
            itemCount - 1 -> TYPE_LAST
            else -> TYPE_MIDDLE
        }
    }
}
