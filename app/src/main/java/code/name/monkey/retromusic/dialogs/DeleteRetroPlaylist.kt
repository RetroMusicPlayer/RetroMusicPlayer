package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.EXTRA_PLAYLIST
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.ReloadType
import code.name.monkey.retromusic.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DeleteRetroPlaylist : DialogFragment() {
    private val repository by inject<Repository>()
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {

        fun create(playlist: PlaylistEntity): DeleteRetroPlaylist {
            val list = mutableListOf<PlaylistEntity>()
            list.add(playlist)
            return create(list)
        }

        fun create(playlists: List<PlaylistEntity>): DeleteRetroPlaylist {
            return DeleteRetroPlaylist().apply {
                arguments = bundleOf(EXTRA_PLAYLIST to playlists)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlists = extraNotNull<List<PlaylistEntity>>(EXTRA_PLAYLIST).value
        val title: Int
        val message: CharSequence
        //noinspection ConstantConditions
        if (playlists.size > 1) {
            title = R.string.delete_playlists_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_x_playlists), playlists.size),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        } else {
            title = R.string.delete_playlist_title
            message = HtmlCompat.fromHtml(
                String.format(getString(R.string.delete_playlist_x), playlists[0].playlistName),
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }

        return materialDialog(title)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ ->
                lifecycleScope.launch(Dispatchers.IO) {
                    repository.deleteSongsFromPlaylist(playlists)
                    repository.deleteRoomPlaylist(playlists)
                    libraryViewModel.forceReload(ReloadType.Playlists)
                }
            }
            .create()
            .colorButtons()
    }

}