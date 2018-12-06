package code.name.monkey.retromusic.glide.audiocover

import android.content.Context

import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.GenericLoaderFactory
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.stream.StreamModelLoader

import java.io.InputStream


class AudioFileCoverLoader : StreamModelLoader<AudioFileCover> {

    override fun getResourceFetcher(model: AudioFileCover, width: Int, height: Int): DataFetcher<InputStream> {
        return AudioFileCoverFetcher(model)
    }

    class Factory : ModelLoaderFactory<AudioFileCover, InputStream> {
        override fun build(context: Context, factories: GenericLoaderFactory): ModelLoader<AudioFileCover, InputStream> {
            return AudioFileCoverLoader()
        }

        override fun teardown() {}
    }
}

