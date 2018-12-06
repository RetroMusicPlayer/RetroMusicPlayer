package code.name.monkey.retromusic.mvp.contract

import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.mvp.BasePresenter
import code.name.monkey.retromusic.mvp.BaseView

import java.util.ArrayList

/**
 * @author Hemanth S (h4h13).
 */

interface GenreContract {
    interface GenreView : BaseView<ArrayList<Genre>>

    interface Presenter : BasePresenter<GenreView> {
        fun loadGenre()
    }
}
