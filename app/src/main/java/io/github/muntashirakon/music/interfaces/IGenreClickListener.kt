package io.github.muntashirakon.music.interfaces

import android.view.View
import io.github.muntashirakon.music.model.Genre

interface IGenreClickListener {
    fun onClickGenre(genre: Genre, view: View)
}