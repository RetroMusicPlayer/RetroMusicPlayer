package code.name.monkey.retromusic.glide.audiocover;

import android.content.Context;

import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.StreamModelLoader;

import java.io.InputStream;


public class AudioFileCoverLoader implements StreamModelLoader<AudioFileCover> {

    @Override
    public DataFetcher<InputStream> getResourceFetcher(AudioFileCover model, int width, int height) {
        return new AudioFileCoverFetcher(model);
    }

    public static class Factory implements ModelLoaderFactory<AudioFileCover, InputStream> {
        @Override
        public ModelLoader<AudioFileCover, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new AudioFileCoverLoader();
        }

        @Override
        public void teardown() {
        }
    }
}

