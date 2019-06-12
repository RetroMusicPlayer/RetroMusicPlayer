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
