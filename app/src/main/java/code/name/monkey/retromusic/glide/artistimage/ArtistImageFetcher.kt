package code.name.monkey.retromusic.glide.artistimage

import android.content.Context
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import code.name.monkey.retromusic.util.MusicUtil
import java.io.FileNotFoundException
import java.io.InputStream


class ArtistImageFetcher(
    private val context: Context,
    val model: ArtistImage,
) : DataFetcher<InputStream> {

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.REMOTE
    }

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        if (!MusicUtil.isArtistNameUnknown(model.artist.name)) {
            callback.onDataReady(getFallbackAlbumImage())
        }
    }

    private fun getFallbackAlbumImage(): InputStream? {
        model.artist.safeGetFirstAlbum().id.let { id->
            return if (id != -1L) {
                val imageUri = MusicUtil.getMediaStoreAlbumCoverUri(model.artist.safeGetFirstAlbum().id)
                try {
                    context.contentResolver.openInputStream(imageUri)
                } catch (e: FileNotFoundException){
                    null
                } catch (e: UnsupportedOperationException) {
                    null
                }
            } else {
                null
            }
        }
    }

    override fun cleanup() {
    }

    override fun cancel() {
    }
}
