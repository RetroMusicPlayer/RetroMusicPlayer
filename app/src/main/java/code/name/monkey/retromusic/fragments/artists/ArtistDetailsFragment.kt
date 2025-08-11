package code.name.monkey.retromusic.fragments.artists

import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ArtistDetailsFragment : AbsArtistDetailsFragment() {
    private val arguments by navArgs<ArtistDetailsFragmentArgs>()
    override val detailsViewModel: ArtistDetailsViewModel by viewModel {
        parametersOf(arguments.extraArtistId, null)
    }
    override val artistId: Long
        get() = arguments.extraArtistId
    override val artistName: String?
        get() = null

}
