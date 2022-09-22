package code.name.monkey.retromusic.feature.library.album.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import code.name.monkey.retromusic.feature.library.album.domain.LibraryAlbum
import code.name.monkey.retromusic.feature.library.album.domain.LibraryAlbumRepository
import ru.stersh.retrosonic.core.extensions.mapItems
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class LibraryAlbumViewModel(private val repository: LibraryAlbumRepository) : ViewModel() {
    private val _albums = MutableStateFlow<List<LibraryAlbumUi>>(emptyList())
    val albums: Flow<List<LibraryAlbumUi>>
        get() = _albums

    init {
        viewModelScope.launch {
            repository
                .getAlbums()
                .mapItems { it.toPresentation() }
                .collect {
                    _albums.value = it
                }
        }
    }

    private fun LibraryAlbum.toPresentation(): LibraryAlbumUi {
        return LibraryAlbumUi(id, title, artist, coverUrl, year)
    }
}