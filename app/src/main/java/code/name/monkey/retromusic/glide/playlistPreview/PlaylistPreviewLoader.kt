package code.name.monkey.retromusic.glide.playlistPreview

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoader.LoadData
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey

class PlaylistPreviewLoader(val context: Context) : ModelLoader<PlaylistPreview, Bitmap> {
    override fun buildLoadData(
        model: PlaylistPreview,
        width: Int,
        height: Int,
        options: Options
    ): LoadData<Bitmap> {
        return LoadData(
            ObjectKey(model),
            PlaylistPreviewFetcher(context, model)
        )
    }

    override fun handles(model: PlaylistPreview): Boolean {
        return true
    }

    class Factory(val context: Context) : ModelLoaderFactory<PlaylistPreview, Bitmap> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<PlaylistPreview, Bitmap> {
            return PlaylistPreviewLoader(context)
        }

        override fun teardown() {}
    }
}
