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
