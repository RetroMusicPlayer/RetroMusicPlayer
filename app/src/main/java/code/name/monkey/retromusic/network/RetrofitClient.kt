package code.name.monkey.retromusic.network

import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.Constants.AUDIO_SCROBBLER_URL
import com.google.gson.Gson
import okhttp3.Cache
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

private const val TIMEOUT: Long = 700

val networkModule = module {
    factory {
        provideHttpClient(get(), get())
    }
    factory {
        provideCacheControlInterceptor()
    }
    factory {
        provideDefaultCache()
    }
    factory {
        provideLastFmService(get())
    }
    single {
        providerRetrofit(get())
    }
}

fun provideLastFmService(retrofit: Retrofit): LastFMService =
    retrofit.create(LastFMService::class.java)

fun providerRetrofit(okHttpClient: OkHttpClient.Builder): Retrofit = Retrofit.Builder()
    .baseUrl(AUDIO_SCROBBLER_URL)
    .callFactory(okHttpClient.build())
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .build()

fun provideHttpClient(
    cache: Cache,
    interceptor: Interceptor
): OkHttpClient.Builder = OkHttpClient.Builder()
    .connectionPool(ConnectionPool(0, 1, TimeUnit.NANOSECONDS))
    .retryOnConnectionFailure(true)
    .connectTimeout(TIMEOUT, TimeUnit.MINUTES)
    .writeTimeout(TIMEOUT, TimeUnit.MINUTES)
    .readTimeout(TIMEOUT, TimeUnit.MINUTES)
    .cache(cache)
    .addInterceptor(interceptor)


fun provideCacheControlInterceptor(): Interceptor = Interceptor { chain: Interceptor.Chain ->
    val modifiedRequest = chain.request().newBuilder()
        .addHeader("Cache-Control", "max-age=31536000, max-stale=31536000")
        .build()
    chain.proceed(modifiedRequest)
}

fun provideDefaultCache(): Cache? {
    val cacheDir = File(App.getContext().cacheDir.absolutePath, "/okhttp-lastfm/")
    if (cacheDir.mkdirs() || cacheDir.isDirectory) {
        return Cache(cacheDir, 1024 * 1024 * 10)
    }
    return null
}
