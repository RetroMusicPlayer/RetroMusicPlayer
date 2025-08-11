package code.name.monkey.retromusic.model

import androidx.annotation.StringRes
import code.name.monkey.retromusic.HomeSection

data class Home(
    val arrayList: List<Any>,
    @HomeSection
    val homeSection: Int,
    @StringRes
    val titleRes: Int
)