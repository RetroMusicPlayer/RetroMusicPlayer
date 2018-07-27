package code.name.monkey.retromusic.glide.artistimage;

import android.content.Context;

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import code.name.monkey.retromusic.rest.LastFMRestClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;



public class ArtistImageLoader implements StreamModelLoader<ArtistImage> {
    // we need these very low values to make sure our artist image loading calls doesn't block the image loading queue
    private static final int TIMEOUT = 500;

    private Context context;
    private LastFMRestClient lastFMClient;
    private ModelLoader<GlideUrl, InputStream> urlLoader;

    public ArtistImageLoader(Context context, LastFMRestClient lastFMRestClient, ModelLoader<GlideUrl, InputStream> urlLoader) {
        this.context = context;
        this.lastFMClient = lastFMRestClient;
        this.urlLoader = urlLoader;
    }

    @Override
    public DataFetcher<InputStream> getResourceFetcher(ArtistImage model, int width, int height) {
        return new ArtistImageFetcher(context, lastFMClient, model, urlLoader, width, height);
    }

    public static class Factory implements ModelLoaderFactory<ArtistImage, InputStream> {
        private LastFMRestClient lastFMClient;
        private OkHttpUrlLoader.Factory okHttpFactory;

        public Factory(Context context) {
            okHttpFactory = new OkHttpUrlLoader.Factory(new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build());
            lastFMClient = new LastFMRestClient(LastFMRestClient.createDefaultOkHttpClientBuilder(context)
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build());
        }

        @Override
        public ModelLoader<ArtistImage, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new ArtistImageLoader(context, lastFMClient, okHttpFactory.build(context, factories));
        }

        @Override
        public void teardown() {
            okHttpFactory.teardown();
        }
    }
}

