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
import java.io.IOException
import java.io.InputStream

class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {
    
    private var stream: InputStream? = null
    
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        var retriever: MediaMetadataRetriever? = null
        
        try {
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(model.filePath)
            
            val embeddedPicture = retriever.embeddedPicture
            if (embeddedPicture != null && embeddedPicture.isNotEmpty()) {
                stream = ByteArrayInputStream(embeddedPicture)
                callback.onDataReady(stream!!)
                return
            }
            
            stream = AudioFileCoverUtils.fallback(model.filePath)
            if (stream != null) {
                callback.onDataReady(stream!!)
                return
            }
            
            callback.onLoadFailed(
                NoSuchElementException("No cover art found for file: ${model.filePath}")
            )
            
        } catch (e: Exception) {
            handleFallbackOrFail(callback, e)
        } finally {
            retriever?.let { safeReleaseRetriever(it) }
        }
    }
    
    private fun handleFallbackOrFail(
        callback: DataFetcher.DataCallback<in InputStream>,
        originalException: Exception
    ) {
        try {
            stream = AudioFileCoverUtils.fallback(model.filePath)
            if (stream != null) {
                callback.onDataReady(stream!!)
            } else {
                callback.onLoadFailed(originalException)
            }
        } catch (fallbackException: Exception) {
            callback.onLoadFailed(originalException)
        }
    }
    
    private fun safeReleaseRetriever(retriever: MediaMetadataRetriever) {
        try {
            retriever.release()
        } catch (e: Exception) {
        }
    }

    override fun cleanup() {
        if (stream != null) {
            try {
                stream?.close()
            } catch (ignore: IOException) {
            }
        }
    }

    override fun cancel() {
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}