package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

import java.util.ArrayList

/**
 * @author Hemanth S (h4h13).
 */

interface GenreDetailsContract {
    interface GenreDetailsView : BaseView<ArrayList<Song>>

    interface Presenter : BasePresenter<GenreDetailsView> {
        fun loadGenre(genreId: Int)
    }
}
