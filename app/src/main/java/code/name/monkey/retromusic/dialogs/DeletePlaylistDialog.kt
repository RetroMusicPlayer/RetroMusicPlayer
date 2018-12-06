package code.name.monkey.retromusic.dialogs

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.util.PlaylistsUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_remove_from_playlist.*
import java.util.*


class DeletePlaylistDialog : RoundedBottomSheetDialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_remove_from_playlist, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlists = arguments!!.getParcelableArrayList<Playlist>("playlists")
        val content: CharSequence

        content = if (playlists!!.size > 1) {
            Html.fromHtml(getString(R.string.delete_x_playlists, playlists.size))
        } else {
            Html.fromHtml(getString(R.string.delete_playlist_x, playlists[0].name))
        }
        bannerTitle.text = content
        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))

        actionRemove.setText(R.string.action_delete)
        actionRemove.setTextColor(ThemeStore.textColorSecondary(context!!))
        actionCancel.setTextColor(ThemeStore.textColorSecondary(context!!))

        actionCancel.setOnClickListener { dismiss() }
        actionRemove.setOnClickListener {
            PlaylistsUtil.deletePlaylists(activity!!, playlists)
            dismiss()
        }
    }


    companion object {

        fun create(playlist: Playlist): DeletePlaylistDialog {
            val list = ArrayList<Playlist>()
            list.add(playlist)
            return create(list)
        }

        fun create(playlist: ArrayList<Playlist>): DeletePlaylistDialog {
            val dialog = DeletePlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("playlist", playlist)
            dialog.arguments = args
            return dialog
        }
    }

}