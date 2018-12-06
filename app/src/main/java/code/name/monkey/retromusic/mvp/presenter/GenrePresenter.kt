package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.GenreContract
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class GenrePresenter(
        private val view: GenreContract.GenreView) : Presenter(), GenreContract.Presenter {

    override fun subscribe() {
        loadGenre()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadGenre() {
        disposable.add(repository.allGenres
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showList(genres: ArrayList<Genre>) {
        if (genres.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(genres)
        }
    }
}
