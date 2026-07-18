package code.name.monkey.retromusic.fragments.player

import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.fragments.base.AbsPlayerControlsFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.lyrics.AbsSynchronizedLyrics
import code.name.monkey.retromusic.model.lyrics.Lyrics
import code.name.monkey.retromusic.util.LyricUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InlineLyricsController(
    private val fragment: AbsPlayerControlsFragment,
    private val rootView: View,
) {
    private var container: LinearLayout? = null
    private var currentLineView: MaterialTextView? = null
    private var nextLineView: MaterialTextView? = null
    private var optionsButton: ImageButton? = null
    private var lyrics: Lyrics? = null
    private var songId: Long = -1L
    private var currentLine: String? = null
    private var nextLine: String? = null
    private var isSupportedLayout = true
    private var isDestroyed = false

    fun start() {
        isDestroyed = false
        isSupportedLayout = ensureViews()
        if (!isSupportedLayout) return
        loadLyrics()
    }

    fun destroy() {
        isDestroyed = true
        lyrics = null
        songId = -1L
        currentLine = null
        nextLine = null
        (container?.parent as? ViewGroup)?.removeView(container)
        (optionsButton?.parent as? ViewGroup)?.removeView(optionsButton)
        container = null
        currentLineView = null
        nextLineView = null
        optionsButton = null
    }

    fun onSongChanged() {
        if (!isSupportedLayout) return
        loadLyrics()
    }

    fun onProgress(progress: Int) {
        if (!isSupportedLayout || isDestroyed) return
        if (songId != MusicPlayerRemote.currentSong.id) {
            loadLyrics()
            return
        }
        val synchronizedLyrics = lyrics as? AbsSynchronizedLyrics
        if (PreferenceUtil.showLyrics) {
            applyStyle()
            showLyricsOptionsButton()
            return
        }
        if (!PreferenceUtil.showInlineLyrics || synchronizedLyrics?.isValid != true) {
            hide()
            return
        }

        val line = synchronizedLyrics.getLine(progress).trim()
        val upcomingLine = synchronizedLyrics.getLineAfter(progress).trim()
        if (line.isEmpty() && upcomingLine.isEmpty()) {
            hide()
            return
        }

        applyStyle()
        if (currentLine != line || nextLine != upcomingLine) {
            currentLine = line
            nextLine = upcomingLine
            animateLineChange(line, upcomingLine)
        }
        currentLineView?.alpha = 1f
        nextLineView?.alpha = if (upcomingLine.isEmpty()) 0f else 0.58f
        optionsButton?.isVisible = false
        container?.apply {
            alpha = 1f
            isVisible = true
        }
    }

    private fun ensureViews(): Boolean {
        if (container != null) return true
        val root = rootView as? ConstraintLayout ?: return false
        val progress = root.findViewById<View>(R.id.progressSlider) ?: return false
        val lyricsContainer = LinearLayout(root.context).apply {
            id = View.generateViewId()
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            isVisible = false
            clipToPadding = false
            minimumHeight = root.context.dp(48f)
            setPadding(root.context.dp(24f), 0, root.context.dp(24f), root.context.dp(8f))
            layoutParams = ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        val current = newLineView(root.context)
        val next = newLineView(root.context).apply {
            alpha = 0.58f
            textSize = 13f
        }
        lyricsContainer.addView(current)
        lyricsContainer.addView(next)
        root.addView(lyricsContainer)
        val button = newOptionsButton(root.context)
        root.addView(button)

        ConstraintSet().apply {
            clone(root)
            connect(lyricsContainer.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(lyricsContainer.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            connect(lyricsContainer.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(lyricsContainer.id, ConstraintSet.BOTTOM, progress.id, ConstraintSet.TOP)
            connect(progress.id, ConstraintSet.TOP, lyricsContainer.id, ConstraintSet.BOTTOM)
            setVerticalBias(lyricsContainer.id, 0.5f)
            connect(button.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, root.context.dp(28f))
            connect(button.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            connect(button.id, ConstraintSet.BOTTOM, progress.id, ConstraintSet.TOP)
            setVerticalBias(button.id, 0.5f)
            applyTo(root)
        }

        container = lyricsContainer
        currentLineView = current
        nextLineView = next
        optionsButton = button
        applyStyle()
        return true
    }

    private fun newLineView(context: android.content.Context): MaterialTextView {
        return MaterialTextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            ellipsize = TextUtils.TruncateAt.MARQUEE
            marqueeRepeatLimit = -1
            isSingleLine = true
            isSelected = true
            setHorizontallyScrolling(true)
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }

    private fun newOptionsButton(context: android.content.Context): ImageButton {
        return ImageButton(context).apply {
            id = View.generateViewId()
            layoutParams = ConstraintLayout.LayoutParams(context.dp(34f), context.dp(34f))
            background = null
            setImageResource(R.drawable.ic_lyrics_size)
            contentDescription = context.getString(R.string.pref_header_full_lyrics)
            scaleType = ImageView.ScaleType.FIT_CENTER
            adjustViewBounds = true
            setPadding(context.dp(2f), context.dp(2f), context.dp(2f), context.dp(2f))
            isVisible = false
            setOnClickListener { showLyricsTextOptionsDialog() }
        }
    }

    private fun loadLyrics() {
        val song = MusicPlayerRemote.currentSong
        songId = song.id
        lyrics = null
        currentLine = null
        nextLine = null
        if (PreferenceUtil.showLyrics) {
            applyStyle()
            showLyricsOptionsButton()
        } else {
            hide()
        }

        (fragment as Fragment).lifecycleScope.launch(Dispatchers.IO) {
            val syncedLyrics = runCatching {
                val lrcFile = LyricUtil.getSyncedLyricsFile(song)
                val data = LyricUtil.getStringFromLrc(lrcFile).ifEmpty {
                    LyricUtil.getEmbeddedSyncedLyrics(song.data).orEmpty()
                }
                Lyrics.parse(song, data)
            }.getOrNull()

            withContext(Dispatchers.Main) {
                if (isDestroyed) return@withContext
                if (songId != song.id) return@withContext
                lyrics = syncedLyrics
                onProgress(MusicPlayerRemote.songProgressMillis)
            }
        }
    }

    private fun applyStyle() {
        val gravity = when (PreferenceUtil.inlineLyricsGravity) {
            1 -> Gravity.START
            2 -> Gravity.END
            else -> Gravity.CENTER
        }
        val textAlignment = when (PreferenceUtil.inlineLyricsGravity) {
            1 -> View.TEXT_ALIGNMENT_TEXT_START
            2 -> View.TEXT_ALIGNMENT_TEXT_END
            else -> View.TEXT_ALIGNMENT_CENTER
        }
        val color = if (PreferenceUtil.inlineLyricsUseCustomColor) {
            PreferenceUtil.inlineLyricsColor
        } else {
            fragment.lastPlaybackControlsColor.takeIf { it != 0 }
                ?: ThemeStore.accentColor(rootView.context)
        }
        val typeface = if (PreferenceUtil.inlineLyricsBold) Typeface.BOLD else Typeface.NORMAL

        container?.gravity = gravity
        currentLineView?.apply {
            setTextColor(color)
            setTypeface(Typeface.DEFAULT, typeface)
            this.textAlignment = textAlignment
        }
        nextLineView?.apply {
            setTextColor(color)
            setTypeface(Typeface.DEFAULT, typeface)
            this.textAlignment = textAlignment
        }
        optionsButton?.setColorFilter(color)
    }

    private fun animateLineChange(line: String, upcomingLine: String) {
        currentLineView?.apply {
            animate().cancel()
            alpha = 0f
            translationY = height.coerceAtLeast(1).toFloat()
            text = line
            animate().alpha(1f).translationY(0f).setDuration(220L).start()
        }
        nextLineView?.apply {
            animate().cancel()
            alpha = 0f
            translationY = height.coerceAtLeast(1).toFloat()
            text = upcomingLine
            isVisible = upcomingLine.isNotEmpty()
            animate().alpha(0.58f).translationY(0f).setDuration(260L).start()
        }
    }

    private fun showLyricsOptionsButton() {
        container?.apply {
            alpha = 1f
            isVisible = true
            minimumHeight = rootView.context.dp(48f)
        }
        currentLineView?.alpha = 0f
        nextLineView?.alpha = 0f
        optionsButton?.apply {
            isVisible = true
            bringToFront()
        }
    }

    private fun hide() {
        container?.apply {
            alpha = 1f
            isVisible = false
        }
        currentLineView?.alpha = 1f
        nextLineView?.alpha = 0.58f
        optionsButton?.isVisible = false
        currentLineView?.text = null
        nextLineView?.text = null
    }

    private fun showLyricsTextOptionsDialog() {
        val context = rootView.context
        val content = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(context.dp(24f), context.dp(12f), context.dp(24f), 0)
        }
        val sizeLabel = TextView(context).apply {
            text = context.getString(R.string.lyrics_text_size_label, PreferenceUtil.fullLyricsTextSize)
        }
        val sizeSlider = Slider(context).apply {
            valueFrom = MIN_FULL_LYRICS_TEXT_SIZE.toFloat()
            valueTo = MAX_FULL_LYRICS_TEXT_SIZE.toFloat()
            stepSize = 1f
            value = PreferenceUtil.fullLyricsTextSize
                .coerceIn(MIN_FULL_LYRICS_TEXT_SIZE, MAX_FULL_LYRICS_TEXT_SIZE)
                .toFloat()
        }
        val boldCheckBox = CheckBox(context).apply {
            text = context.getString(R.string.pref_title_full_lyrics_bold)
            isChecked = PreferenceUtil.fullLyricsBold
        }

        sizeSlider.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                PreferenceUtil.fullLyricsTextSize = value.toInt()
                sizeLabel.text = context.getString(R.string.lyrics_text_size_label, value.toInt())
            }
        }
        boldCheckBox.setOnCheckedChangeListener { _, isChecked ->
            PreferenceUtil.fullLyricsBold = isChecked
        }

        content.addView(sizeLabel)
        content.addView(sizeSlider)
        content.addView(boldCheckBox)

        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.pref_header_full_lyrics)
            .setView(content)
            .setNegativeButton(R.string.reset_action) { _, _ ->
                PreferenceUtil.fullLyricsTextSize = 28
                PreferenceUtil.fullLyricsBold = false
            }
            .setPositiveButton(R.string.done, null)
            .show()
    }

    private fun android.content.Context.dp(value: Float): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    companion object {
        private const val MIN_FULL_LYRICS_TEXT_SIZE = 18
        private const val MAX_FULL_LYRICS_TEXT_SIZE = 42
    }
}
