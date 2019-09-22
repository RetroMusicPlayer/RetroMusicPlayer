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
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaMetadataRetriever
import code.name.monkey.retromusic.glide.audiocover.AudioFileCoverUtils
import code.name.monkey.retromusic.util.ImageUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.stream.StreamModelLoader
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream


class AlbumCover(
        var year: Int,
        var filePath: String?)

class ArtistImage(val artistName: String, // filePath to get the image of the artist
                  val albumCovers: List<AlbumCover>
) {

    fun toIdString(): String {
        val id = StringBuilder(artistName)
        for (albumCover in albumCovers) {
            id.append(albumCover.year).append(albumCover.filePath)
        }
        return id.toString()
    }
}


class ArtistImageFetcher(
        val artistImage: ArtistImage,
        val ignoreMediaStore: Boolean
) : DataFetcher<InputStream> {
    private var stream: InputStream? = null

    override fun cleanup() {
        if (stream != null) {
            try {
                stream?.close()
            } catch (ignore: IOException) {
                // can't do much about it
            }
        }
    }

    override fun cancel() {

    }

    override fun loadData(priority: Priority?): InputStream {
        println("MOSAIC load data for" + artistImage.artistName)
        stream = getMosaic(artistImage.albumCovers)?.let {
            it
        }
        return stream as InputStream
    }

    private fun getMosaic(albumCovers: List<AlbumCover>): InputStream? {
        val retriever = MediaMetadataRetriever()
        val artistBitMapSize = 512
        val images = HashMap<InputStream, Int>()
        var result: InputStream? = null
        var streams = ArrayList<InputStream>()
        try {
            for (albumCover in albumCovers) {
                var picture: ByteArray? = null
                if (!ignoreMediaStore) {
                    retriever.setDataSource(albumCover.filePath)
                    picture = retriever.embeddedPicture
                }
                val stream: InputStream? = if (picture != null) {
                    ByteArrayInputStream(picture)
                } else {
                    AudioFileCoverUtils.fallback(albumCover.filePath)
                }
                if (stream != null) {
                    images[stream] = albumCover.year
                }
                val nbImages = images.size
                if (nbImages > 3) {
                    streams = ArrayList(images.keys)

                    var divisor = 1
                    var i = 1
                    while (i < nbImages && Math.pow(i.toDouble(), 2.0) <= nbImages) {
                        divisor = i
                        ++i
                    }
                    divisor += 1
                    var nbTiles = Math.pow(divisor.toDouble(), 2.0)

                    if (nbImages < nbTiles) {
                        divisor -= 1;
                        nbTiles = Math.pow(divisor.toDouble(), 2.0)
                    }

                    val resize = (artistBitMapSize / divisor) + 1

                    val bitmap = Bitmap.createBitmap(artistBitMapSize, artistBitMapSize, Bitmap.Config.RGB_565)
                    val canvas = Canvas(bitmap)

                    var x = 0F
                    var y = 0F

                    var j = 0
                    while (j < streams.size && j < nbTiles) {
                        val tempBitmap = ImageUtil.resize(streams[j], resize, resize)
                        canvas.drawBitmap(tempBitmap, x, y, null)
                        x += resize

                        if (x >= artistBitMapSize) {
                            x = 0F
                            y += resize
                        }
                        ++j
                    }

                    val bos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
                    result = ByteArrayInputStream(bos.toByteArray())

                } else if (nbImages > 0) {
                    var maxEntryYear: Map.Entry<InputStream, Int>? = null
                    for (entry in images.entries) {
                        if (maxEntryYear == null || entry.value
                                        .compareTo(maxEntryYear.value) > 0) {
                            maxEntryYear = entry
                        }

                    }
                    result = if (maxEntryYear != null) {
                        maxEntryYear.key
                    } else {
                        images.entries
                                .iterator()
                                .next()
                                .key
                    }
                }
            }
        } finally {
            retriever.release()
            try {
                for (stream in streams) {
                    stream.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return result

    }

    override fun getId(): String {
        println("MOSAIC get id for" + artistImage.artistName)
        // never return NULL here!
        // this id is used to determine whether the image is already cached
        // we use the artist name as well as the album years + file paths
        return artistImage.toIdString() + "ignoremediastore:" + ignoreMediaStore
    }

}

class ArtistImageLoader(
        private val context: Context
) : StreamModelLoader<ArtistImage> {

    override fun getResourceFetcher(model: ArtistImage, width: Int, height: Int): DataFetcher<InputStream> {

        return ArtistImageFetcher(model, PreferenceUtil.getInstance(context).ignoreMediaStoreArtwork())
    }

    class Factory : ModelLoaderFactory<ArtistImage, InputStream> {

        override fun build(context: Context, factories: GenericLoaderFactory): ModelLoader<ArtistImage, InputStream> {
            return ArtistImageLoader(context)
        }

        override fun teardown() {

        }
    }
}
/*

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
}*/
