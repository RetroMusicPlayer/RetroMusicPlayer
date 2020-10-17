package code.name.monkey.retromusic.interfaces

import android.view.View
import code.name.monkey.retromusic.model.Genre

interface IGenreClickListener {
    fun onClickGenre(genre: Genre, view: View)
}