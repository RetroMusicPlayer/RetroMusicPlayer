package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.SongContract
import java.util.*

/**
 * Created by hemanths on 10/08/17.
 */

class SongPresenter(private val view: SongContract.SongView) : Presenter(), SongContract.Presenter {

    override fun loadSongs() {
        disposable.add(repository.allSongs
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    override fun subscribe() {
        loadSongs()
    }

    private fun showList(songs: ArrayList<Song>) {
        if (songs.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(songs)
        }
    }

    override fun unsubscribe() {
        disposable.clear()
    }
}
