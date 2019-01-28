package code.name.monkey.retromusic.ui.fragments

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.*
import android.view.animation.DecelerateInterpolator
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.MusicProgressViewUpdateHelper
import code.name.monkey.retromusic.helper.PlayPauseButtonOnClickHandler
import code.name.monkey.retromusic.ui.fragments.base.AbsMusicServiceFragment
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import kotlinx.android.synthetic.main.fragment_mini_player.*

open class MiniPlayerFragment : AbsMusicServiceFragment(), MusicProgressViewUpdateHelper.Callback, View.OnClickListener {
    private var progressViewUpdateHelper: MusicProgressViewUpdateHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressViewUpdateHelper = MusicProgressViewUpdateHelper(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.actionPlayingQueue -> NavigationUtil.goToPlayingQueue(activity!!)
            R.id.actionNext -> MusicPlayerRemote.playNextSong()
            R.id.actionPrevious -> MusicPlayerRemote.back()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setBackgroundColor(ThemeStore.primaryColor(context!!))
        view.setOnTouchListener(FlingPlayBackController(context!!))
        //view.setOnClickListener(v -> NavigationUtil.gotoNowPlayingActivity(getContext()));
        setUpMiniPlayer()

        if (RetroUtil.isTablet()) {
            actionNext.visibility = View.VISIBLE
            actionPrevious.visibility = View.VISIBLE
            actionPlayingQueue.visibility = View.VISIBLE
        } else {
            actionNext.visibility = if (PreferenceUtil.getInstance().isExtraMiniExtraControls) View.VISIBLE else View.GONE
            actionPlayingQueue.visibility = if (PreferenceUtil.getInstance().isExtraMiniExtraControls) View.GONE else View.VISIBLE
            actionPrevious.visibility = if (PreferenceUtil.getInstance().isExtraMiniExtraControls) View.VISIBLE else View.GONE
        }

        actionPlayingQueue.setOnClickListener(this)
        actionNext.setOnClickListener(this)
        actionPrevious.setOnClickListener(this)
    }

    private fun setUpMiniPlayer() {
        setUpPlayPauseButton()
        progressBar.progressTintList = ColorStateList.valueOf(ThemeStore.accentColor(activity!!))
    }

    private fun setUpPlayPauseButton() {
        miniPlayerPlayPauseButton.setOnClickListener(PlayPauseButtonOnClickHandler())
    }

    private fun updateSongTitle() {
        val builder = SpannableStringBuilder()

        val song = MusicPlayerRemote.currentSong
        val title = SpannableString(song.title)
        title.setSpan(ForegroundColorSpan(ThemeStore.textColorPrimary(context!!)), 0, title.length, 0)

        val text = SpannableString(song.artistName)
        text.setSpan(ForegroundColorSpan(ThemeStore.textColorSecondary(context!!)), 0, text.length, 0)

        builder.append(title).append(" • ").append(text)

        miniPlayerTitle.isSelected = true
        miniPlayerTitle.text = builder
    }

    override fun onServiceConnected() {
        updateSongTitle()
        updatePlayPauseDrawableState()
    }

    override fun onPlayingMetaChanged() {
        updateSongTitle()
    }

    override fun onPlayStateChanged() {
        updatePlayPauseDrawableState()
    }

    override fun onUpdateProgressViews(progress: Int, total: Int) {
        progressBar.max = total
        val animator = ObjectAnimator.ofInt(progressBar, "progress", progress)
        animator.duration = 1000
        animator.interpolator = DecelerateInterpolator()
        animator.start()
    }

    override fun onResume() {
        super.onResume()
        progressViewUpdateHelper!!.start()
    }

    override fun onPause() {
        super.onPause()
        progressViewUpdateHelper!!.stop()
    }

    protected fun updatePlayPauseDrawableState() {
        if (MusicPlayerRemote.isPlaying) {
            miniPlayerPlayPauseButton!!.setImageResource(R.drawable.ic_pause_white_24dp)
        } else {
            miniPlayerPlayPauseButton!!.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        }
    }

    fun setColor(playerFragmentColor: Int) {
        view!!.setBackgroundColor(playerFragmentColor)
    }


    class FlingPlayBackController(context: Context) : View.OnTouchListener {

        private var flingPlayBackController: GestureDetector

        init {
            flingPlayBackController = GestureDetector(context,
                    object : GestureDetector.SimpleOnGestureListener() {
                        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                                             velocityY: Float): Boolean {
                            if (Math.abs(velocityX) > Math.abs(velocityY)) {
                                if (velocityX < 0) {
                                    MusicPlayerRemote.playNextSong()
                                    return true
                                } else if (velocityX > 0) {
                                    MusicPlayerRemote.playPreviousSong()
                                    return true
                                }
                            }
                            return false
                        }
                    })
        }

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(v: View, event: MotionEvent): Boolean {
            return flingPlayBackController.onTouchEvent(event)
        }
    }
}
