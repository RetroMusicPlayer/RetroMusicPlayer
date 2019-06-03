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
import code.name.monkey.retromusic.rest.model.KuGouRawLyric;
import code.name.monkey.retromusic.rest.model.KuGouSearchLyricResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hemanths on 28/07/17.
 */

public interface KuGouApiService {

    @NonNull
    @GET("search?ver=1&man=yes&client=pc")
    Observable<KuGouSearchLyricResult> searchLyric(@Query("keyword") @NonNull String songName, @Query("duration") @NonNull String duration);

    @NonNull
    @GET("download?ver=1&client=pc&fmt=lrc&charset=utf8")
    Observable<KuGouRawLyric> getRawLyric(@Query("id") @NonNull String id, @Query("accesskey") @NonNull String accesskey);
}
