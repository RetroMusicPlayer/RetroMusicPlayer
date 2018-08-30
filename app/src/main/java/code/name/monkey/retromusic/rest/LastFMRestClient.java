package code.name.monkey.retromusic.rest;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.rest.service.LastFMService;
import java.io.File;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class LastFMRestClient {

  private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";

  private LastFMService apiService;

  public LastFMRestClient(@NonNull Context context) {
    this(createDefaultOkHttpClientBuilder(context).build());
  }

  public LastFMRestClient(@NonNull Call.Factory client) {
    Retrofit restAdapter = new Retrofit.Builder()
        .baseUrl(BASE_URL)
        .callFactory(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();

    apiService = restAdapter.create(LastFMService.class);
  }

  @Nullable
  private static Cache createDefaultCache(Context context) {
    File cacheDir = new File(context.getCacheDir().getAbsolutePath(), "/okhttp-lastfm/");
    if (cacheDir.mkdirs() || cacheDir.isDirectory()) {
      return new Cache(cacheDir, 1024 * 1024 * 10);
    }
    return null;
  }

  private static Interceptor createCacheControlInterceptor() {
    return chain -> {
      Request modifiedRequest = chain.request().newBuilder()
          .addHeader("Cache-Control", String.format("max-age=%d, max-stale=%d", 31536000, 31536000))
          .build();
      return chain.proceed(modifiedRequest);
    };
  }

  public static OkHttpClient.Builder createDefaultOkHttpClientBuilder(Context context) {
    return new OkHttpClient.Builder()
        .cache(createDefaultCache(context))
        .addInterceptor(createCacheControlInterceptor());
  }

  public LastFMService getApiService() {
    return apiService;
  }
}
