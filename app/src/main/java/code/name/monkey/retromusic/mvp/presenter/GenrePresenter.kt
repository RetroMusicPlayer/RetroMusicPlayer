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
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

/**
 * @author Hemanth S (h4h13).
 */
interface GenresView : BaseView {
    fun genres(genres: ArrayList<Genre>)
}

interface GenresPresenter : Presenter<GenresView> {
    fun loadGenres()

    class GenresPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<GenresView>(), GenresPresenter {

        private var disposable: Disposable? = null

        override fun loadGenres() {
            disposable = repository.allGenresFlowable
                    .subscribe { this.showList(it) }
        }

        private fun showList(genres: ArrayList<Genre>) {
            if (genres.isNotEmpty()) {
                view.genres(genres)
            } else {
                view.showEmptyView()
            }
        }
    }
}
