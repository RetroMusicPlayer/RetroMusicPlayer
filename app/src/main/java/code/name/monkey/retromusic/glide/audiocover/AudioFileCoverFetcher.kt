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

/**
 * Fetcher for loading individual song cover art from audio files.
 * 
 * This fetcher implements a robust fallback strategy:
 * 1. Primary: Extract embedded artwork from the audio file using MediaMetadataRetriever
 * 2. Secondary: Use JAudioTagger for additional metadata format support
 * 3. Tertiary: Look for external cover art files in the same directory
 * 
 * All operations are performed with comprehensive error handling to ensure
 * the cover art system remains stable even with corrupted or inaccessible files.
 */
class AudioFileCoverFetcher(private val model: AudioFileCover) : DataFetcher<InputStream> {
    
    private var stream: InputStream? = null
    
    companion object {
        private const val TAG = "AudioFileCoverFetcher"
    }
    
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        var retriever: MediaMetadataRetriever? = null
        
        try {
            // Primary method: Use MediaMetadataRetriever for embedded artwork
            retriever = MediaMetadataRetriever()
            retriever.setDataSource(model.filePath)
            
            val embeddedPicture = retriever.embeddedPicture
            if (embeddedPicture != null && embeddedPicture.isNotEmpty()) {
                stream = ByteArrayInputStream(embeddedPicture)
                callback.onDataReady(stream!!)
                return
            }
            
            // Secondary method: Try fallback methods (JAudioTagger + external files)
            stream = AudioFileCoverUtils.fallback(model.filePath)
            if (stream != null) {
                callback.onDataReady(stream!!)
                return
            }
            
            // No artwork found - let Glide handle placeholder/error drawable
            callback.onLoadFailed(
                NoSuchElementException("No cover art found for file: ${model.filePath}")
            )
            
        } catch (e: SecurityException) {
            // Handle permission-related issues
            handleFallbackOrFail(callback, e, "Permission denied accessing file")
        } catch (e: IllegalArgumentException) {
            // Handle invalid file path or format issues
            handleFallbackOrFail(callback, e, "Invalid file path or format")
        } catch (e: RuntimeException) {
            // Handle MediaMetadataRetriever runtime exceptions
            handleFallbackOrFail(callback, e, "MediaMetadataRetriever error")
        } catch (e: Exception) {
            // Handle any other unexpected exceptions
            handleFallbackOrFail(callback, e, "Unexpected error during artwork extraction")
        } finally {
            // Ensure MediaMetadataRetriever is always released
            retriever?.let { safeReleaseRetriever(it) }
        }
    }
    
    /**
     * Attempts fallback artwork loading when primary method fails.
     * If fallback also fails, reports the original exception to Glide.
     */
    private fun handleFallbackOrFail(
        callback: DataFetcher.DataCallback<in InputStream>,
        originalException: Exception,
        context: String
    ) {
        try {
            stream = AudioFileCoverUtils.fallback(model.filePath)
            if (stream != null) {
                callback.onDataReady(stream!!)
            } else {
                callback.onLoadFailed(
                    RuntimeException("$context: ${originalException.message}", originalException)
                )
            }
        } catch (fallbackException: Exception) {
            // If fallback also fails, report the original exception with context
            callback.onLoadFailed(
                RuntimeException(
                    "$context. Fallback also failed: ${fallbackException.message}",
                    originalException
                )
            )
        }
    }
    
    /**
     * Safely releases MediaMetadataRetriever, ignoring any exceptions.
     */
    private fun safeReleaseRetriever(retriever: MediaMetadataRetriever) {
        try {
            retriever.release()
        } catch (e: Exception) {
            // Ignore release exceptions - nothing we can do about them
            // and they shouldn't affect the overall operation
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