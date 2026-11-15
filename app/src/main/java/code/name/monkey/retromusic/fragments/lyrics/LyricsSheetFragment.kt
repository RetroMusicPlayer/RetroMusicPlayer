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
import code.name.monkey.retromusic.extensions.showToast
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

    // Hand syncing: the user taps "Mark" when each line of plain lyrics starts.
    private var currentSong: Song? = null
    private var currentPlain: String? = null
    private var syncLines: List<String> = emptyList()
    private val syncStamps = ArrayList<Long>()

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
        binding.lyricsSheetSyncStart.setOnClickListener { startSync() }
        binding.lyricsSyncMark.setOnClickListener { markLine() }
        binding.lyricsSyncUndo.setOnClickListener { undoMark() }
        binding.lyricsSyncCancel.setOnClickListener {
            currentPlain?.let { showPlain(it) }
        }

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

    private fun hideSync() {
        syncLines = emptyList()
        syncStamps.clear()
        binding.lyricsSheetSyncHolder.isVisible = false
        binding.lyricsSheetSyncStart.isVisible = false
    }

    private fun showStatus(text: String, showButton: Boolean) {
        hideSync()
        currentPlain = null
        binding.lyricsSheetLrc.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = false
        binding.lyricsSheetStatusHolder.isVisible = true
        binding.lyricsSheetStatus.text = text
        binding.lyricsSheetGet.isVisible = showButton
    }

    private fun showSynced(text: String) {
        hideSync()
        binding.lyricsSheetStatusHolder.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = false
        binding.lyricsSheetLrc.isVisible = true
        binding.lyricsSheetLrc.loadLrc(text)
    }

    private fun showPlain(text: String) {
        hideSync()
        currentPlain = text
        binding.lyricsSheetStatusHolder.isVisible = false
        binding.lyricsSheetLrc.isVisible = false
        binding.lyricsSheetPlainScroll.isVisible = true
        binding.lyricsSheetPlain.text = text
        binding.lyricsSheetSyncStart.isVisible = true
    }

    private fun startSync() {
        val lines = currentPlain.orEmpty().lines().map { it.trim() }.filter { it.isNotEmpty() }
        if (lines.isEmpty()) return
        syncLines = lines
        syncStamps.clear()
        binding.lyricsSheetPlainScroll.isVisible = false
        binding.lyricsSheetSyncStart.isVisible = false
        binding.lyricsSheetSyncHolder.isVisible = true
        // Start from the beginning so the first mark is the first line.
        MusicPlayerRemote.seekTo(0)
        MusicPlayerRemote.resumePlaying()
        renderSync()
    }

    private fun renderSync() {
        val index = syncStamps.size
        binding.lyricsSyncPrev.text = syncLines.getOrNull(index - 1).orEmpty()
        binding.lyricsSyncCurrent.text = syncLines.getOrNull(index).orEmpty()
        binding.lyricsSyncNext.text = syncLines.getOrNull(index + 1).orEmpty()
        binding.lyricsSyncCounter.text = "${index + 1} / ${syncLines.size}"
    }

    private fun markLine() {
        if (syncLines.isEmpty()) return
        // People press a moment after they hear the line start, so go back a little.
        val position = (MusicPlayerRemote.songProgressMillis - 300L)
            .coerceAtLeast(syncStamps.lastOrNull() ?: 0L)
        syncStamps.add(position)
        if (syncStamps.size == syncLines.size) finishSync() else renderSync()
    }

    private fun undoMark() {
        if (syncStamps.isEmpty()) return
        val removed = syncStamps.removeAt(syncStamps.lastIndex)
        // Jump back to where that line started so it can be marked again.
        MusicPlayerRemote.seekTo(removed.toInt())
        renderSync()
    }

    private fun finishSync() {
        val song = currentSong ?: return
        val lrc = syncLines.indices.joinToString("\n") { i ->
            val ms = syncStamps[i]
            "[%02d:%02d.%02d]%s".format(ms / 60000, (ms % 60000) / 1000, (ms % 1000) / 10, syncLines[i])
        }
        viewLifecycleOwner.lifecycleScope.launch {
            withContext(Dispatchers.IO) { LyricUtil.storeFetchedLyrics(song, lrc, null) }
            context?.showToast(R.string.lyrics_sync_saved)
            if (_binding != null) showSynced(lrc)
        }
    }

    /** Shows local lyrics if there are any, otherwise (or when [forceOnline]) asks LRCLIB. */
    private fun load(song: Song, forceOnline: Boolean) {
        shownSongId = song.id
        currentSong = song
        hideSync()
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
