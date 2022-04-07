package code.name.monkey.retromusic.activities.base

import android.os.Bundle
import android.view.ViewStub
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.cast.CastHelper
import code.name.monkey.retromusic.cast.RetroSessionManager
import code.name.monkey.retromusic.cast.RetroWebServer
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability


abstract class AbsCastActivity : AbsSlidingMusicPanelActivity() {

    private var mCastSession: CastSession? = null
    private lateinit var sessionManager: SessionManager
    private var webServer: RetroWebServer? = null
    private var playServicesAvailable: Boolean = false

    private val sessionManagerListener by lazy {
        object : RetroSessionManager {
            override fun onSessionStarting(castSession: CastSession) {
                invalidateOptionsMenu()
                webServer = RetroWebServer.getInstance(this@AbsCastActivity)
                webServer?.start()
            }

            override fun onSessionStarted(castSession: CastSession, p1: String) {
                invalidateOptionsMenu()
                mCastSession = castSession
                loadCastQueue(MusicPlayerRemote.position)
                inflateCastController()
                MusicPlayerRemote.isCasting = true
                setAllowDragging(false)
                collapsePanel()
            }

            override fun onSessionEnding(p0: CastSession) {
                invalidateOptionsMenu()
                webServer?.stop()
            }

            override fun onSessionEnded(castSession: CastSession, p1: Int) {
                invalidateOptionsMenu()
                if (mCastSession == castSession) {
                    mCastSession = null
                }
                MusicPlayerRemote.isCasting = false
                setAllowDragging(true)
            }

            override fun onSessionResumed(castSession: CastSession, p1: Boolean) {
                invalidateOptionsMenu()
                mCastSession = castSession
                loadCastQueue(MusicPlayerRemote.position)
                inflateCastController()
                MusicPlayerRemote.isCasting = true
                setAllowDragging(false)
                collapsePanel()
            }

            override fun onSessionSuspended(castSession: CastSession, p1: Int) {
                invalidateOptionsMenu()
                if (mCastSession == castSession) {
                    mCastSession = null
                }
                MusicPlayerRemote.isCasting = false
                setAllowDragging(true)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playServicesAvailable = try {
            GoogleApiAvailability
                .getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        } catch (e: Exception) {
            false
        }
        if (playServicesAvailable) {
            setupCast()
        }
    }

    private fun setupCast() {
        sessionManager = CastContext.getSharedInstance(applicationContext).sessionManager
    }

    override fun onResume() {
        if (playServicesAvailable) {
            sessionManager.addSessionManagerListener(
                sessionManagerListener,
                CastSession::class.java
            )
            if (mCastSession == null) {
                mCastSession = sessionManager.currentCastSession
            }
        }
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        mCastSession = null
    }

    private fun songChanged(position: Int) {
        loadCastQueue(position)
    }

    fun loadCastQueue(position: Int) {
        if (!MusicPlayerRemote.playingQueue.isNullOrEmpty()) {
            mCastSession?.let {
                CastHelper.castQueue(
                    it,
                    MusicPlayerRemote.playingQueue,
                    position,
                    MusicPlayerRemote.songProgressMillis.toLong()
                )
            }
        } else {
            mCastSession?.let { CastHelper.castSong(it, MusicPlayerRemote.currentSong) }
        }
    }

    override fun onPlayingMetaChanged() {
        super.onPlayingMetaChanged()
        if (playServicesAvailable) {
            songChanged(MusicPlayerRemote.position)
        }
    }

    fun inflateCastController() {
        findViewById<ViewStub>(R.id.cast_stub)?.inflate()
    }
}