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
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import javax.inject.Inject


/**
 * Created by hemanths on 19/08/17.
 */

interface PlaylistView : BaseView {
    fun playlists(playlists: ArrayList<Playlist>)
}

interface PlaylistsPresenter : Presenter<PlaylistView> {

    fun playlists()

    class PlaylistsPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<PlaylistView>(), PlaylistsPresenter {

        private var disposable: Disposable? = null

        override fun playlists() {
            disposable = repository.allPlaylistsFlowable
                    .subscribe { this.showList(it) }
        }

        private fun showList(arrayList: ArrayList<Playlist>) {
            if (arrayList.isEmpty()) {
                view.showEmptyView()
            } else {
                view.playlists(arrayList)
            }
        }
    }
}


