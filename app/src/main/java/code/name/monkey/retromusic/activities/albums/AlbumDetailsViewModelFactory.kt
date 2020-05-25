package code.name.monkey.retromusic.activities.albums

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AlbumDetailsViewModelFactory(
    private val application: Application,
    private val albumId: Int
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AlbumDetailsViewModel::class.java)) {
            AlbumDetailsViewModel(application, albumId) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}