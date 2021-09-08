package code.name.monkey.retromusic.glide

import android.graphics.drawable.Drawable
import code.name.monkey.appthemehelper.ThemeStore.Companion.accentColor
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.App.Companion.getContext
import code.name.monkey.retromusic.Constants.USER_BANNER
import code.name.monkey.retromusic.Constants.USER_PROFILE
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.artistimage.ArtistImage
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.glide.palette.BitmapPaletteWrapper
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ArtistSignatureUtil
import code.name.monkey.retromusic.util.CustomArtistImageUtil.Companion.getFile
import code.name.monkey.retromusic.util.CustomArtistImageUtil.Companion.getInstance
import code.name.monkey.retromusic.util.MusicUtil.getMediaStoreAlbumCoverUri
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Priority
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.annotation.GlideExtension
import com.bumptech.glide.annotation.GlideOption
import com.bumptech.glide.annotation.GlideType
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import com.bumptech.glide.signature.MediaStoreSignature
import java.io.File


@GlideExtension
object RetroGlideExtension {

    private const val DEFAULT_ERROR_ARTIST_IMAGE =
        R.drawable.default_artist_art
    private const val DEFAULT_ERROR_SONG_IMAGE: Int = R.drawable.default_audio_art
    private const val DEFAULT_ERROR_ALBUM_IMAGE = R.drawable.default_album_art
    private const val DEFAULT_ERROR_IMAGE_BANNER = R.drawable.material_design_default

    private val DEFAULT_DISK_CACHE_STRATEGY_ARTIST = DiskCacheStrategy.RESOURCE
    private val DEFAULT_DISK_CACHE_STRATEGY = DiskCacheStrategy.NONE

    private const val DEFAULT_ANIMATION = android.R.anim.fade_in

    @JvmStatic
    @GlideType(BitmapPaletteWrapper::class)
    fun asBitmapPalette(requestBuilder: RequestBuilder<BitmapPaletteWrapper>): RequestBuilder<BitmapPaletteWrapper> {
        return requestBuilder
    }

    private fun getSongModel(song: Song, ignoreMediaStore: Boolean): Any {
        return if (ignoreMediaStore) {
            AudioFileCover(song.data)
        } else {
            getMediaStoreAlbumCoverUri(song.albumId)
        }
    }

    fun getSongModel(song: Song): Any {
        return getSongModel(song, PreferenceUtil.isIgnoreMediaStoreArtwork)
    }

    fun getArtistModel(artist: Artist): Any {
        return getArtistModel(
            artist,
            getInstance(getContext()).hasCustomArtistImage(artist),
            false
        )
    }

    fun getArtistModel(artist: Artist, forceDownload: Boolean): Any {
        return getArtistModel(
            artist,
            getInstance(getContext()).hasCustomArtistImage(artist),
            forceDownload
        )
    }

    private fun getArtistModel(artist: Artist, hasCustomImage: Boolean, forceDownload: Boolean): Any {
        return if (!hasCustomImage) {
            ArtistImage(artist)
        } else {
            getFile(artist)
        }
    }

    @JvmStatic
    @GlideOption
    fun artistImageOptions(
        baseRequestOptions: BaseRequestOptions<*>,
        artist: Artist
    ): BaseRequestOptions<*> {
        return baseRequestOptions
            .diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY_ARTIST)
            .priority(Priority.LOW)
            .error(DEFAULT_ERROR_ARTIST_IMAGE)
            .override(SIZE_ORIGINAL, SIZE_ORIGINAL)
            .signature(createSignature(artist))
    }

    @JvmStatic
    @GlideOption
    fun songCoverOptions(
        baseRequestOptions: BaseRequestOptions<*>,
        song: Song
    ): BaseRequestOptions<*> {
        return baseRequestOptions.diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
            .error(DEFAULT_ERROR_SONG_IMAGE)
            .signature(createSignature(song))
    }

    @JvmStatic
    @GlideOption
    fun albumCoverOptions(
        baseRequestOptions: BaseRequestOptions<*>,
        song: Song
    ): BaseRequestOptions<*> {
        return baseRequestOptions.diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
            .error(DEFAULT_ERROR_ALBUM_IMAGE)
            .signature(createSignature(song))
    }

    @JvmStatic
    @GlideOption
    fun userProfileOptions(
        baseRequestOptions: BaseRequestOptions<*>,
        file: File
    ): BaseRequestOptions<*> {
        return baseRequestOptions.diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
            .error(getErrorUserProfile())
            .signature(createSignature(file))
    }

    @JvmStatic
    @GlideOption
    fun profileBannerOptions(
        baseRequestOptions: BaseRequestOptions<*>,
        file: File
    ): BaseRequestOptions<*> {
        return baseRequestOptions.diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
            .placeholder(DEFAULT_ERROR_IMAGE_BANNER)
            .error(DEFAULT_ERROR_IMAGE_BANNER)
            .signature(createSignature(file))
    }

    @JvmStatic
    @GlideOption
    fun playlistOptions(
        baseRequestOptions: BaseRequestOptions<*>
    ): BaseRequestOptions<*> {
        return baseRequestOptions.diskCacheStrategy(DEFAULT_DISK_CACHE_STRATEGY)
            .error(DEFAULT_ERROR_ALBUM_IMAGE)
    }

    private fun createSignature(song: Song): Key {
        return MediaStoreSignature("", song.dateModified, 0)
    }

    private fun createSignature(file: File): Key {
        return MediaStoreSignature("", file.lastModified(), 0)
    }

    private fun createSignature(artist: Artist): Key {
        return ArtistSignatureUtil.getInstance(getContext())
            .getArtistSignature(artist.name)
    }

    fun getUserModel(): File {
        val dir = getContext().filesDir
        return File(dir, USER_PROFILE)
    }

    fun getBannerModel(): File {
        val dir = getContext().filesDir
        return File(dir, USER_BANNER)
    }

    private fun getErrorUserProfile(): Drawable {
        return TintHelper.createTintedDrawable(
            getContext(),
            R.drawable.ic_account,
            accentColor(getContext())
        )
    }

    fun <TranscodeType> getDefaultTransition(): GenericTransitionOptions<TranscodeType> {
        return GenericTransitionOptions<TranscodeType>().transition(DEFAULT_ANIMATION)
    }
}