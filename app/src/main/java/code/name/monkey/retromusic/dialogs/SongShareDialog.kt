package code.name.monkey.retromusic.dialogs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_file_share.*


class SongShareDialog : RoundedBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_file_share, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val song = arguments!!.getParcelable<Song>("song")!!

        audioText.text = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
        audioFile.setTextColor(ThemeStore.textColorSecondary(context!!))
        audioText.setTextColor(ThemeStore.textColorSecondary(context!!))
        title.setTextColor(ThemeStore.textColorPrimary(context!!))

        audioFile.setOnClickListener {
            MusicUtil.createShareSongFileIntent(song, context)
            dismiss()
        }
        audioText.setOnClickListener {
            val currentlyListening = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
            activity!!.startActivity(Intent.createChooser(Intent().setAction(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                    .setType("text/plain"), null))
            dismiss()
        }

    }

    companion object {

        fun create(song: Song): SongShareDialog {
            val dialog = SongShareDialog()
            val args = Bundle()
            args.putParcelable("song", song)
            dialog.arguments = args
            return dialog
        }
    }
}
