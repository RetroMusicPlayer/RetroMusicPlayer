package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.EXTRA_SONG
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class RemoveSongFromPlaylistDialog : DialogFragment() {
    private val repository by inject<Repository>()
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(song: SongEntity): RemoveSongFromPlaylistDialog {
            val list = mutableListOf<SongEntity>()
            list.add(song)
            return create(list)
        }

        fun create(songs: List<SongEntity>): RemoveSongFromPlaylistDialog {
            return RemoveSongFromPlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_SONG to songs
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val songs = extraNotNull<List<SongEntity>>(EXTRA_SONG).value
        val pair = if (songs.size > 1) {
            Pair(
                R.string.remove_songs_from_playlist_title,
                HtmlCompat.fromHtml(
                    String.format(getString(R.string.remove_x_songs_from_playlist), songs.size),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        } else {
            Pair(
                R.string.remove_song_from_playlist_title,
                HtmlCompat.fromHtml(
                    String.format(
                        getString(R.string.remove_song_x_from_playlist),
                        songs[0].title
                    ),
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
        }
        return materialDialog(pair.first)
            .setMessage(pair.second)
            .setPositiveButton(R.string.remove_action) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.removeSongFromPlaylist(songs)
                }
                /* PlaylistsUtil.removeFromPlaylist(
                    requireContext(),
                    songs as MutableList<PlaylistSong>
                )*/
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .colorButtons()
    }
}