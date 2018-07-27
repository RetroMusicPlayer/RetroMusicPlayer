package code.name.monkey.retromusic.ui.activities.base;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import code.name.monkey.retromusic.interfaces.MusicServiceEventListener;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.helper.MusicPlayerRemote;

import static code.name.monkey.retromusic.Constants.MEDIA_STORE_CHANGED;
import static code.name.monkey.retromusic.Constants.META_CHANGED;
import static code.name.monkey.retromusic.Constants.PLAY_STATE_CHANGED;
import static code.name.monkey.retromusic.Constants.QUEUE_CHANGED;
import static code.name.monkey.retromusic.Constants.REPEAT_MODE_CHANGED;
import static code.name.monkey.retromusic.Constants.SHUFFLE_MODE_CHANGED;


public abstract class AbsMusicServiceActivity extends AbsBaseActivity implements MusicServiceEventListener {
    public static final String TAG = AbsMusicServiceActivity.class.getSimpleName();

    private final ArrayList<MusicServiceEventListener> mMusicServiceEventListeners = new ArrayList<>();

    private MusicPlayerRemote.ServiceToken serviceToken;
    private MusicStateReceiver musicStateReceiver;
    private boolean receiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceToken = MusicPlayerRemote.bindToService(this, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AbsMusicServiceActivity.this.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                AbsMusicServiceActivity.this.onServiceDisconnected();
            }
        });

        setPermissionDeniedMessage(getString(R.string.permission_external_storage_denied));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayerRemote.unbindFromService(serviceToken);
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver);
            receiverRegistered = false;
        }
    }

    public void addMusicServiceEventListener(final MusicServiceEventListener listener) {
        if (listener != null) {
            mMusicServiceEventListeners.add(listener);
        }
    }

    public void removeMusicServiceEventListener(final MusicServiceEventListener listener) {
        if (listener != null) {
            mMusicServiceEventListeners.remove(listener);
        }
    }

    @Override
    public void onServiceConnected() {
        if (!receiverRegistered) {
            musicStateReceiver = new MusicStateReceiver(this);

            final IntentFilter filter = new IntentFilter();
            filter.addAction(PLAY_STATE_CHANGED);
            filter.addAction(SHUFFLE_MODE_CHANGED);
            filter.addAction(REPEAT_MODE_CHANGED);
            filter.addAction(META_CHANGED);
            filter.addAction(QUEUE_CHANGED);
            filter.addAction(MEDIA_STORE_CHANGED);

            registerReceiver(musicStateReceiver, filter);

            receiverRegistered = true;
        }

        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onServiceConnected();
            }
        }
    }

    @Override
    public void onServiceDisconnected() {
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver);
            receiverRegistered = false;
        }

        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onServiceDisconnected();
            }
        }
    }

    @Override
    public void onPlayingMetaChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onPlayingMetaChanged();
            }
        }
    }

    @Override
    public void onQueueChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onQueueChanged();
            }
        }
    }

    @Override
    public void onPlayStateChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onPlayStateChanged();
            }
        }
    }

    @Override
    public void onMediaStoreChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onMediaStoreChanged();
            }
        }
    }

    @Override
    public void onRepeatModeChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onRepeatModeChanged();
            }
        }
    }

    @Override
    public void onShuffleModeChanged() {
        for (MusicServiceEventListener listener : mMusicServiceEventListeners) {
            if (listener != null) {
                listener.onShuffleModeChanged();
            }
        }
    }

    @Override
    protected void onHasPermissionsChanged(boolean hasPermissions) {
        super.onHasPermissionsChanged(hasPermissions);
        Intent intent = new Intent(MEDIA_STORE_CHANGED);
        intent.putExtra("from_permissions_changed", true); // just in case we need to know this at some point
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    protected String[] getPermissionsToRequest() {
        return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    private static final class MusicStateReceiver extends BroadcastReceiver {

        private final WeakReference<AbsMusicServiceActivity> reference;

        public MusicStateReceiver(final AbsMusicServiceActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void onReceive(final Context context, @NonNull final Intent intent) {
            final String action = intent.getAction();
            AbsMusicServiceActivity activity = reference.get();
            if (activity != null && action != null) {
                switch (action) {
                    case META_CHANGED:
                        activity.onPlayingMetaChanged();
                        break;
                    case QUEUE_CHANGED:
                        activity.onQueueChanged();
                        break;
                    case PLAY_STATE_CHANGED:
                        activity.onPlayStateChanged();
                        break;
                    case REPEAT_MODE_CHANGED:
                        activity.onRepeatModeChanged();
                        break;
                    case SHUFFLE_MODE_CHANGED:
                        activity.onShuffleModeChanged();
                        break;
                    case MEDIA_STORE_CHANGED:
                        activity.onMediaStoreChanged();
                        break;
                }
            }
        }
    }
}
