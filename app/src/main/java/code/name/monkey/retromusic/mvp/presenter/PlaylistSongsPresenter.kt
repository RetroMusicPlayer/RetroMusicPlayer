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
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by hemanths on 20/08/17.
 */
interface PlaylistSongsView : BaseView {
    fun songs(songs: ArrayList<Song>)
}

interface PlaylistSongsPresenter : Presenter<PlaylistSongsView> {
    fun loadPlaylistSongs(playlist: Playlist)

    class PlaylistSongsPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<PlaylistSongsView>(), PlaylistSongsPresenter {

        private var disposable: Disposable? = null

        override fun loadPlaylistSongs(playlist: Playlist) {
            disposable = repository.getPlaylistSongsFlowable(playlist)
                    .subscribe {
                        view.songs(it)
                    }
        }

        override fun detachView() {
            super.detachView()
            disposable?.dispose()
        }

        override fun attachView(view: PlaylistSongsView) {
            super.attachView(view)
        }

    }
}