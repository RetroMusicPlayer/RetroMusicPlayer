package ru.stersh.apisonic.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class NetworkFactory(
    authInterceptor: AuthenticationInterceptor
) {

    private val okHttpClient: OkHttpClient
    private val retrofitBuilder: Retrofit.Builder

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        val moshi = Moshi.Builder().build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .client(okHttpClient)

    }

    fun <T> createApi(
        apiClass: Class<T>,
        baseUrl: String
    ): T {
        return retrofitBuilder
            .baseUrl(baseUrl)
            .build()
            .create(apiClass)
    }
}