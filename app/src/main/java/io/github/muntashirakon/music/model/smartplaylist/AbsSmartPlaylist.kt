package io.github.muntashirakon.music.model.smartplaylist

import androidx.annotation.DrawableRes
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.model.AbsCustomPlaylist

abstract class AbsSmartPlaylist(
    name: String = "",
    @DrawableRes val iconRes: Int = R.drawable.ic_queue_music
) : AbsCustomPlaylist(-Math.abs(31 * name.hashCode() + iconRes * name.hashCode() * 31 * 31), name)