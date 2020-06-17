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

package io.github.muntashirakon.music.dagger

import io.github.muntashirakon.music.activities.artists.ArtistDetailActivity
import io.github.muntashirakon.music.activities.GenreDetailsActivity
import io.github.muntashirakon.music.activities.PlaylistDetailActivity
import io.github.muntashirakon.music.activities.SearchActivity
import io.github.muntashirakon.music.dagger.module.AppModule
import io.github.muntashirakon.music.dagger.module.PresenterModule
import io.github.muntashirakon.music.fragments.albums.AlbumsFragment
import io.github.muntashirakon.music.fragments.artists.ArtistsFragment
import io.github.muntashirakon.music.fragments.genres.GenresFragment
import io.github.muntashirakon.music.fragments.home.BannerHomeFragment
import io.github.muntashirakon.music.fragments.playlists.PlaylistsFragment
import io.github.muntashirakon.music.fragments.songs.SongsFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by hemanths on 2019-09-04.
 */
@Singleton
@Component(
    modules = [
        AppModule::class,
        PresenterModule::class
    ]
)
interface MusicComponent {

    fun inject(songsFragment: SongsFragment)

    fun inject(albumsFragment: AlbumsFragment)

    fun inject(artistsFragment: ArtistsFragment)

    fun inject(genresFragment: GenresFragment)

    fun inject(playlistsFragment: PlaylistsFragment)

    fun inject(artistDetailActivity: ArtistDetailActivity)

    fun inject(playlistDetailActivity: PlaylistDetailActivity)

    fun inject(genreDetailsActivity: GenreDetailsActivity)

    fun inject(searchActivity: SearchActivity)

    fun inject(bannerHomeFragment: BannerHomeFragment)
}
