package code.name.monkey.retromusic.glide

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.module.GlideModule
import code.name.monkey.retromusic.glide.artistimage.ArtistImage
import code.name.monkey.retromusic.glide.artistimage.ArtistImageLoader
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.glide.audiocover.AudioFileCoverLoader

import java.io.InputStream


class RetroMusicGlideModule : GlideModule {
    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }

    override fun registerComponents(context: Context, glide: Glide) {
        glide.register(AudioFileCover::class.java, InputStream::class.java, AudioFileCoverLoader.Factory())
        glide.register(ArtistImage::class.java, InputStream::class.java, ArtistImageLoader.Factory(context))
    }
}
