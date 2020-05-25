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

import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

interface HomeView : BaseView {
    fun sections(sections: List<Home>)
}

interface HomePresenter : Presenter<HomeView> {
    fun loadSections()

    class HomePresenterImpl @Inject constructor(
        private val repository: Repository
    ) : PresenterImpl<HomeView>(), HomePresenter, CoroutineScope {

        private val job = Job()

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.IO + job

        override fun detachView() {
            super.detachView()
            job.cancel()
        }

        override fun loadSections() {
            launch {
                val list = ArrayList<Home>()
                val recentArtistResult = listOf(
                    repository.topArtists(),
                    repository.topAlbums(),
                    repository.recentArtists(),
                    repository.recentAlbums(),
                    repository.favoritePlaylist()
                )
                for (r in recentArtistResult) {
                    r?.let { list.add(it) }
                }
                withContext(Dispatchers.Main) {
                    if (list.isNotEmpty()) view?.sections(list) else view?.showEmptyView()
                }
            }
        }
    }
}