package code.name.monkey.retromusic.rest

import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.rest.service.LastFMService
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object LastFmClient {
    private const val baseUrl = "https://ws.audioscrobbler.com/2.0/"

    private var lastFMService: LastFMService

    fun getApiService(): LastFMService {
        return lastFMService
    }

    init {
        lastFMService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(createDefaultOkHttpClientBuilder().build())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(LastFMService::class.java)
    }

    private fun createDefaultOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
            .retryOnConnectionFailure(true)
            .connectTimeout(1, TimeUnit.MINUTES) // connect timeout
            .writeTimeout(1, TimeUnit.MINUTES) // write timeout
            .readTimeout(1, TimeUnit.MINUTES) // read timeout
            .cache(createDefaultCache())
            .addInterceptor(createCacheControlInterceptor())
            .addInterceptor(createLogInterceptor())
    }

    private fun createLogInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

    private fun createCacheControlInterceptor(): Interceptor {
        return Interceptor { chain: Chain ->
            val modifiedRequest = chain.request().newBuilder()
                .addHeader("Cache-Control", "max-age=31536000, max-stale=31536000")
                .build()
            chain.proceed(modifiedRequest)
        }
    }

    private fun createDefaultCache(): Cache? {
        val cacheDir = File(App.getContext().cacheDir.absolutePath, "/okhttp-lastfm/")
        if (cacheDir.mkdirs() || cacheDir.isDirectory) {
            return Cache(cacheDir, 1024 * 1024 * 10)
        }
        return null
    }
}