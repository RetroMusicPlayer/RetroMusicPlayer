package code.name.monkey.retromusic.fragments.artists

import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumArtistDetailsFragment : AbsArtistDetailsFragment() {

    private val arguments by navArgs<AlbumArtistDetailsFragmentArgs>()

    override val detailsViewModel: ArtistDetailsViewModel by viewModel {
        parametersOf(null, arguments.extraArtistName)
    }
    override val artistId: Long?
        get() = null
    override val artistName: String
        get() = arguments.extraArtistName
}
