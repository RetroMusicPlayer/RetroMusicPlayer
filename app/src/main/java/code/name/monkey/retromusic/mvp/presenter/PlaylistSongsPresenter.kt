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

import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.PlaylistSongsContract

/**
 * Created by hemanths on 20/08/17.
 */

class PlaylistSongsPresenter(private val view: PlaylistSongsContract.PlaylistSongsView,
                             private val mPlaylist: Playlist) : Presenter(), PlaylistSongsContract.Presenter {


    override fun subscribe() {
        loadSongs(mPlaylist)
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadSongs(playlist: Playlist) {
        disposable.add(repository.getPlaylistSongs(playlist)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ songs -> view.showData(songs) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }
}
