package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.ArtistContract
import java.util.*

class ArtistPresenter(private val mView: ArtistContract.ArtistView) : Presenter(), ArtistContract.Presenter {

    override fun subscribe() {
        loadArtists()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    private fun showList(songs: ArrayList<Artist>) {
        if (songs.isEmpty()) {
            mView.showEmptyView()
        } else {
            mView.showData(songs)
        }
    }

    override fun loadArtists() {
        disposable.add(repository.allArtists
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { mView.loading() }
                .subscribe({ this.showList(it) },
                        { mView.showEmptyView() },
                        { mView.completed() }))
    }
}
