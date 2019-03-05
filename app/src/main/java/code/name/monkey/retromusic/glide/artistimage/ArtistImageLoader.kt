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

package code.name.monkey.retromusic.glide.artistimage

import android.content.Context
import android.text.TextUtils
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmArtist
import code.name.monkey.retromusic.util.LastFMUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.util.concurrent.TimeUnit


class ArtistImage(val artistName: String, val skipOkHttpCache: Boolean)

class ArtistImageFetcher(private val context: Context, private val lastFMRestClient: LastFMRestClient, private val okHttp: OkHttpClient, private val model: ArtistImage) : DataFetcher<InputStream> {
    @Volatile
    private var isCancelled: Boolean = false
    private var call: Call<LastFmArtist>? = null
    private var streamFetcher: OkHttpStreamFetcher? = null


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.MEMORY_CACHE
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {
            if (!MusicUtil.isArtistNameUnknown(model.artistName) && RetroUtil.isAllowedToDownloadMetadata(context)) {
                call = lastFMRestClient.apiService.getArtistInfo(model.artistName, null, if (model.skipOkHttpCache) "no-cache" else null)
                call!!.enqueue(object : Callback<LastFmArtist> {
                    override fun onResponse(call: Call<LastFmArtist>, response: Response<LastFmArtist>) {
                        if (isCancelled) {
                            callback.onDataReady(null)
                            return
                        }

                        val lastFmArtist = response.body()
                        if (lastFmArtist == null || lastFmArtist.artist == null || lastFmArtist.artist.image == null) {
                            callback.onLoadFailed(Exception("No artist image url found"))
                            return
                        }

                        val url = LastFMUtil.getLargestArtistImageUrl(lastFmArtist.artist.image)
                        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url.trim { it <= ' ' })) {
                            callback.onLoadFailed(Exception("No artist image url found"))
                            return
                        }

                        streamFetcher = OkHttpStreamFetcher(okHttp, GlideUrl(url))
                        streamFetcher!!.loadData(priority, callback)
                    }

                    override fun onFailure(call: Call<LastFmArtist>, throwable: Throwable) {
                        callback.onLoadFailed(Exception(throwable))
                    }
                })


            }
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }

    }

    override fun cleanup() {
        if (streamFetcher != null) {
            streamFetcher!!.cleanup()
        }
    }

    override fun cancel() {
        isCancelled = true
        if (call != null) {
            call!!.cancel()
        }
        if (streamFetcher != null) {
            streamFetcher!!.cancel()
        }
    }

    companion object {
        val TAG: String = ArtistImageFetcher::class.java.simpleName
    }
}

class ArtistImageLoader(private val context: Context, private val lastFMClient: LastFMRestClient, private val okhttp: OkHttpClient) : ModelLoader<ArtistImage, InputStream> {

    override fun buildLoadData(model: ArtistImage, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model.artistName), ArtistImageFetcher(context, lastFMClient, okhttp, model))
    }

    override fun handles(model: ArtistImage): Boolean {
        return true
    }

    class Factory(private val context: Context) : ModelLoaderFactory<ArtistImage, InputStream> {
        private val lastFMClient: LastFMRestClient = LastFMRestClient(LastFMRestClient.createDefaultOkHttpClientBuilder(context)
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build())
        private val okHttp: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build()


        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ArtistImage, InputStream> {
            return ArtistImageLoader(context, lastFMClient, okHttp)
        }

        override fun teardown() {}
    }

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT = 500
    }
}