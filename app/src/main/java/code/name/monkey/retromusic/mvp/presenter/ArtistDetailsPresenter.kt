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

import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.BaseView
import code.name.monkey.retromusic.rest.model.LastFmArtist

/**
 * Created by hemanths on 20/08/17.
 */
interface ArtistDetailsView : BaseView {

    fun artist(artist: Artist)

    fun artistInfo(lastFmArtist: LastFmArtist?)

    fun complete()
}