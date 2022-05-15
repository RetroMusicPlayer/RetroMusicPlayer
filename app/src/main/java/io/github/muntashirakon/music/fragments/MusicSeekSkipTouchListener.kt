package io.github.muntashirakon.music.fragments

import android.annotation.SuppressLint
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import kotlinx.coroutines.*

/**
 * @param activity, Activity
 * @param next, if the button is next, if false then it's considered previous
 */
class MusicSeekSkipTouchListener(val activity: FragmentActivity, val next: Boolean) :
    View.OnTouchListener {

    var job: Job? = null
    var counter = 0
    var wasSeeking = false

    private val gestureDetector = GestureDetector(activity, object :
        GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent?): Boolean {
            job = activity.lifecycleScope.launch(Dispatchers.Default) {
                counter = 0
                while (isActive) {
                    delay(500)
                    wasSeeking = true
                    var seekingDuration = MusicPlayerRemote.songProgressMillis
                    if (next) {
                        seekingDuration += 5000 * (counter.floorDiv(2) + 1)
                    } else {
                        seekingDuration -= 5000 * (counter.floorDiv(2) + 1)
                    }
                    MusicPlayerRemote.seekTo(seekingDuration)
                    counter += 1
                }
            }
            return super.onDown(e)
        }
    })

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            job?.cancel()
            if (!wasSeeking) {
                if (next) {
                    MusicPlayerRemote.playNextSong()
                } else {
                    MusicPlayerRemote.back()
                }
            }
            wasSeeking = false
        }
        return gestureDetector.onTouchEvent(event)
    }
}