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

import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.ArtistContract
import java.util.*

class ArtistPresenter(private val mView: ArtistContract.ArtistView) : Presenter(), ArtistContract.Presenter {

    override fun subscribe() {
        loadArtists()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    private fun showList(songs: ArrayList<Artist>) {
        if (songs.isEmpty()) {
            mView.showEmptyView()
        } else {
            mView.showData(songs)
        }
    }

    override fun loadArtists() {
        disposable.add(repository.allArtists
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { mView.loading() }
                .subscribe({ this.showList(it) },
                        { mView.showEmptyView() },
                        { mView.completed() }))
    }
}
