package code.name.monkey.retromusic.fragments.lyrics

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentLyricsSheetBinding
import code.name.monkey.retromusic.extensions.accentColor
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.lyrics.LrcLibClient
import code.name.monkey.retromusic.lyrics.LrcView
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.LyricUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * Lyrics panel that slides up from the bottom of the player. Tap a line to jump to it.
 * When the song has no lyrics yet, they are fetched from LRCLIB and kept for offline use.
 */
class LyricsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLyricsSheetBinding? = null
    private val binding get() = _binding!!
    private var shownSongId: Long = -1
    private var loadJob: kotlinx.coroutines.Job? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        // Let the player stay visible behind the panel.
        dialog.window?.setDimAmount(0.25f)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLyricsSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val height = (resources.displayMetrics.heightPixels * 0.6f).toInt()
        binding.lyricsSheetRoot.minimumHeight = height
        binding.lyricsSheetLrc.apply {
            setCurrentColor(accentColor())
            setTimeTextColor(accentColor())
            setTimelineColor(accentColor())
            setTimelineTextColor(accentColor())
            // Tapping a line seeks to it.
            setDraggable(true, LrcView.OnPlayClickListener {
                MusicPlayerRemote.seekTo(it.toInt())
                true
            })
        }
        binding.lyricsSheetGet.setOnClickListener { load(MusicPlayerRemote.currentSong, true) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    val song = MusicPlayerRemote.currentSong
                    if (song.id != shownSongId) load(song, false)
                    _binding?.lyricsSheetLrc?.updateTime(MusicPlayerRemote.songProgressMillis.toLong())
                    delay(300)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        (dialog as? BottomSheetDialog)?.behavior?.apply {
            state = BottomSheetBehavior.STATE_EXPANDED
            skipCollapsed = true
        }
    }

    private fun showStatus(text: String, showButton: Boolean) {
        binding.lyricsSheetLrc.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = false
        binding.lyricsSheetStatusHolder.isVisible = true
        binding.lyricsSheetStatus.text = text
        binding.lyricsSheetGet.isVisible = showButton
    }

    private fun showSynced(text: String) {
        binding.lyricsSheetStatusHolder.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = false
        binding.lyricsSheetLrc.isVisible = true
        binding.lyricsSheetLrc.loadLrc(text)
    }

    private fun showPlain(text: String) {
        binding.lyricsSheetStatusHolder.isVisible = false
        binding.lyricsSheetLrc.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = true
        binding.lyricsSheetPlain.text = text
    }

    /** Shows local lyrics if there are any, otherwise (or when [forceOnline]) asks LRCLIB. */
    private fun load(song: Song, forceOnline: Boolean) {
        shownSongId = song.id
        binding.lyricsSheetTitle.text = song.title
        loadJob?.cancel()
        loadJob = viewLifecycleOwner.lifecycleScope.launch {
            val local = withContext(Dispatchers.IO) {
                val file = LyricUtil.getSyncedLyricsFile(song)
                val synced = file?.let { LyricUtil.getStringFromLrc(it) }?.takeIf { it.isNotBlank() }
                    ?: LyricUtil.getEmbeddedSyncedLyrics(song.data)
                synced to LyricUtil.getStoredPlainLyrics(song)
            }
            if (!forceOnline) {
                local.first?.let { return@launch showSynced(it) }
            }
            showStatus(getString(R.string.lyrics_searching), false)
            val outcome = withContext(Dispatchers.IO) {
                try {
                    Result.success(LrcLibClient.fetch(song))
                } catch (e: IOException) {
                    Result.failure(e)
                }
            }
            if (_binding == null) return@launch
            outcome.onSuccess { found ->
                when {
                    found == null -> fallbackToLocal(local, R.string.lyrics_not_found_online)
                    found.synced != null || found.plain != null -> {
                        withContext(Dispatchers.IO) {
                            LyricUtil.storeFetchedLyrics(song, found.synced, found.plain)
                        }
                        if (found.synced != null) showSynced(found.synced) else showPlain(found.plain!!)
                    }
                    else -> showStatus(getString(R.string.lyrics_instrumental), false)
                }
            }.onFailure {
                fallbackToLocal(local, R.string.lyrics_network_error)
            }
        }
    }

    private fun fallbackToLocal(local: Pair<String?, String?>, @androidx.annotation.StringRes message: Int) {
        when {
            local.first != null -> showSynced(local.first!!)
            local.second != null -> showPlain(local.second!!)
            else -> showStatus(getString(message), true)
        }
    }

    override fun onDestroyView() {
        loadJob?.cancel()
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "LYRICS_SHEET"
    }
}
