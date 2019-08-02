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

import android.os.Bundle
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.ArtistDetailContract
import code.name.monkey.retromusic.activities.ArtistDetailActivity


/**
 * Created by hemanths on 20/08/17.
 */

class ArtistDetailsPresenter(private val view: ArtistDetailContract.ArtistsDetailsView,
                             private val bundle: Bundle) : Presenter(), ArtistDetailContract.Presenter {

    override fun subscribe() {
        loadArtistById()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadArtistById() {
        disposable.add(repository.getArtistByIdFlowable(bundle.getInt(ArtistDetailActivity.EXTRA_ARTIST_ID))
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showArtist(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showArtist(album: Artist?) {
        if (album != null) {
            view.showData(album)
        } else {
            view.showEmptyView()
        }
    }
}
