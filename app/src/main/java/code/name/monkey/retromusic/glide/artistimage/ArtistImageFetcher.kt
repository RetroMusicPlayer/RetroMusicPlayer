package code.name.monkey.retromusic.glide.artistimage

import android.content.Context

import com.bumptech.glide.Priority
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmArtist

import code.name.monkey.retromusic.util.LastFMUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil

import java.io.IOException
import java.io.InputStream

import retrofit2.Response


class ArtistImageFetcher(private val context: Context, private val lastFMRestClient: LastFMRestClient, private val model: ArtistImage, private val urlLoader: ModelLoader<GlideUrl, InputStream>, private val width: Int, private val height: Int) : DataFetcher<InputStream> {
    @Volatile
    private var isCancelled: Boolean = false
    private var urlFetcher: DataFetcher<InputStream>? = null

    override fun getId(): String {
        // makes sure we never ever return null here
        return model.artistName
    }

    @Throws(Exception::class)
    override fun loadData(priority: Priority): InputStream? {
        if (!MusicUtil.isArtistNameUnknown(model.artistName) && RetroUtil.isAllowedToDownloadMetadata(context)) {
            val response = lastFMRestClient.apiService.getArtistInfo(model.artistName, null, if (model.skipOkHttpCache) "no-cache" else null).execute()

            if (!response.isSuccessful) {
                throw IOException("Request failed with code: " + response.code())
            }

            val lastFmArtist = response.body()

            if (isCancelled) return null

            val url = GlideUrl(LastFMUtil.getLargestArtistImageUrl(lastFmArtist!!.artist.image))
            urlFetcher = urlLoader.getResourceFetcher(url, width, height)

            return urlFetcher!!.loadData(priority)
        }
        return null
    }

    override fun cleanup() {
        if (urlFetcher != null) {
            urlFetcher!!.cleanup()
        }
    }

    override fun cancel() {
        isCancelled = true
        if (urlFetcher != null) {
            urlFetcher!!.cancel()
        }
    }

    companion object {
        val TAG = ArtistImageFetcher::class.java.simpleName
    }
}
