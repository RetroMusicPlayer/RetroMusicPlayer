package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistDatabase
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.extensions.colorButtons
import code.name.monkey.retromusic.extensions.materialDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_playlist.view.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class CreateRetroPlaylist : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireActivity()).inflate(R.layout.dialog_playlist, null)
        val playlistView: TextInputEditText = view.actionNewPlaylist
        val playlistContainer: TextInputLayout = view.actionNewPlaylistContainer
        return materialDialog(R.string.new_playlist_title)
            .setView(view)
            .setPositiveButton(
                R.string.create_action
            ) { _, _ ->
                val playlistName = playlistView.text.toString()
                if (!TextUtils.isEmpty(playlistName)) {
                    val database: PlaylistDatabase = get()
                    lifecycleScope.launch {
                        database.playlistDao().createPlaylist(PlaylistEntity(playlistName))
                    }
                } else {
                    playlistContainer.error = "Playlist is can't be empty"
                }
            }
            .create()
            .colorButtons()
    }
}