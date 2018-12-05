package code.name.monkey.retromusic.ui.activities.base

import android.os.Bundle
import android.util.Log
import code.name.monkey.retromusic.cast.WebServer
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import java.io.IOException

abstract class AbsCastActivity : AbsBaseActivity() {

    var playServicesAvailable = false

    private var castContext: CastContext? = null
    private var sessionManagerListener: SessionManagerListener<CastSession>? = null
    var castSession: CastSession? = null
        private set
    private var castServer: WebServer? = null

    private fun setupCastListener() {
        sessionManagerListener = object : SessionManagerListener<CastSession> {

            override fun onSessionEnded(session: CastSession, error: Int) {
                onApplicationDisconnected()
                Log.i(TAG, "onSessionEnded: ")
            }

            override fun onSessionResumed(session: CastSession, wasSuspended: Boolean) {
                onApplicationConnected(session)
                Log.i(TAG, "onSessionResumed: ")
            }

            override fun onSessionResumeFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
                Log.i(TAG, "onSessionResumeFailed: ")
            }

            override fun onSessionStarted(session: CastSession, sessionId: String) {
                onApplicationConnected(session)
                Log.i(TAG, "onSessionStarted: ")
            }

            override fun onSessionStartFailed(session: CastSession, error: Int) {
                onApplicationDisconnected()
                Log.i(TAG, "onSessionStartFailed: ")
            }

            override fun onSessionStarting(session: CastSession) {}

            override fun onSessionEnding(session: CastSession) {}

            override fun onSessionResuming(session: CastSession, sessionId: String) {}

            override fun onSessionSuspended(session: CastSession, reason: Int) {}

            private fun onApplicationConnected(castSession: CastSession) {
                this@AbsCastActivity.castSession = castSession
                castServer = WebServer(applicationContext)
                try {
                    castServer!!.start()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                invalidateOptionsMenu()
                showCastMiniController()
            }

            private fun onApplicationDisconnected() {
                if (castServer != null) {
                    castServer!!.stop()
                }
                invalidateOptionsMenu()
                hideCastMiniController()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            playServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS
        } catch (ignored: Exception) {

        }

        if (playServicesAvailable)
            initCast()


    }

    override fun onResume() {
        if (playServicesAvailable) {
            castContext!!.sessionManager.addSessionManagerListener(sessionManagerListener!!, CastSession::class.java)
        }
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (playServicesAvailable) {
            castContext!!.sessionManager.removeSessionManagerListener(sessionManagerListener, CastSession::class.java)
        }
    }

    private fun initCast() {
        setupCastListener()
        castContext = CastContext.getSharedInstance(this)
        castSession = castContext!!.sessionManager.currentCastSession
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_cast, menu);
         if (playServicesAvailable) {
             CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
         }
         return true;
     }*/

    open fun showCastMiniController() {
        //implement by overriding in activities
    }

    open fun hideCastMiniController() {
        //implement by overriding in activities
    }

    companion object {
        private val TAG = "AbsCastActivity"
    }
}
