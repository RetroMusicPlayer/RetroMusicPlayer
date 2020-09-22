package io.github.muntashirakon.music.dialogs

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import io.github.muntashirakon.music.App
import io.github.muntashirakon.music.EXTRA_PLAYLIST
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.db.PlaylistWithSongs
import io.github.muntashirakon.music.extensions.colorButtons
import io.github.muntashirakon.music.extensions.extraNotNull
import io.github.muntashirakon.music.extensions.materialDialog
import io.github.muntashirakon.music.util.PlaylistsUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SavePlaylistDialog : DialogFragment() {
    companion object {
        fun create(playlistWithSongs: PlaylistWithSongs): SavePlaylistDialog {
            return SavePlaylistDialog().apply {
                arguments = bundleOf(
                    EXTRA_PLAYLIST to playlistWithSongs
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            val playlistWithSongs: PlaylistWithSongs =
                extraNotNull<PlaylistWithSongs>(EXTRA_PLAYLIST).value
            val file = PlaylistsUtil.savePlaylistWithSongs(requireContext(), playlistWithSongs)
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    String.format(App.getContext().getString(R.string.saved_playlist_to), file),
                    Toast.LENGTH_LONG
                ).show()
                dismiss()
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return materialDialog(R.string.save_playlist_title)
            .setView(R.layout.loading)
            .create().colorButtons()
    }
}