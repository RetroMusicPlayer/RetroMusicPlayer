package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.GenreDetailsContract
import java.util.*


/**
 * Created by hemanths on 20/08/17.
 */

class GenreDetailsPresenter(private val view: GenreDetailsContract.GenreDetailsView,
                            private val genreId: Int) : Presenter(), GenreDetailsContract.Presenter {

    override fun subscribe() {
        loadGenre(genreId)
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadGenre(genreId: Int) {
        disposable.add(repository.getGenre(genreId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showGenre(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showGenre(songs: ArrayList<Song>?) {
        if (songs != null) {
            view.showData(songs)
        } else {
            view.showEmptyView()
        }
    }
}
