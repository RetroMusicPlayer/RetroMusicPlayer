package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.PlaylistContract
import java.util.*


/**
 * Created by hemanths on 19/08/17.
 */

class PlaylistPresenter(private val view: PlaylistContract.PlaylistView) : Presenter(), PlaylistContract.Presenter {

    override fun subscribe() {
        loadPlaylists()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadPlaylists() {
        disposable.add(repository.allPlaylists
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showList(songs: ArrayList<Playlist>) {
        if (songs.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(songs)
        }
    }
}
