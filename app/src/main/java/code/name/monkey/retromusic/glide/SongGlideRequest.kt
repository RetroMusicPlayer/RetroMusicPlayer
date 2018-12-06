package code.name.monkey.retromusic.glide

import android.content.Context
import android.graphics.Bitmap
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.glide.palette.BitmapPaletteTranscoder
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.BitmapRequestBuilder
import com.bumptech.glide.DrawableRequestBuilder
import com.bumptech.glide.DrawableTypeRequest
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.MediaStoreSignature


object SongGlideRequest {

    internal val DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE
    internal const val DEFAULT_ANIMATION = android.R.anim.fade_in
    internal const val DEFAULT_ERROR_IMAGE = R.drawable.default_album_art

    internal fun createBaseRequest(requestManager: RequestManager, song: Song,
                                   ignoreMediaStore: Boolean): DrawableTypeRequest<*> {
        return if (ignoreMediaStore) {
            requestManager.load(AudioFileCover(song.data!!))
        } else {
            requestManager.loadFromMediaStore(MusicUtil.getMediaStoreAlbumCoverUri(song.albumId))
        }
    }

    internal fun createSignature(song: Song): Key {
        return MediaStoreSignature("", song.dateModified, 0)
    }

    class Builder private constructor(internal val requestManager: RequestManager, internal val song: Song) {
        internal var ignoreMediaStore: Boolean = false

        fun generatePalette(context: Context): PaletteBuilder {
            return PaletteBuilder(this, context)
        }

        fun asBitmap(): BitmapBuilder {
            return BitmapBuilder(this)
        }

        fun checkIgnoreMediaStore(context: Context): Builder {
            return ignoreMediaStore(PreferenceUtil.getInstance().ignoreMediaStoreArtwork())
        }

        fun ignoreMediaStore(ignoreMediaStore: Boolean): Builder {
            this.ignoreMediaStore = ignoreMediaStore
            return this
        }

        fun build(): DrawableRequestBuilder<out Any> {
            return createBaseRequest(requestManager, song, ignoreMediaStore)
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(song))
        }

        companion object {

            fun from(requestManager: RequestManager, song: Song): Builder {
                return Builder(requestManager, song)
            }
        }
    }

    class BitmapBuilder internal constructor(private val builder: Builder) {

        fun build(): BitmapRequestBuilder<*, Bitmap> {

            return createBaseRequest(builder.requestManager, builder.song, builder.ignoreMediaStore)
                    .asBitmap()
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(builder.song))
        }
    }

    class PaletteBuilder internal constructor(private val builder: Builder, internal val context: Context) {

        fun build(): BitmapRequestBuilder<*, BitmapPaletteWrapper> {

            return createBaseRequest(builder.requestManager, builder.song, builder.ignoreMediaStore)
                    .asBitmap()
                    .transcode(BitmapPaletteTranscoder(context), BitmapPaletteWrapper::class.java)
                    .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
                    .error(DEFAULT_ERROR_IMAGE)
                    .animate(DEFAULT_ANIMATION)
                    .signature(createSignature(builder.song))
        }
    }
}
