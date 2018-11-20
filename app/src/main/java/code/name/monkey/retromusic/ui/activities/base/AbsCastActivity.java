package code.name.monkey.retromusic.ui.activities.base;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.cast.WebServer;

public abstract class AbsCastActivity extends AbsBaseActivity {
    private static final String TAG = "AbsCastActivity";

    public boolean playServicesAvailable = false;

    private CastContext castContext;
    private SessionManagerListener<CastSession> sessionManagerListener;
    private CastSession castSession;
    private WebServer castServer;

    private void setupCastListener() {
        sessionManagerListener = new SessionManagerListener<CastSession>() {

            @Override
            public void onSessionEnded(CastSession session, int error) {
                onApplicationDisconnected();
                Log.i(TAG, "onSessionEnded: ");
            }

            @Override
            public void onSessionResumed(CastSession session, boolean wasSuspended) {
                onApplicationConnected(session);
                Log.i(TAG, "onSessionResumed: ");
            }

            @Override
            public void onSessionResumeFailed(CastSession session, int error) {
                onApplicationDisconnected();
                Log.i(TAG, "onSessionResumeFailed: ");
            }

            @Override
            public void onSessionStarted(CastSession session, String sessionId) {
                onApplicationConnected(session);
                Log.i(TAG, "onSessionStarted: ");
            }

            @Override
            public void onSessionStartFailed(CastSession session, int error) {
                onApplicationDisconnected();
                Log.i(TAG, "onSessionStartFailed: ");
            }

            @Override
            public void onSessionStarting(CastSession session) {
            }

            @Override
            public void onSessionEnding(CastSession session) {
            }

            @Override
            public void onSessionResuming(CastSession session, String sessionId) {
            }

            @Override
            public void onSessionSuspended(CastSession session, int reason) {
            }

            private void onApplicationConnected(CastSession castSession) {
                AbsCastActivity.this.castSession = castSession;
                castServer = new WebServer(getApplicationContext());
                try {
                    castServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                supportInvalidateOptionsMenu();
                showCastMiniController();
            }

            private void onApplicationDisconnected() {
                if (castServer != null) {
                    castServer.stop();
                }
                supportInvalidateOptionsMenu();
                hideCastMiniController();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            playServicesAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS;
        } catch (Exception ignored) {

        }

        if (playServicesAvailable)
            initCast();


    }

    @Override
    protected void onResume() {
        if (playServicesAvailable) {
            castContext.getSessionManager().addSessionManagerListener(sessionManagerListener, CastSession.class);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playServicesAvailable) {
            castContext.getSessionManager().removeSessionManagerListener(sessionManagerListener, CastSession.class);
        }
    }

    private void initCast() {
        setupCastListener();
        castContext = CastContext.getSharedInstance(this);
        castSession = castContext.getSessionManager().getCurrentCastSession();
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_cast, menu);
         if (playServicesAvailable) {
             CastButtonFactory.setUpMediaRouteButton(getApplicationContext(), menu, R.id.media_route_menu_item);
         }
         return true;
     }*/

    public void showCastMiniController() {
        //implement by overriding in activities
    }

    public void hideCastMiniController() {
        //implement by overriding in activities
    }

    public CastSession getCastSession() {
        return castSession;
    }
}
