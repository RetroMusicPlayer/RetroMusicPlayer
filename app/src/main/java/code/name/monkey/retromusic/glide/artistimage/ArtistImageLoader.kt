/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.glide.artistimage

import android.content.Context
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Data
import code.name.monkey.retromusic.network.DeezerService
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.stream.StreamModelLoader
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.TimeUnit
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class ArtistImage(val artist: Artist)

class ArtistImageFetcher(
    private val context: Context,
    private val deezerService: DeezerService,
    val model: ArtistImage,
    val urlLoader: ModelLoader<GlideUrl, InputStream>,
    val width: Int,
    val height: Int
) : DataFetcher<InputStream> {

    private var urlFetcher: DataFetcher<InputStream>? = null
    private var isCancelled: Boolean = false

    override fun cleanup() {
        urlFetcher?.cleanup()
    }

    override fun getId(): String {
        return model.artist.name
    }

    override fun cancel() {
        isCancelled = true
        urlFetcher?.cancel()
    }

    override fun loadData(priority: Priority?): InputStream? {
        if (!MusicUtil.isArtistNameUnknown(model.artist.name) &&
            PreferenceUtil.isAllowedToDownloadMetadata()
        ) {
            val artists = model.artist.name.split(",")
            val response = deezerService.getArtistImage(artists[0]).execute()

            if (!response.isSuccessful) {
                throw IOException("Request failed with code: " + response.code())
            }

            if (isCancelled) return null

            return try {
                val deezerResponse = response.body()
                val imageUrl = deezerResponse?.data?.get(0)?.let { getHighestQuality(it) }
                // Fragile way to detect a place holder image returned from Deezer:
                // ex: "https://e-cdns-images.dzcdn.net/images/artist//250x250-000000-80-0-0.jpg"
                // the double slash implies no artist identified
                val placeHolder = imageUrl?.contains("/images/artist//") ?: false
                if (!placeHolder) {
                    val glideUrl = GlideUrl(imageUrl)
                    urlFetcher = urlLoader.getResourceFetcher(glideUrl, width, height)
                    urlFetcher?.loadData(priority)
                } else {
                    getFallbackAlbumImage()
                }
            } catch (e: Exception) {
                getFallbackAlbumImage()
            }
        } else return null
    }

    private fun getFallbackAlbumImage(): InputStream? {
        val imageUri = MusicUtil.getMediaStoreAlbumCoverUri(model.artist.safeGetFirstAlbum().id)
        return context.contentResolver.openInputStream(imageUri)
    }

    private fun getHighestQuality(imageUrl: Data): String {
        return when {
            imageUrl.pictureXl.isNotEmpty() -> imageUrl.pictureXl
            imageUrl.pictureBig.isNotEmpty() -> imageUrl.pictureBig
            imageUrl.pictureMedium.isNotEmpty() -> imageUrl.pictureMedium
            imageUrl.pictureSmall.isNotEmpty() -> imageUrl.pictureSmall
            imageUrl.picture.isNotEmpty() -> imageUrl.picture
            else -> ""
        }
    }
}

class ArtistImageLoader(
    val context: Context,
    private val deezerService: DeezerService,
    private val urlLoader: ModelLoader<GlideUrl, InputStream>
) : StreamModelLoader<ArtistImage> {

    override fun getResourceFetcher(
        model: ArtistImage,
        width: Int,
        height: Int
    ): DataFetcher<InputStream> {
        return ArtistImageFetcher(context, deezerService, model, urlLoader, width, height)
    }
}

class Factory(
    val context: Context
) : ModelLoaderFactory<ArtistImage, InputStream> {

    private var deezerService: DeezerService
    private var okHttpFactory: OkHttpUrlLoader.Factory

    init {
        okHttpFactory = OkHttpUrlLoader.Factory(
            OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build()
        )
        deezerService = DeezerService.invoke(
            DeezerService.createDefaultOkHttpClient(context)
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(createLogInterceptor())
                .build()
        )
    }

    private fun createLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    override fun build(
        context: Context?,
        factories: GenericLoaderFactory?
    ): ModelLoader<ArtistImage, InputStream> {
        return ArtistImageLoader(
            context!!,
            deezerService,
            okHttpFactory.build(context, factories)
        )
    }

    override fun teardown() {
        okHttpFactory.teardown()
    }

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT: Long = 700
    }
}
