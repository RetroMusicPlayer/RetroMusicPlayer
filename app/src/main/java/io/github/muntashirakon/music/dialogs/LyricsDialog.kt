package io.github.muntashirakon.music.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.extensions.accentTextColor
import io.github.muntashirakon.music.extensions.hide
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.network.Result
import io.github.muntashirakon.music.repository.Repository
import kotlinx.android.synthetic.main.lyrics_dialog.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class LyricsDialog : DialogFragment() {
    override fun getTheme(): Int {
        return R.style.MaterialAlertDialogTheme
    }

    private val repository by inject<Repository>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.lyrics_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val song = MusicPlayerRemote.currentSong
        dialogTitle.text = song.title
        syncedLyrics.accentTextColor()
        lifecycleScope.launch(IO) {
            val result: Result<String> = repository.lyrics(
                song.artistName,
                song.title
            )
            withContext(Main) {

                when (result) {
                    is Result.Error -> progressBar.hide()
                    is Result.Loading -> println("Loading")
                    is Result.Success -> {
                        progressBar.hide()
                        lyricsText.text = result.data
                    }
                }
            }
        }
    }
}