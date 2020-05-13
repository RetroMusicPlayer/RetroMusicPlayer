package code.name.monkey.retromusic.fragments

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import code.name.monkey.retromusic.R


enum class AlbumCoverStyle(
    @param:StringRes @field:StringRes
    val titleRes: Int,
    @param:DrawableRes @field:DrawableRes
    val drawableResId: Int, val id: Int
) {
    CARD(R.string.card, R.drawable.np_blur_card, 4),
    CIRCLE(R.string.circular, R.drawable.np_circle, 2),
    FLAT(R.string.flat, R.drawable.np_flat, 1),
    FULL_CARD(R.string.full_card, R.drawable.np_adaptive, 6),
    FULL(R.string.full, R.drawable.np_full, 5),
    MATERIAL(R.string.material, R.drawable.np_material, 3),
    NORMAL(R.string.normal, R.drawable.np_normal, 0),
}
