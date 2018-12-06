package code.name.monkey.retromusic.glide.artistimage

import android.content.Context

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.stream.StreamModelLoader
import code.name.monkey.retromusic.rest.LastFMRestClient

import java.io.InputStream
import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient


class ArtistImageLoader(private val context: Context, private val lastFMClient: LastFMRestClient, private val urlLoader: ModelLoader<GlideUrl, InputStream>) : StreamModelLoader<ArtistImage> {

    override fun getResourceFetcher(model: ArtistImage, width: Int, height: Int): DataFetcher<InputStream> {
        return ArtistImageFetcher(context, lastFMClient, model, urlLoader, width, height)
    }

    class Factory(context: Context) : ModelLoaderFactory<ArtistImage, InputStream> {
        private val lastFMClient: LastFMRestClient = LastFMRestClient(LastFMRestClient.createDefaultOkHttpClientBuilder(context)
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build())
        private val okHttpFactory: OkHttpUrlLoader.Factory = OkHttpUrlLoader.Factory(OkHttpClient.Builder()
                .connectTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT.toLong(), TimeUnit.MILLISECONDS)
                .build())

        override fun build(context: Context, factories: GenericLoaderFactory): ModelLoader<ArtistImage, InputStream> {
            return ArtistImageLoader(context, lastFMClient, okHttpFactory.build(context, factories))
        }

        override fun teardown() {
            okHttpFactory.teardown()
        }
    }

    companion object {
        // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
        private const val TIMEOUT = 500
    }
}

