package code.name.monkey.retromusic.glide.artistimage

import android.content.Context
import code.name.monkey.retromusic.rest.LastFMRestClient
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


class ArtistImageLoader(private val context: Context, private val lastFMClient: LastFMRestClient, private val okhttp: OkHttpClient) : ModelLoader<ArtistImage, InputStream> {

    override fun buildLoadData(model: ArtistImage, width: Int, height: Int, options: Options): ModelLoader.LoadData<InputStream>? {
        return ModelLoader.LoadData(ObjectKey(model.artistName), ArtistImageFetcher(context, lastFMClient, okhttp, model, width, height))
    }

    override fun handles(model: ArtistImage): Boolean {
        return true
    }

    class Factory(private val context: Context) : ModelLoaderFactory<ArtistImage, InputStream> {
        private val lastFMClient: LastFMRestClient = LastFMRestClient(LastFMRestClient.createDefaultOkHttpClientBuilder(context)
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build())
        private val okHttp: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build()


        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<ArtistImage, InputStream> {
            return ArtistImageLoader(context, lastFMClient, okHttp)
        }

        override fun teardown() {}
    }

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT = 500
    }
}