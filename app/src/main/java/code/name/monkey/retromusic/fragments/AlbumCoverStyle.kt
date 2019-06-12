package code.name.monkey.retromusic.fragments

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import code.name.monkey.retromusic.R


enum class AlbumCoverStyle(@param:StringRes @field:StringRes
                           val titleRes: Int,
                           @param:DrawableRes @field:DrawableRes
                           val drawableResId: Int, val id: Int) {
    NORMAL(R.string.normal, R.drawable.np_normal, 0),
    FLAT(R.string.flat, R.drawable.np_flat, 1),
    CIRCLE(R.string.circular, R.drawable.np_circle, 2),
    MATERIAL(R.string.material, R.drawable.np_material, 3),
    CARD(R.string.card, R.drawable.np_blur_card, 4),
    FULL(R.string.full, R.drawable.np_full, 5),
    FULL_CARD(R.string.full_card, R.drawable.np_adaptive, 6)
}
