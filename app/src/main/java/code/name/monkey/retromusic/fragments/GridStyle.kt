package code.name.monkey.retromusic.fragments

import androidx.annotation.LayoutRes
import code.name.monkey.retromusic.R

enum class GridStyle constructor(
    @param:LayoutRes @field:LayoutRes val layoutResId: Int,
    val id: Int
) {
    Grid(R.layout.item_grid, 0),
    Card(R.layout.item_card, 1),
    ColoredCard(R.layout.item_card_color, 2),
    Circular(R.layout.item_grid_circle, 3),
    Image(R.layout.image, 4),
    GradientImage(R.layout.item_image_gradient, 5)
}