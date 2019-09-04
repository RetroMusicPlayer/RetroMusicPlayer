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
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject


/**
 * Created by hemanths on 12/08/17.
 */
interface AlbumsView : BaseView {
    fun albums(albums: ArrayList<Album>)
}

interface AlbumsPresenter : Presenter<AlbumsView> {

    fun loadAlbums()

    class AlbumsPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<AlbumsView>(), AlbumsPresenter {

        private var disposable: Disposable? = null

        private fun showList(albums: ArrayList<Album>) {
            if (albums.isNotEmpty()) view.albums(albums) else view.showEmptyView()
        }

        override fun detachView() {
            super.detachView()
            disposable?.dispose()
        }
        override fun loadAlbums() {
            disposable = repository.allAlbumsFlowable
                    .subscribe { this.showList(it) }
        }
    }
}