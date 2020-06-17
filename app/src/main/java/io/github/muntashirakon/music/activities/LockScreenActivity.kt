package io.github.muntashirakon.music.activities

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.activities.base.AbsMusicServiceActivity
import io.github.muntashirakon.music.fragments.player.lockscreen.LockScreenPlayerControlsFragment
import io.github.muntashirakon.music.glide.RetroMusicColoredTarget
import io.github.muntashirakon.music.glide.SongGlideRequest
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.activity_lock_screen.*

class LockScreenActivity : AbsMusicServiceActivity() {
    private var fragment: LockScreenPlayerControlsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            this.window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        setContentView(R.layout.activity_lock_screen)
        hideStatusBar()
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        val config = SlidrConfig.Builder().listener(object : SlidrListener {
            override fun onSlideStateChanged(state: Int) {
            }

            override fun onSlideChange(percent: Float) {
            }

            override fun onSlideOpened() {
            }

            override fun onSlideClosed(): Boolean {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val keyguardManager =
                        getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                    keyguardManager.requestDismissKeyguard(this@LockScreenActivity, null)
                }
                finish()
                return true
            }
        }).position(SlidrPosition.BOTTOM).build()

        Slidr.attach(this, config)

        fragment =
            supportFragmentManager.findFragmentById(R.id.playback_controls_fragment) as LockScreenPlayerControlsFragment?

        findViewById<View>(R.id.slide).apply {
            translationY = 100f
            alpha = 0f
            ViewCompat.animate(this).translationY(0f).alpha(1f).setDuration(1500).start()
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        updateSongs()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        updateSongs()
    }

    private fun updateSongs() {
        val song = MusicPlayerRemote.currentSong
        SongGlideRequest.Builder.from(Glide.with(this), song).checkIgnoreMediaStore(this)
            .generatePalette(this).build().dontAnimate()
            .into(object : RetroMusicColoredTarget(image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    fragment?.setColor(colors)
                }
            })
    }
}