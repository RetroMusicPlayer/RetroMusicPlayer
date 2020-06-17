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

package io.github.muntashirakon.music.dagger.module

import android.content.Context
import io.github.muntashirakon.music.mvp.presenter.GenreDetailsPresenter
import io.github.muntashirakon.music.mvp.presenter.GenreDetailsPresenter.GenreDetailsPresenterImpl
import io.github.muntashirakon.music.mvp.presenter.PlaylistSongsPresenter
import io.github.muntashirakon.music.mvp.presenter.PlaylistSongsPresenter.PlaylistSongsPresenterImpl
import io.github.muntashirakon.music.mvp.presenter.SearchPresenter
import io.github.muntashirakon.music.mvp.presenter.SearchPresenter.SearchPresenterImpl
import io.github.muntashirakon.music.providers.RepositoryImpl
import io.github.muntashirakon.music.providers.interfaces.Repository
import dagger.Module
import dagger.Provides

/**
 * Created by hemanths on 2019-12-30.
 */

@Module
class PresenterModule {

    @Provides
    fun providesRepository(context: Context): Repository {
        return RepositoryImpl(context)
    }

    @Provides
    fun providesGenreDetailsPresenter(presenter: GenreDetailsPresenterImpl): GenreDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesPlaylistSongPresenter(presenter: PlaylistSongsPresenterImpl): PlaylistSongsPresenter {
        return presenter
    }

    @Provides
    fun providesSearchPresenter(presenter: SearchPresenterImpl): SearchPresenter {
        return presenter
    }
}