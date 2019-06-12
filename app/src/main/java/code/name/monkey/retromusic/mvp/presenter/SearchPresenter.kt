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
import code.name.monkey.retromusic.mvp.contract.SearchContract
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by hemanths on 20/08/17.
 */

class SearchPresenter(private val view: SearchContract.SearchView) : Presenter(), SearchContract.SearchPresenter {

    override fun subscribe() {
        search("")
    }

    override fun unsubscribe() {
        disposable.clear()
    }

    private fun showList(albums: ArrayList<Any>) {
        if (albums.isEmpty()) {
            view.showEmptyView()
        } else {
            view.showData(albums)
        }
    }

    override fun search(query: String?) {
        disposable.add(repository.search(query)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe { view.loading() }
                .subscribe({ this.showList(it) },
                        { view.showEmptyView() },
                        { view.completed() }))
    }
}
