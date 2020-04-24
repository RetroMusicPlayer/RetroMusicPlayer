package code.name.monkey.retromusic.data

import code.name.monkey.retromusic.model.Contributor
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://github.com/h4h13/RetroMusicPlayer/blob/dev/data/"

interface RetroDataService {

    @GET("translators.json")
    suspend fun getContributors(): List<Contributor>

    @GET("translators.json")
    suspend fun getTranslators(): List<Contributor>

    companion object {
        val retoService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RetroDataService::class.java)
    }
}
