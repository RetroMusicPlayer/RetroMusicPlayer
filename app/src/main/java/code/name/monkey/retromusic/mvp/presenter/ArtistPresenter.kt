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

import code.name.monkey.retromusic.helper.SearchQueryHelper.songs
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import javax.inject.Inject

interface ArtistsView : BaseView {
    fun artists(artists: ArrayList<Artist>)
}

interface ArtistsPresenter : Presenter<ArtistsView> {

    fun loadArtists()

    class ArtistsPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<ArtistsView>(), ArtistsPresenter {

        private var disposable: Disposable? = null

        private fun showList(artists: ArrayList<Artist>) {
            if (songs.isNotEmpty())
                view.artists(artists)
            else
                view.showEmptyView()
        }

        override fun detachView() {
            super.detachView()
            disposable?.dispose()
        }

        override fun loadArtists() {
            disposable = repository.allArtistsFlowable
                    .subscribe({
                        view.artists(it)
                    }, {
                        println(it)
                    })
        }
    }
}
