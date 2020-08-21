package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.EXTRA_PLAYLISTS
import code.name.monkey.retromusic.EXTRA_SONG
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.fragments.ReloadType.Playlists
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.RealRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AddToRetroPlaylist : DialogFragment() {
    private val repository by inject<RealRepository>()
    private val libraryViewModel by sharedViewModel<LibraryViewModel>()

    companion object {
        fun create(playlistEntities: List<PlaylistEntity>, song: Song): AddToRetroPlaylist {
            val list = mutableListOf<Song>()
            list.add(song)
            return create(playlistEntities, list)
        }

        fun create(playlistEntities: List<PlaylistEntity>, songs: List<Song>): AddToRetroPlaylist {
            return AddToRetroPlaylist().apply {
                arguments = bundleOf(
                    EXTRA_SONG to songs,
                    EXTRA_PLAYLISTS to playlistEntities
                )
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlistEntities = extraNotNull<List<PlaylistEntity>>(EXTRA_PLAYLISTS).value
        val songs = extraNotNull<List<Song>>(EXTRA_SONG).value
        val playlistNames = mutableListOf<String>()
        playlistNames.add(requireContext().resources.getString(R.string.action_new_playlist))
        for (p in playlistEntities) {
            playlistNames.add(p.playlistName)
        }
        return materialDialog(R.string.add_playlist_title)
            .setItems(playlistNames.toTypedArray()) { _, which ->
                if (which == 0) {
                    CreateRetroPlaylist().show(requireActivity().supportFragmentManager, "Dialog")
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        val songEntities = songs.toSongEntity(playlistEntities[which - 1])
                        repository.insertSongs(songEntities)
                        libraryViewModel.forceReload(Playlists)
                    }
                }
                dismiss()
            }
            .create().colorButtons()
    }
}

private fun List<Song>.toSongEntity(playlistEntity: PlaylistEntity): List<SongEntity> {
    return map {
        it.toSongEntity(playlistEntity.playListId)
    }
}
