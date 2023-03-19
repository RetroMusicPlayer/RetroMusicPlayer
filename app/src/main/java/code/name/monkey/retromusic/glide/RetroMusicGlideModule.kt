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
package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import code.name.monkey.retromusic.glide.artistimage.ArtistImage
import code.name.monkey.retromusic.glide.artistimage.Factory
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.glide.audiocover.AudioFileCoverLoader
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.glide.playlistPreview.PlaylistPreview
import code.name.monkey.retromusic.glide.playlistPreview.PlaylistPreviewLoader
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

@GlideModule
class RetroMusicGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(
            PlaylistPreview::class.java,
            Bitmap::class.java,
            PlaylistPreviewLoader.Factory(context)
        )
        registry.prepend(
            AudioFileCover::class.java,
            InputStream::class.java,
            AudioFileCoverLoader.Factory()
        )
        registry.prepend(ArtistImage::class.java, InputStream::class.java, Factory(context))
        registry.register(
            Bitmap::class.java,
            BitmapPaletteWrapper::class.java, BitmapPaletteTranscoder()
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}