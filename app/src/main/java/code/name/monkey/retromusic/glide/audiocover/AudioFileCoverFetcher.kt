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
package code.name.monkey.retromusic.glide.audiocover

import android.media.MediaMetadataRetriever
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {
    private var stream: InputStream? = null
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(model.filePath)
            val picture = retriever.embeddedPicture
            stream = if (picture != null) {
                ByteArrayInputStream(picture)
            } else {
                AudioFileCoverUtils.fallback(model.filePath)
            }
            callback.onDataReady(stream)
        } catch (e: FileNotFoundException) {
            callback.onLoadFailed(e)
        } finally {
            retriever.release()
        }
    }

    override fun cleanup() {
        // already cleaned up in loadData and ByteArrayInputStream will be GC'd
        if (stream != null) {
            try {
                stream?.close()
            } catch (ignore: IOException) {
                // can't do much about it
            }
        }
    }

    override fun cancel() {
        // cannot cancel
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}