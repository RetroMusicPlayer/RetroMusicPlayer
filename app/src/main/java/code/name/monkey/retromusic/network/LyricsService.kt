package code.name.monkey.retromusic.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface LyricsRestService {

    @Headers("Cache-Control: public")
    @GET("/lyrics")
    suspend fun getLyrics(@Query("artist") artist: String, @Query("title") title: String): String
}