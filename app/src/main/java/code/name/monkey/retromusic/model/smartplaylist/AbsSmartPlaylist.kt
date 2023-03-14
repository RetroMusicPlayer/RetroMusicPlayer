package code.name.monkey.retromusic.model.smartplaylist

import androidx.annotation.DrawableRes
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.AbsCustomPlaylist

abstract class AbsSmartPlaylist(
    name: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_queue_music
) : AbsCustomPlaylist(
    id = PlaylistIdGenerator(name, iconRes),
    name = name
)