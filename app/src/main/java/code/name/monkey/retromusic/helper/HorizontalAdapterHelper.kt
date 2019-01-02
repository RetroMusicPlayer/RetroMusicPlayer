package code.name.monkey.retromusic.helper

import android.content.Context
import android.view.ViewGroup
import code.name.monkey.retromusic.R


object HorizontalAdapterHelper {

    const val LAYOUT_RES = R.layout.item_image

    const val TYPE_FIRST = 1
    const val TYPE_MIDDLE = 2
    const val TYPE_LAST = 3

    fun applyMarginToLayoutParams(context: Context,
                                  layoutParams: ViewGroup.MarginLayoutParams, viewType: Int) {
        val listMargin = context.resources
                .getDimensionPixelSize(R.dimen.now_playing_top_margin)
        if (viewType == TYPE_FIRST) {
            layoutParams.leftMargin = listMargin
        } else if (viewType == TYPE_LAST) {
            layoutParams.rightMargin = listMargin
        }
    }

    fun getItemViewtype(position: Int, itemCount: Int): Int {
        return if (position == 0) {
            TYPE_FIRST
        } else if (position == itemCount - 1) {
            TYPE_LAST
        } else {
            TYPE_MIDDLE
        }
    }
}
