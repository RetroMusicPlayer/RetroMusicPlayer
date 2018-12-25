package code.name.monkey.retromusic.dialogs

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_delete.*
import java.util.*

class DeleteSongsDialog : RoundedBottomSheetDialogFragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        title.setTextColor(ThemeStore.textColorPrimary(context!!))
        MaterialUtil.setTint(actionDelete)
        MaterialUtil.setTint(actionCancel)

        //noinspection unchecked,ConstantConditions
        val songs = arguments!!.getParcelableArrayList<Song>("songs")
        val content: CharSequence
        if (songs != null) {
            content = if (songs.size > 1) {
                Html.fromHtml(getString(R.string.delete_x_songs, songs.size))
            } else {
                Html.fromHtml(getString(R.string.delete_song_x, songs[0].title))
            }
            this.title.text = content
        }
        actionDelete.setOnClickListener {
            if (songs != null) {
                MusicUtil.deleteTracks(activity!!, songs)
            }
            dismiss()
        }
        actionCancel.setOnClickListener { dismiss() }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_delete, container, false)
    }

    companion object {

        fun create(song: Song): DeleteSongsDialog {
            val list = ArrayList<Song>()
            list.add(song)
            return create(list)
        }

        fun create(songs: ArrayList<Song>): DeleteSongsDialog {
            val dialog = DeleteSongsDialog()
            val args = Bundle()
            args.putParcelableArrayList("songs", songs)
            dialog.arguments = args
            return dialog
        }
    }
}

