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

import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import java.io.InputStream

/**
 * Glide ModelLoader for loading cover art from individual audio files.
 * 
 * This loader integrates with Glide's loading pipeline to provide seamless
 * individual song artwork loading with proper caching and error handling.
 * It creates the appropriate fetcher and cache key for each audio file.
 */
class AudioFileCoverLoader : ModelLoader<AudioFileCover, InputStream> {
    
    /**
     * Builds load data for the given audio file cover model.
     * 
     * @param audioFileCover The model containing the audio file path
     * @param width Requested width (unused for audio covers)
     * @param height Requested height (unused for audio covers)  
     * @param options Additional loading options
     * @return LoadData containing the cache key and fetcher
     */
    override fun buildLoadData(
        audioFileCover: AudioFileCover,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<InputStream> {
        return LoadData(
            ObjectKey(audioFileCover.filePath), 
            AudioFileCoverFetcher(audioFileCover)
        )
    }

    /**
     * Determines if this loader can handle the given model.
     * 
     * @param audioFileCover The model to check
     * @return Always true as this loader handles all AudioFileCover instances
     */
    override fun handles(audioFileCover: AudioFileCover): Boolean = true

    /**
     * Factory for creating AudioFileCoverLoader instances.
     * Required by Glide's ModelLoader registration system.
     */
    class Factory : ModelLoaderFactory<AudioFileCover, InputStream> {
        
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AudioFileCover, InputStream> {
            return AudioFileCoverLoader()
        }

        override fun teardown() {
            // No resources to clean up
        }
    }
}