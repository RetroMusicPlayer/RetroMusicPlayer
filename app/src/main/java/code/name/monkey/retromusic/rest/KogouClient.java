
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

package code.name.monkey.retromusic.rest;

import android.content.Context;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.App;
import code.name.monkey.retromusic.rest.service.KuGouApiService;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static code.name.monkey.retromusic.Constants.BASE_API_URL_KUGOU;

/**
 * Created by hemanths on 23/08/17.
 */

public class KogouClient {

    private static final String BASE_URL = BASE_API_URL_KUGOU;

    private KuGouApiService apiService;

    public KogouClient() {
        this(createDefaultOkHttpClientBuilder().build());
    }

    private KogouClient(@NonNull Call.Factory client) {
        Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        apiService = restAdapter.create(KuGouApiService.class);
    }

    @Nullable
    private static Cache createDefaultCache(Context context) {
        File cacheDir = new File(context.getCacheDir().getAbsolutePath(), "/okhttp-lastfm/");
        if (cacheDir.mkdirs() || cacheDir.isDirectory()) {
            return new Cache(cacheDir, 1024 * 1024 * 10);
        }
        return null;
    }

    private static Interceptor createCacheControlInterceptor() {
        return chain -> {
            Request modifiedRequest = chain.request().newBuilder()
                    .addHeader("Cache-Control", String.format("max-age=%d, max-stale=%d", 31536000, 31536000))
                    .build();
            return chain.proceed(modifiedRequest);
        };
    }

    private static OkHttpClient.Builder createDefaultOkHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .cache(createDefaultCache(App.Companion.getInstance()))
                .addInterceptor(createCacheControlInterceptor());
    }

    public KuGouApiService getApiService() {
        return apiService;
    }
}
