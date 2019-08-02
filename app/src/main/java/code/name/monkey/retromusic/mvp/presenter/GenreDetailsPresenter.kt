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

import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.GenreDetailsContract
import java.util.*


/**
 * Created by hemanths on 20/08/17.
 */

class GenreDetailsPresenter(private val view: GenreDetailsContract.GenreDetailsView,
                            private val genreId: Int) : Presenter(), GenreDetailsContract.Presenter {

    override fun subscribe() {
        loadGenre(genreId)
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadGenre(genreId: Int) {
        disposable.add(repository.getGenreFlowable(genreId)
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showGenre(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showGenre(songs: ArrayList<Song>?) {
        if (songs != null) {
            view.showData(songs)
        } else {
            view.showEmptyView()
        }
    }
}
