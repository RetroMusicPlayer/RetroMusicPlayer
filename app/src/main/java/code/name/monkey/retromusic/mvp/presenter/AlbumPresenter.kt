package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.AlbumContract
import java.util.*


/**
 * Created by hemanths on 12/08/17.
 */

class AlbumPresenter(private val view: AlbumContract.AlbumView) : Presenter(), AlbumContract.Presenter {

    override fun subscribe() {
        loadAlbums()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    private fun showList(albums: ArrayList<Album>) {
        view.showData(albums)
    }

    override fun loadAlbums() {
        disposable.add(repository.allAlbums
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }
}
