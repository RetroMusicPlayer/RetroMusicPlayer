package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistDatabase
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.SongEntity
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.extensions.materialDialog
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.repository.RealSongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class AddToRetroPlaylist : DialogFragment() {
    companion object {
        fun getInstance(playlistName: List<PlaylistEntity>): AddToRetroPlaylist {
            return AddToRetroPlaylist().apply {
                arguments = bundleOf("playlist_names" to playlistName)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val playlistEntities = extraNotNull<List<PlaylistEntity>>("playlist_names").value
        val playlistNames = mutableListOf<String>()
        playlistNames.add(requireContext().resources.getString(R.string.action_new_playlist))
        for (p in playlistEntities) {
            playlistNames.add(p.playlistName)
        }
        return materialDialog(R.string.add_playlist_title)
            .setItems(playlistNames.toTypedArray()) { _, which ->
                val songs = RealSongRepository(requireContext()).songs()
                println(songs.size)
                if (which == 0) {
                    CreateRetroPlaylist().show(requireActivity().supportFragmentManager, "Dialog")
                } else {
                    lifecycleScope.launch(Dispatchers.IO) {
                        println(Thread.currentThread().name)
                        val database: PlaylistDatabase = get()
                        val songEntities = songs.withPlaylistIds(playlistEntities[which - 1])
                        database.playlistDao().insertSongs(songEntities)
                    }
                }
                dismiss()
            }
            .create().colorButtons()
    }
}

private fun List<Song>.withPlaylistIds(playlistEntity: PlaylistEntity): List<SongEntity> {
    val songEntities = map {
        SongEntity(it.id, playlistEntity.playlistName)
    }
    println(songEntities.size)
    return songEntities
}
