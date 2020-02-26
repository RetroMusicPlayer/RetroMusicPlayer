package code.name.monkey.retromusic.fragments.base

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import androidx.fragment.app.Fragment
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.interfaces.MusicServiceEventListener
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.RetroUtil
import org.jaudiotagger.audio.AudioFileIO
import java.io.File
import java.net.URLEncoder
import java.util.*

/**
 * Created by hemanths on 18/08/17.
 */

open class AbsMusicServiceFragment : Fragment(), MusicServiceEventListener {

    var playerActivity: AbsMusicServiceActivity? = null
        private set

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            playerActivity = context as AbsMusicServiceActivity?
        } catch (e: ClassCastException) {
            throw RuntimeException(context.javaClass.simpleName + " must be an instance of " + AbsMusicServiceActivity::class.java.simpleName)
        }
    }

    override fun onDetach() {
        super.onDetach()
        playerActivity = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerActivity?.addMusicServiceEventListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        playerActivity?.removeMusicServiceEventListener(this)
    }

    override fun onPlayingMetaChanged() {
    }

    override fun onServiceConnected() {
    }

    override fun onServiceDisconnected() {
    }

    override fun onQueueChanged() {
    }

    override fun onPlayStateChanged() {
    }

    override fun onRepeatModeChanged() {
    }

    override fun onShuffleModeChanged() {
    }

    override fun onMediaStoreChanged() {
    }

    fun getSongInfo(song: Song): String {
        val file = File(song.data)
        if (file.exists()) {
            return try {
                val audioHeader = AudioFileIO.read(File(song.data)).audioHeader
                val string: StringBuilder = StringBuilder()
                val uriFile = Uri.fromFile(file)
                string.append(getMimeType(uriFile.toString())).append(" • ")
                string.append(audioHeader.bitRate).append(" kb/s").append(" • ")
                string.append(RetroUtil.frequencyCount(audioHeader.sampleRate.toInt()))
                    .append(" kHz")
                string.toString()
            } catch (er: Exception) {
                " - "
            }
        }
        return "-"
    }

    private fun getMimeType(url: String): String? {
        var type: String? = MimeTypeMap.getFileExtensionFromUrl(
            URLEncoder.encode(url, "utf-8")
        ).toUpperCase(Locale.getDefault())
        if (type == null) {
            type = url.substring(url.lastIndexOf(".") + 1)
        }
        return type
    }
}
