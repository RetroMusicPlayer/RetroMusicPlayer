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

package code.name.monkey.retromusic.dagger.module

import code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter
import code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter.GenreDetailsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.GenresPresenter
import code.name.monkey.retromusic.mvp.presenter.GenresPresenter.GenresPresenterImpl
import dagger.Module
import dagger.Provides

/**
 * Created by hemanths on 2019-09-04.
 */
@Module
class GenreModule {

    @Provides
    fun providesGenresPresenter(presenter: GenresPresenterImpl): GenresPresenter {
        return presenter
    }


    @Provides
    fun providesGenreDetailsPresenter(presenter: GenreDetailsPresenterImpl): GenreDetailsPresenter {
        return presenter
    }
}
