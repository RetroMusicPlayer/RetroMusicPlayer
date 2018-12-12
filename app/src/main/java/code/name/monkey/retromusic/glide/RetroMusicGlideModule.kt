package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import code.name.monkey.retromusic.glide.artistimage.ArtistImage
import code.name.monkey.retromusic.glide.artistimage.ArtistImageLoader
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.glide.audiocover.AudioFileCoverLoader
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import java.io.InputStream

@GlideModule
class RetroMusicGlideModule : AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide,
                                    registry: Registry) {
        registry.append(AudioFileCover::class.java, InputStream::class.java, AudioFileCoverLoader.Factory())
        registry.append(ArtistImage::class.java, InputStream::class.java, ArtistImageLoader.Factory(context))
        registry.register(Bitmap::class.java, BitmapPaletteWrapper::class.java, BitmapPaletteTranscoder())
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
