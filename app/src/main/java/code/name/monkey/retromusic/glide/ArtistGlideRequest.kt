package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.artistimage.ArtistImage
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.util.ArtistSignatureUtil
import code.name.monkey.retromusic.util.CustomArtistImageUtil
import com.bumptech.glide.*
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target

object ArtistGlideRequest {

    private const val DEFAULT_ANIMATION = android.R.anim.fade_in
    private val DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL
    private const val DEFAULT_ERROR_IMAGE = R.drawable.default_artist_art

    private fun createBaseRequest(requestManager: RequestManager, artist: Artist, noCustomImage: Boolean, forceDownload: Boolean): DrawableTypeRequest<*> {
        val hasCustomImage = CustomArtistImageUtil.getInstance(App.instance)
                .hasCustomArtistImage(artist)
        return if (noCustomImage || !hasCustomImage) {
            requestManager.load(ArtistImage(artist.name, forceDownload))
        } else {
            requestManager.load(CustomArtistImageUtil.getFile(artist))
        }
    }

    private fun createSignature(artist: Artist): Key {
        return ArtistSignatureUtil.getInstance(App.instance)
                .getArtistSignature(artist.name)
    }

    class Builder private constructor(internal val requestManager: RequestManager, internal val artist: Artist) {
        internal var noCustomImage: Boolean = false
        internal var forceDownload: Boolean = false

        fun generatePalette(context: Context): PaletteBuilder {
            return PaletteBuilder(this, context)
        }

        fun asBitmap(): BitmapBuilder {
            return BitmapBuilder(this)
        }

        fun noCustomImage(noCustomImage: Boolean): Builder {
            this.noCustomImage = noCustomImage
            return this
        }

        fun forceDownload(forceDownload: Boolean): Builder {
            this.forceDownload = forceDownload
            return this
        }

        fun build(): DrawableRequestBuilder<out Any> {
            return createBaseRequest(requestManager, artist, noCustomImage, forceDownload)
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .priority(Priority.LOW)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .signature(createSignature(artist))
        }

        companion object {

            fun from(requestManager: RequestManager, artist: Artist): Builder {
                return Builder(requestManager, artist)
            }
        }
    }

    class BitmapBuilder internal constructor(private val builder: Builder) {

        fun build(): BitmapRequestBuilder<*, Bitmap> {

            return createBaseRequest(builder.requestManager, builder.artist, builder.noCustomImage,
                    builder.forceDownload)
                    .asBitmap()
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .priority(Priority.LOW)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .signature(createSignature(builder.artist))
        }
    }

    class PaletteBuilder internal constructor(private val builder: Builder, internal val context: Context) {

        fun build(): BitmapRequestBuilder<*, BitmapPaletteWrapper> {

            return createBaseRequest(builder.requestManager, builder.artist, builder.noCustomImage,
                    builder.forceDownload)
                    .asBitmap()
                    .transcode(BitmapPaletteTranscoder(context), BitmapPaletteWrapper::class.java)

                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .priority(Priority.LOW)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .signature(createSignature(builder.artist))
        }
    }
}