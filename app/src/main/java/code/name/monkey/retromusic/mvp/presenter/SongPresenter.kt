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
import code.name.monkey.retromusic.mvp.PresenterImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
import io.reactivex.disposables.Disposable
import java.util.*
import javax.inject.Inject

/**
 * Created by hemanths on 10/08/17.
 */
interface SongView {
    fun songs(songs: ArrayList<Song>)

    fun showEmptyView()
}

interface SongPresenter : Presenter<SongView> {
    fun loadSongs()

    class SongPresenterImpl @Inject constructor(
            private val repository: Repository
    ) : PresenterImpl<SongView>(), SongPresenter {

        private var disposable: Disposable? = null

        override fun loadSongs() {
            disposable = repository.allSongsFlowable
                    .subscribe({
                        view?.songs(it)
                    }, { t -> print(t) })
        }

        override fun detachView() {
            super.detachView()
            disposable?.dispose()
        }
    }
}


