package code.name.monkey.retromusic.activities.artists

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ArtistDetailsViewModelFactory(
    private val application: Application,
    private val artistId: Int
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ArtistDetailsViewModel::class.java)) {
            ArtistDetailsViewModel(application, artistId) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}