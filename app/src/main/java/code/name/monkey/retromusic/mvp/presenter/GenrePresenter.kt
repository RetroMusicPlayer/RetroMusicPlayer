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

import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.contract.GenreContract
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */

class GenrePresenter(
        private val view: GenreContract.GenreView) : Presenter(), GenreContract.Presenter {

    override fun subscribe() {
        loadGenre()
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    override fun loadGenre() {
        disposable.add(repository.allGenres
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }

    private fun showList(genres: ArrayList<Genre>) {
        if (genres.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(genres)
        }
    }
}
