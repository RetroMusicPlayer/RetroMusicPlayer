package code.name.monkey.retromusic.network

import android.content.Context
import code.name.monkey.retromusic.model.DeezerResponse
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.*

private const val BASE_QUERY_ARTIST = "search/artist"
private const val BASE_URL = "https://api.deezer.com/"

interface DeezerService {

    @GET("$BASE_QUERY_ARTIST&limit=1")
    fun getArtistImage(
        @Query("q") artistName: String
    ): Call<DeezerResponse>

    companion object {
        operator fun invoke(
            client: okhttp3.Call.Factory
        ): DeezerService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create()
        }

        fun createDefaultOkHttpClient(
            context: Context
        ): OkHttpClient.Builder = OkHttpClient.Builder()
            .cache(createDefaultCache(context))
            .addInterceptor(createCacheControlInterceptor())

        private fun createDefaultCache(
            context: Context
        ): Cache? {
            val cacheDir = File(context.applicationContext.cacheDir.absolutePath, "/okhttp-deezer/")
            if (cacheDir.mkdir() or cacheDir.isDirectory) {
                return Cache(cacheDir, 1024 * 1024 * 10)
            }
            return null
        }

        private fun createCacheControlInterceptor(): Interceptor {
            return Interceptor { chain ->
                val modifiedRequest = chain.request().newBuilder()
                    .addHeader(
                        "Cache-Control",
                        String.format(
                            Locale.getDefault(),
                            "max-age=31536000, max-stale=31536000"
                        )
                    ).build()
                chain.proceed(modifiedRequest)
            }
        }
    }
}
