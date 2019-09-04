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

import code.name.monkey.retromusic.mvp.Presenter
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import javax.inject.Inject

/**
 * Created by hemanths on 20/08/17.
 */

interface SearchView {
    fun showData(data: MutableList<Any>)

    fun showEmptyView()
}

interface SearchPresenter : Presenter<SearchView> {

    fun search(query: String?)

    class SearchPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<SearchView>(), SearchPresenter {

        override fun attachView(view: SearchView) {
            super.attachView(view)
        }

        override fun detachView() {
            super.detachView()
        }

        override fun search(query: String?) {
            view.showData(repository.search(query))
        }
    }
}


