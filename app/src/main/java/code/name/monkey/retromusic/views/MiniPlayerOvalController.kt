/*
 * R Music: floating "oval" mini player.
 *
 * Drives view_mini_player_oval.xml: cover with a progress ring, play/pause and (for the "pill"
 * shape) the song title. The activity owns the view, decides when it is visible and where it sits.
 */
package code.name.monkey.retromusic.views

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ViewMiniPlayerOvalBinding
import code.name.monkey.retromusic.extensions.isColorLight
import code.name.monkey.retromusic.fragments.other.MiniPlayerFragment
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroGlideExtension.asBitmapPalette
import code.name.monkey.retromusic.glide.RetroGlideExtension.songCoverOptions
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.interfaces.IMusicServiceEventListener
import code.name.monkey.retromusic.util.ArtistSeparator
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide

class MiniPlayerOvalController(
    private val binding: ViewMiniPlayerOvalBinding,
    private val onOpen: () -> Unit,
) : IMusicServiceEventListener, MusicProgressViewUpdateHelper.Callback {

    private val context get() = binding.root.context
    private val progressHelper = MusicProgressViewUpdateHelper(this)
    private var coverColors: MediaNotificationProcessor? = null
    private var shape = 0

    // Position: the default spot is below the toolbar at the physical left; the user can move
    // the oval with a long press + drag and the offset from the default spot is remembered.
    private var baseLeft = 0
    private var baseTop = 0
    private val fling = MiniPlayerFragment.FlingPlayBackController(binding.root.context)
    private val touchSlop = ViewConfiguration.get(binding.root.context).scaledTouchSlop
    private var dragging = false
    private var moved = false
    private var downRawX = 0f
    private var downRawY = 0f
    private var startLeft = 0
    private var startTop = 0
    private val startDrag = Runnable {
        dragging = true
        moved = false
        binding.root.isPressed = false
        binding.root.parent?.requestDisallowInterceptTouchEvent(true)
        binding.root.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        binding.root.animate().scaleX(1.1f).scaleY(1.1f).setDuration(120).start()
    }

    init {
        binding.root.setOnClickListener { onOpen() }
        setUpTouch()
        binding.ovalPlayPause.setOnClickListener(PlayPauseButtonOnClickHandler())
        applySettings()
    }

    /**
     * Tap = open the player (view click), swipe left / right = next / previous (same as the old
     * bar), long press + drag = move the oval. In the circle shape (no play/pause button) a long
     * press without moving toggles play/pause.
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun setUpTouch() {
        binding.root.setOnTouchListener { v, event ->
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    val lp = v.layoutParams as ViewGroup.MarginLayoutParams
                    downRawX = event.rawX
                    downRawY = event.rawY
                    startLeft = lp.leftMargin
                    startTop = lp.topMargin
                    dragging = false
                    moved = false
                    v.postDelayed(startDrag, ViewConfiguration.getLongPressTimeout().toLong())
                    fling.onTouch(v, event)
                    false
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = event.rawX - downRawX
                    val dy = event.rawY - downRawY
                    if (dragging) {
                        if (!moved && (kotlin.math.abs(dx) > touchSlop || kotlin.math.abs(dy) > touchSlop)) {
                            moved = true
                        }
                        if (moved) {
                            moveTo(startLeft + dx.toInt(), startTop + dy.toInt())
                        }
                        true
                    } else {
                        if (kotlin.math.abs(dx) > touchSlop || kotlin.math.abs(dy) > touchSlop) {
                            v.removeCallbacks(startDrag)
                        }
                        fling.onTouch(v, event)
                    }
                }

                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.removeCallbacks(startDrag)
                    if (dragging) {
                        dragging = false
                        v.isPressed = false
                        v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
                        if (moved) {
                            savePosition()
                        } else if (shape == SHAPE_CIRCLE && event.actionMasked == MotionEvent.ACTION_UP) {
                            PlayPauseButtonOnClickHandler().onClick(v)
                        }
                        true
                    } else {
                        fling.onTouch(v, event)
                    }
                }

                else -> fling.onTouch(v, event)
            }
        }
    }

    private fun defaultLeft() = baseLeft + dp(12)

    // Below the toolbar so the toolbar menu (the three dots, on the left in RTL) stays reachable.
    private fun defaultTop() = baseTop + dp(8 + 56)

    private fun moveTo(left: Int, top: Int) {
        val parent = binding.root.parent as? View
        var l = left
        var t = top
        if (parent != null && parent.width > 0 && binding.root.width > 0) {
            l = l.coerceIn(0, (parent.width - binding.root.width).coerceAtLeast(0))
            t = t.coerceIn(0, (parent.height - binding.root.height).coerceAtLeast(0))
        }
        binding.root.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            leftMargin = l
            topMargin = t
        }
    }

    private fun savePosition() {
        val lp = binding.root.layoutParams as ViewGroup.MarginLayoutParams
        PreferenceUtil.miniPlayerOffsetX = lp.leftMargin - defaultLeft()
        PreferenceUtil.miniPlayerOffsetY = lp.topMargin - defaultTop()
    }

    private fun dp(value: Int) = (value * context.resources.displayMetrics.density).toInt()

    /** Reads shape, size and background from the preferences and applies them. */
    fun applySettings() {
        shape = PreferenceUtil.miniPlayerShape
        val coverDp = when (PreferenceUtil.miniPlayerSize) {
            0 -> 36
            2 -> 56
            else -> 44
        }
        val ring = dp(3)
        val coverPx = dp(coverDp)
        val holderPx = coverPx + ring * 2
        val pad = dp(4)

        binding.ovalCoverHolder.updateLayoutParams<ViewGroup.LayoutParams> {
            width = holderPx
            height = holderPx
        }
        binding.ovalCoverCard.updateLayoutParams<ViewGroup.LayoutParams> {
            width = coverPx
            height = coverPx
        }
        binding.ovalCoverCard.radius = coverPx / 2f
        binding.ovalProgress.trackThickness = dp(2)
        binding.ovalProgress.indicatorInset = 0
        binding.ovalProgress.indicatorSize = holderPx

        val playPx = (coverPx * 0.85f).toInt().coerceAtLeast(dp(30))
        binding.ovalPlayHolder.updateLayoutParams<ViewGroup.LayoutParams> {
            width = playPx
            height = playPx
        }
        binding.ovalPlayHolder.isGone = shape == SHAPE_CIRCLE
        binding.ovalTitle.isVisible = shape == SHAPE_PILL

        binding.ovalRow.setPadding(pad, pad, pad, pad)
        binding.root.radius = (holderPx + pad * 2) / 2f

        applyColors()
        updateSong()
        updatePlayPause()
    }

    private fun applyColors() {
        val setting = PreferenceUtil.miniPlayerBackground
        val accent = ThemeStore.accentColor(context)
        val transparent = setting == BG_TRANSPARENT
        val background = when (setting) {
            BG_COVER -> coverColors?.backgroundColor ?: accent
            BG_TRANSPARENT -> Color.TRANSPARENT
            BG_DARK -> 0xE61E1E1E.toInt()
            else -> accent
        }
        val onColor = when {
            transparent || setting == BG_DARK -> Color.WHITE
            background.isColorLight -> Color.BLACK
            else -> Color.WHITE
        }

        binding.root.setCardBackgroundColor(background)
        binding.root.cardElevation = if (transparent) 0f else dp(6).toFloat()

        binding.ovalTitle.setTextColor(onColor)
        if (transparent) {
            binding.ovalTitle.setShadowLayer(dp(3).toFloat(), 0f, 1f, Color.BLACK)
        } else {
            binding.ovalTitle.setShadowLayer(0f, 0f, 0f, Color.TRANSPARENT)
        }

        binding.ovalPlayPause.imageTintList = ColorStateList.valueOf(onColor)
        binding.ovalPlayHolder.background = if (transparent) {
            GradientDrawable().apply {
                this.shape = GradientDrawable.OVAL
                setColor(0x99000000.toInt())
            }
        } else {
            null
        }

        // Ring: the cover/solid backgrounds use the readable colour, the see-through and dark
        // ones use the accent colour so the ring stays visible over the wall.
        val ringColor = if (transparent || setting == BG_DARK) accent else onColor
        binding.ovalProgress.setIndicatorColor(ringColor)
        binding.ovalProgress.trackColor = ColorUtils.setAlphaComponent(ringColor, 60)
    }

    private fun updateSong() {
        if (MusicPlayerRemote.playingQueue.isEmpty()) return
        val song = MusicPlayerRemote.currentSong
        val artist = ArtistSeparator.split(song.artistName).joinToString(", ")
        binding.ovalTitle.text = "${song.title} • $artist"
        binding.ovalTitle.isSelected = true
        Glide.with(context)
            .asBitmapPalette()
            .songCoverOptions(song)
            .load(RetroGlideExtension.getSongModel(song))
            .dontAnimate()
            .into(object : RetroMusicColoredTarget(binding.ovalCover) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    coverColors = colors
                    applyColors()
                }
            })
    }

    private fun updatePlayPause() {
        binding.ovalPlayPause.setImageResource(
            if (MusicPlayerRemote.isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow
        )
    }

    /** Move the oval: [left] and [top] are extra offsets (status bar, navigation rail). */
    fun setPosition(left: Int, top: Int) {
        baseLeft = left
        baseTop = top
        moveTo(
            defaultLeft() + PreferenceUtil.miniPlayerOffsetX,
            defaultTop() + PreferenceUtil.miniPlayerOffsetY
        )
    }

    /** [visible] = oval mode is on and there is something in the queue. */
    fun update(visible: Boolean, alpha: Float) {
        val a = alpha.coerceIn(0f, 1f)
        binding.root.alpha = a
        binding.root.isGone = !visible || a == 0f
    }

    fun start() {
        updateSong()
        updatePlayPause()
        progressHelper.start()
    }

    fun stop() {
        progressHelper.stop()
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        binding.ovalProgress.max = total
        binding.ovalProgress.progress = progress
    }

    override fun onServiceConnected() {
        updateSong()
        updatePlayPause()
    }

    override fun onPlayingMetaChanged() {
        updateSong()
    }

    override fun onPlayStateChanged() {
        updatePlayPause()
    }

    override fun onServiceDisconnected() {}
    override fun onQueueChanged() {}
    override fun onFavoriteStateChanged() {}
    override fun onRepeatModeChanged() {}
    override fun onShuffleModeChanged() {}
    override fun onMediaStoreChanged() {}

    companion object {
        private const val SHAPE_PILL = 1
        private const val SHAPE_CIRCLE = 2
        private const val BG_COVER = 0
        private const val BG_TRANSPARENT = 1
        private const val BG_DARK = 2
    }
}
