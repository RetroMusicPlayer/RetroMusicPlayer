package code.name.monkey.retromusic.feature.library.artist.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.feature.library.artist.domain.LibraryArtist
import code.name.monkey.retromusic.feature.library.artist.domain.LibraryArtistRepository
import ru.stersh.retrosonic.core.extensions.mapItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LibraryArtistViewModel(private val repository: LibraryArtistRepository) : ViewModel() {
    private val _artists = MutableStateFlow<List<LibraryArtistUi>>(emptyList())
    val artists: Flow<List<LibraryArtistUi>>
        get() = _artists

    init {
        viewModelScope.launch {
            repository
                .getArtists()
                .mapItems { it.toPresentation() }
                .collect { _artists.value = it }
        }
    }

    private fun LibraryArtist.toPresentation(): LibraryArtistUi {
        return LibraryArtistUi(id, title, coverArtUrl)
    }
}