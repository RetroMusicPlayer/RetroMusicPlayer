package code.name.monkey.retromusic.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
import androidx.core.view.ViewCompat
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.ui.activities.base.AbsMusicServiceActivity
import code.name.monkey.retromusic.ui.fragments.player.lockscreen.LockScreenPlayerControlsFragment
import com.bumptech.glide.Glide
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrListener
import com.r0adkll.slidr.model.SlidrPosition

class LockScreenActivity : AbsMusicServiceActivity() {
    private var mFragment: LockScreenPlayerControlsFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)

        setDrawUnderStatusBar()
        setContentView(R.layout.activity_lock_screen_old_style)

        hideStatusBar()
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)

        val config = SlidrConfig.Builder()
                .position(SlidrPosition.BOTTOM)
                .build()

        Slidr.attach(this, config)

        mFragment = supportFragmentManager.findFragmentById(R.id.playback_controls_fragment) as LockScreenPlayerControlsFragment?

        findViewById<View>(R.id.slide).apply {
            translationY = 100f
            alpha = 0f
            ViewCompat.animate(this)
                    .translationY(0f)
                    .alpha(1f)
                    .setDuration(1500)
                    .start()
        }

        findViewById<View>(R.id.root_layout).setBackgroundColor(ThemeStore.primaryColor(this))
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
                .generatePalette(this)
                .build().into(object : RetroMusicColoredTarget(findViewById(R.id.image)) {
                    override fun onColorReady(color: Int) {
                        mFragment!!.setDark(color)
                    }
                })
    }
}