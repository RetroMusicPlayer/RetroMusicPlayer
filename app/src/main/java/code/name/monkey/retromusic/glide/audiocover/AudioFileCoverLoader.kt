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

class AudioFileCoverLoader : ModelLoader<AudioFileCover, InputStream> {
    override fun buildLoadData(
        audioFileCover: AudioFileCover,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<InputStream> {
        return LoadData(ObjectKey(audioFileCover.filePath), AudioFileCoverFetcher(audioFileCover))
    }

    override fun handles(audioFileCover: AudioFileCover): Boolean {
        return true
    }

    class Factory : ModelLoaderFactory<AudioFileCover, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<AudioFileCover, InputStream> {
            return AudioFileCoverLoader()
        }

        override fun teardown() {}
    }
}