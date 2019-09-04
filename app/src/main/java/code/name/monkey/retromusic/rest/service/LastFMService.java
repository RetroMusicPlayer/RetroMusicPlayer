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

package code.name.monkey.retromusic.rest.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import code.name.monkey.retromusic.rest.model.LastFmAlbum;
import code.name.monkey.retromusic.rest.model.LastFmArtist;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface LastFMService {
    String API_KEY_BAK = "bd9c6ea4d55ec9ed3af7d276e5ece304";
    String API_KEY = "c679c8d3efa84613dc7dcb2e8d42da4c";
    String BASE_QUERY_PARAMETERS = "?format=json&autocorrect=1&api_key=" + API_KEY;
    String METHOD_TRACK = "track.getInfo";

    @NonNull
    @GET(BASE_QUERY_PARAMETERS + "&method=album.getinfo")
    Observable<LastFmAlbum> getAlbumInfo(@Query("album") @NonNull String albumName, @Query("artist") @NonNull String artistName, @Nullable @Query("lang") String language);

    @NonNull
    @GET(BASE_QUERY_PARAMETERS + "&method=artist.getinfo")
    Call<LastFmArtist> getArtistInfo(@Query("artist") @NonNull String artistName, @Nullable @Query("lang") String language, @Nullable @Header("Cache-Control") String cacheControl);

    @NonNull
    @GET(BASE_QUERY_PARAMETERS + "&method=artist.getinfo")
    Observable<LastFmArtist> getArtistInfoFloable(@Query("artist") @NonNull String artistName, @Nullable @Query("lang") String language, @Nullable @Header("Cache-Control") String cacheControl);

}