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

import android.content.Context
import code.name.monkey.retromusic.mvp.presenter.*
import code.name.monkey.retromusic.mvp.presenter.AlbumDetailsPresenter.AlbumDetailsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.AlbumsPresenter.AlbumsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.ArtistDetailsPresenter.ArtistDetailsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.ArtistsPresenter.ArtistsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.GenreDetailsPresenter.GenreDetailsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.GenresPresenter.GenresPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.HomePresenter.HomePresenterImpl
import code.name.monkey.retromusic.mvp.presenter.PlaylistSongsPresenter.PlaylistSongsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.PlaylistsPresenter.PlaylistsPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.SearchPresenter.SearchPresenterImpl
import code.name.monkey.retromusic.mvp.presenter.SongPresenter.SongPresenterImpl
import code.name.monkey.retromusic.providers.RepositoryImpl
import code.name.monkey.retromusic.providers.interfaces.Repository
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
    fun providesAlbumsPresenter(presenter: AlbumsPresenterImpl): AlbumsPresenter {
        return presenter
    }

    @Provides
    fun providesAlbumDetailsPresenter(presenter: AlbumDetailsPresenterImpl): AlbumDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesArtistDetailsPresenter(presenter: ArtistDetailsPresenterImpl): ArtistDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesArtistsPresenter(presenter: ArtistsPresenterImpl): ArtistsPresenter {
        return presenter
    }

    @Provides
    fun providesGenresPresenter(presenter: GenresPresenterImpl): GenresPresenter {
        return presenter
    }

    @Provides
    fun providesGenreDetailsPresenter(presenter: GenreDetailsPresenterImpl): GenreDetailsPresenter {
        return presenter
    }

    @Provides
    fun providesHomePresenter(presenter: HomePresenterImpl): HomePresenter {
        return presenter
    }

    @Provides
    fun providesPlaylistSongPresenter(presenter: PlaylistSongsPresenterImpl): PlaylistSongsPresenter {
        return presenter
    }

    @Provides
    fun providesPlaylistsPresenter(presenter: PlaylistsPresenterImpl): PlaylistsPresenter {
        return presenter
    }

    @Provides
    fun providesSearchPresenter(presenter: SearchPresenterImpl): SearchPresenter {
        return presenter
    }

    @Provides
    fun providesSongPresenter(presenter: SongPresenterImpl): SongPresenter {
        return presenter
    }
}