package code.name.monkey.retromusic.activities

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.ViewCompat
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.fragments.player.lockscreen.LockScreenPlayerControlsFragment
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.activity_album.*

class LockScreenActivity : AbsMusicServiceActivity() {
    private var fragment: LockScreenPlayerControlsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            this.window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
        setDrawUnderStatusBar()
        setContentView(R.layout.activity_lock_screen)

        hideStatusBar()
        setStatusbarColorAuto()
        setNavigationBarColorPrimary()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        val config = SlidrConfig.Builder()
                .listener(object : SlidrListener {
                    override fun onSlideStateChanged(state: Int) {

                    }

                    override fun onSlideChange(percent: Float) {

                    }

                    override fun onSlideOpened() {

                    }

                    override fun onSlideClosed(): Boolean {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                            keyguardManager.requestDismissKeyguard(this@LockScreenActivity, null)
                        }
                        finish()
                        return true
                    }
                })
                .position(SlidrPosition.BOTTOM)
                .build()

        Slidr.attach(this, config)

        fragment = supportFragmentManager.findFragmentById(R.id.playback_controls_fragment) as LockScreenPlayerControlsFragment?

        findViewById<View>(R.id.slide).apply {
            translationY = 100f
            alpha = 0f
            ViewCompat.animate(this)
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(1500)
                    .start()
        }

        findViewById<View>(R.id.root_layout).setBackgroundColor(ATHUtil.resolveColor(this, R.attr.colorPrimary))
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
        SongGlideRequest.Builder.from(Glide.with(this), song)
                .checkIgnoreMediaStore(this)
                .generatePalette(this).build()
                .dontAnimate()
                .into(object : RetroMusicColoredTarget(image) {
                    override fun onColorReady(color: Int) {
                        fragment?.setDark(color)
                    }
                })
    }
}