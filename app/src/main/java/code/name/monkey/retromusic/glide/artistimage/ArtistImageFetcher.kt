package code.name.monkey.retromusic.glide.artistimage

import android.content.Context
import android.text.TextUtils
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmArtist
import code.name.monkey.retromusic.util.LastFMUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Priority
import com.bumptech.glide.integration.okhttp3.OkHttpStreamFetcher
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GlideUrl

import okhttp3.OkHttpClient
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.io.InputStream


class ArtistImageFetcher(private val context: Context, private val lastFMRestClient: LastFMRestClient, private val okHttp: OkHttpClient, private val model: ArtistImage, width: Int, height: Int) : DataFetcher<InputStream> {
    @Volatile
    private var isCancelled: Boolean = false
    private var call: Call<LastFmArtist>? = null
    private var streamFetcher: OkHttpStreamFetcher? = null


    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }


    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        try {
            if (!MusicUtil.isArtistNameUnknown(model.artistName) && RetroUtil.isAllowedToDownloadMetadata(context)) {
                call = lastFMRestClient.apiService.getArtistInfo(model.artistName, null, if (model.skipOkHttpCache) "no-cache" else null)
                call!!.enqueue(object : Callback<LastFmArtist> {
                    override fun onResponse(call: Call<LastFmArtist>, response: Response<LastFmArtist>) {
                        if (isCancelled) {
                            callback.onDataReady(null)
                            return
                        }

                        val lastFmArtist = response.body()
                        if (lastFmArtist == null || lastFmArtist.artist == null || lastFmArtist.artist.image == null) {
                            callback.onLoadFailed(Exception("No artist image url found"))
                            return
                        }

                        val url = LastFMUtil.getLargestArtistImageUrl(lastFmArtist.artist.image)
                        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url.trim { it <= ' ' })) {
                            callback.onLoadFailed(Exception("No artist image url found"))
                            return
                        }

                        streamFetcher = OkHttpStreamFetcher(okHttp, GlideUrl(url))
                        streamFetcher!!.loadData(priority, callback)
                    }

                    override fun onFailure(call: Call<LastFmArtist>, throwable: Throwable) {
                        callback.onLoadFailed(Exception(throwable))
                    }
                })


            }
        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }

    }

    override fun cleanup() {
        if (streamFetcher != null) {
            streamFetcher!!.cleanup()
        }
    }

    override fun cancel() {
        isCancelled = true
        if (call != null) {
            call!!.cancel()
        }
        if (streamFetcher != null) {
            streamFetcher!!.cancel()
        }
    }

    companion object {
        val TAG: String = ArtistImageFetcher::class.java.simpleName
    }
}