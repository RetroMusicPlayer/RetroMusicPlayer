/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

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
        disposable.add(repository.getAlbumFlowable(albumId)
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
