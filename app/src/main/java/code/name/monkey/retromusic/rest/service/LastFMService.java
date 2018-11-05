package code.name.monkey.retromusic.rest.service;

import androidx.annotation.Nullable;
import code.name.monkey.retromusic.rest.model.LastFmAlbum;
import code.name.monkey.retromusic.rest.model.LastFmArtist;
import code.name.monkey.retromusic.rest.model.LastFmTrack;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public interface LastFMService {
    String API_KEY_BAK = "bd9c6ea4d55ec9ed3af7d276e5ece304";
    String API_KEY = "c679c8d3efa84613dc7dcb2e8d42da4c";
    String BASE_QUERY_PARAMETERS = "?format=json&autocorrect=1&api_key=" + API_KEY;
    String METHOD_TRACK = "track.getInfo";

    @GET(BASE_QUERY_PARAMETERS + "&method=album.getinfo")
    Observable<LastFmAlbum> getAlbumInfo(@Query("album") String albumName, @Query("artist") String artistName, @Nullable @Query("lang") String language);

    @GET("?api_key=" + API_KEY + "&format=json&autocorrect=1" + "&method=" + METHOD_TRACK)
    Observable<LastFmTrack> getTrackInfo(@Query("artist") String artist, @Query("track") String track);

    @GET(BASE_QUERY_PARAMETERS + "&method=artist.getinfo")
    Call<LastFmArtist> getArtistInfo(@Query("artist") String artistName, @Nullable @Query("lang") String language, @Nullable @Header("Cache-Control") String cacheControl);
}