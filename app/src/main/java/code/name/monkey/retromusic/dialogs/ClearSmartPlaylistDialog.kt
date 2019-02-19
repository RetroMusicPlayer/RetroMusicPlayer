package code.name.monkey.retromusic.dialogs

import android.app.Dialog
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.smartplaylist.AbsSmartPlaylist
import com.afollestad.materialdialogs.MaterialDialog


class ClearSmartPlaylistDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val playlist = arguments!!.getParcelable<AbsSmartPlaylist>("playlist")
        val title = R.string.clear_playlist_title

        val content = Html.fromHtml(getString(R.string.clear_playlist_x, playlist!!.name))

        return MaterialDialog(activity!!).show {
            title(title)
            message(text = content)
            positiveButton(R.string.clear_action) {
                if (activity == null) {
                    return@positiveButton
                }
                playlist.clear(activity)
            }
            negativeButton { (android.R.string.cancel) }
        }
    }

    companion object {

        fun create(playlist: AbsSmartPlaylist): ClearSmartPlaylistDialog {
            val dialog = ClearSmartPlaylistDialog()
            val args = Bundle()
            args.putParcelable("playlist", playlist)
            dialog.arguments = args
            return dialog
        }
    }
}