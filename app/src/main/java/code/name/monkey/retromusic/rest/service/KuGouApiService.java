package code.name.monkey.retromusic.rest.service;

import code.name.monkey.retromusic.rest.model.KuGouRawLyric;
import code.name.monkey.retromusic.rest.model.KuGouSearchLyricResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hemanths on 28/07/17.
 */

public interface KuGouApiService {

    @GET("search?ver=1&man=yes&client=pc")
    Observable<KuGouSearchLyricResult> searchLyric(@Query("keyword") String songName, @Query("duration") String duration);

    @GET("download?ver=1&client=pc&fmt=lrc&charset=utf8")
    Observable<KuGouRawLyric> getRawLyric(@Query("id") String id, @Query("accesskey") String accesskey);
}
