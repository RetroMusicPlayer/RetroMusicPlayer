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
import code.name.monkey.retromusic.deezer.Data
import code.name.monkey.retromusic.deezer.DeezerApiService
import code.name.monkey.retromusic.deezer.DeezerResponse
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

class ArtistImageFetcher(private val context: Context,
                         private val deezerApiService: DeezerApiService,
                         private val okHttp: OkHttpClient,
                         private val model: ArtistImage) : DataFetcher<InputStream> {
    @Volatile
    private var isCancelled: Boolean = false
    private var call: Call<DeezerResponse>? = null
    private var streamFetcher: OkHttpStreamFetcher? = null


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {
            if (!MusicUtil.isArtistNameUnknown(model.artistName) && RetroUtil.isAllowedToDownloadMetadata(context)) {
                val artists = model.artistName.split(",")
                call = deezerApiService.getArtistImage(artists[0])
                call?.enqueue(object : Callback<DeezerResponse> {
                    override fun onFailure(call: Call<DeezerResponse>, t: Throwable) {
                        callback.onLoadFailed(Exception(t))
                    }

                    override fun onResponse(call: Call<DeezerResponse>, response: Response<DeezerResponse>) {
                        if (isCancelled) {
                            callback.onDataReady(null)
                            return
                        }
                        try {
                            val deezerResponse: DeezerResponse? = response.body()
                            val url = deezerResponse?.data?.get(0)?.let { getHighestQuality(it) }
                            streamFetcher = OkHttpStreamFetcher(okHttp, GlideUrl(url))
                            streamFetcher?.loadData(priority, callback)
                        } catch (e: Exception) {
                            callback.onLoadFailed(Exception("No artist image url found"))
                        }
                    }

                })
            }
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }

    }

    fun getHighestQuality(imageUrl: Data): String {
        return when {
            imageUrl.pictureXl.isNotEmpty() -> imageUrl.pictureXl
            imageUrl.pictureBig.isNotEmpty() -> imageUrl.pictureBig
            imageUrl.pictureMedium.isNotEmpty() -> imageUrl.pictureMedium
            imageUrl.pictureSmall.isNotEmpty() -> imageUrl.pictureSmall
            imageUrl.picture.isNotEmpty() -> imageUrl.picture
            else -> ""
        }
    }

    override fun cleanup() {
        if (streamFetcher != null) {
            streamFetcher!!.cleanup()
        }
    }

    override fun cancel() {
        isCancelled = true
        call?.cancel()
        if (streamFetcher != null) {
            streamFetcher!!.cancel()
        }
    }

    companion object {
        val TAG: String = ArtistImageFetcher::class.java.simpleName
    }
}

class ArtistImageLoader(private val context: Context,
                        private val deezerApiService: DeezerApiService,
                        private val okhttp: OkHttpClient) : ModelLoader<ArtistImage, InputStream> {

    override fun buildLoadData(model: ArtistImage, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model.artistName), ArtistImageFetcher(context, deezerApiService, okhttp, model))
    }

    override fun handles(model: ArtistImage): Boolean {
        return true
    }

    class Factory(private val context: Context) : ModelLoaderFactory<ArtistImage, InputStream> {
        private val deezerApiService: DeezerApiService =
                DeezerApiService.invoke(DeezerApiService.createDefaultOkHttpClient(context)
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
            return ArtistImageLoader(context, deezerApiService, okHttp)
        }

        override fun teardown() {}
    }

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT = 700
    }
}