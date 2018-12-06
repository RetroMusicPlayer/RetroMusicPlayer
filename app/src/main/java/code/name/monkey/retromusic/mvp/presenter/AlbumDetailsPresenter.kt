package code.name.monkey.retromusic.mvp.presenter

import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract


/**
 * Created by hemanths on 20/08/17.
 */

class AlbumDetailsPresenter(private val view: AlbumDetailsContract.AlbumDetailsView, private val albumId: Int) : Presenter(), AlbumDetailsContract.Presenter {

    override fun subscribe() {
        loadAlbumSongs(albumId)
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadAlbumSongs(albumId: Int) {
        disposable.add(repository.getAlbum(albumId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showAlbum(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showAlbum(album: Album?) {
        if (album != null) {
            view.showData(album)
        } else {
            view.showEmptyView()
        }
    }
}
