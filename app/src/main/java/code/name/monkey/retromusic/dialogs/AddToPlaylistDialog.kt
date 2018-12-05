package code.name.monkey.retromusic.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.loaders.PlaylistLoader
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.playlist.AddToPlaylist
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_add_to_playlist.*
import java.util.*

/**
 * @author Karim Abou Zeid (kabouzeid), Aidan Follestad (afollestad)
 */
class AddToPlaylistDialog : RoundedBottomSheetDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.dialog_add_to_playlist, container, false)
        ButterKnife.bind(this, layout)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionAddPlaylist.setOnClickListener {
            val songs = arguments!!.getParcelableArrayList<Song>("songs")
            CreatePlaylistDialog.create(songs).show(activity!!.supportFragmentManager, "ADD_TO_PLAYLIST")
            dismiss()
        }

        bannerTitle.setTextColor(ThemeStore.textColorPrimary(context!!))
        val songs = arguments!!.getParcelableArrayList<Song>("songs")
        val playlists = PlaylistLoader.getAllPlaylists(activity!!).blockingFirst()
        val playlistAdapter = AddToPlaylist(activity!!, playlists, R.layout.item_playlist, songs!!, dialog)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            adapter = playlistAdapter
        }
    }

    companion object {

        fun create(song: Song): AddToPlaylistDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<Song>): AddToPlaylistDialog {
            val dialog = AddToPlaylistDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}