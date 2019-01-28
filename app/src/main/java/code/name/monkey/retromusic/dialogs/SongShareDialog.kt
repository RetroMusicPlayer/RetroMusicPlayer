package code.name.monkey.retromusic.dialogs

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.views.RoundedBottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_delete.*


class SongShareDialog : RoundedBottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_delete, container, false)
    }

    @SuppressLint("StringFormatInvalid")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val song = arguments!!.getParcelable<Song>("song")!!
        dialogTitle.setTextColor(ThemeStore.textColorPrimary(context!!))

        actionDelete.apply {
            text = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
            setTextColor(ThemeStore.textColorSecondary(context!!))
            setOnClickListener {
                val currentlyListening = getString(R.string.currently_listening_to_x_by_x, song.title, song.artistName)
                activity!!.startActivity(Intent.createChooser(Intent().setAction(Intent.ACTION_SEND)
                        .putExtra(Intent.EXTRA_TEXT, currentlyListening)
                        .setType("text/plain"), null))
                dismiss()
            }
            icon = ContextCompat.getDrawable(context, R.drawable.ic_text_fields_black_24dp)
            MaterialUtil.setTint(this)
        }

        actionCancel.apply {
            setTextColor(ThemeStore.textColorSecondary(context!!))
            setOnClickListener {
                MusicUtil.createShareSongFileIntent(song, context)
                dismiss()
            }
            icon = ContextCompat.getDrawable(context, R.drawable.ic_share_white_24dp)
            MaterialUtil.setTint(this, false)
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
