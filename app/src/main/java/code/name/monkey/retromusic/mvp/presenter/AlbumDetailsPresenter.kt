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

import code.name.monkey.retromusic.Result
import code.name.monkey.retromusic.Result.Success
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by hemanths on 20/08/17.
 */
interface AlbumDetailsView {

    fun album(album: Album)

    fun complete()

    fun loadArtistImage(artist: Artist)

    fun moreAlbums(albums: ArrayList<Album>)
}

interface AlbumDetailsPresenter : Presenter<AlbumDetailsView> {
    fun loadAlbum(albumId: Int)

    fun loadMore(artistId: Int)

    class AlbumDetailsPresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<AlbumDetailsView>(), AlbumDetailsPresenter, CoroutineScope {

        private val job = Job()
        private lateinit var album: Album

        override fun loadMore(artistId: Int) {
            launch {
                when (val result = repository.artistById(artistId)) {
                    is Success -> withContext(Dispatchers.Main) { showArtistImage(result.data) }
                    is Result.Error -> withContext(Dispatchers.Main) {}
                }
            }
        }

        private fun showArtistImage(artist: Artist) {
            view?.loadArtistImage(artist)

            artist.albums?.filter { it.id != album.id }?.let {
                if (it.isNotEmpty()) view?.moreAlbums(ArrayList(it))
            }
        }

        override fun loadAlbum(albumId: Int) {
            launch {
                when (val result = repository.albumById(albumId)) {
                    is Success -> withContext(Dispatchers.Main) {
                        album = result.data
                        view?.album(result.data)
                    }
                    is Error -> withContext(Dispatchers.Main) { view?.complete() }
                }
            }
        }

        override fun detachView() {
            super.detachView()
            job.cancel()
        }

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job
    }
}