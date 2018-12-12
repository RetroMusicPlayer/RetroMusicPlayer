package code.name.monkey.retromusic.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.audiofx.AudioEffect;
import android.media.session.MediaSession;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.appwidgets.AppWidgetBig;
import code.name.monkey.retromusic.appwidgets.AppWidgetCard;
import code.name.monkey.retromusic.appwidgets.AppWidgetClassic;
import code.name.monkey.retromusic.appwidgets.AppWidgetSmall;
import code.name.monkey.retromusic.appwidgets.AppWidgetText;
import code.name.monkey.retromusic.glide.BlurTransformation;
import code.name.monkey.retromusic.glide.GlideApp;
import code.name.monkey.retromusic.glide.GlideRequest;
import code.name.monkey.retromusic.glide.RetroGlideExtension;
import code.name.monkey.retromusic.glide.RetroSimpleTarget;
import code.name.monkey.retromusic.helper.ShuffleHelper;
import code.name.monkey.retromusic.helper.StopWatch;
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader;
import code.name.monkey.retromusic.model.AbsCustomPlaylist;
import code.name.monkey.retromusic.model.Playlist;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.providers.HistoryStore;
import code.name.monkey.retromusic.providers.MusicPlaybackQueueStore;
import code.name.monkey.retromusic.providers.SongPlayCountStore;
import code.name.monkey.retromusic.service.notification.PlayingNotification;
import code.name.monkey.retromusic.service.notification.PlayingNotificationImpl24;
import code.name.monkey.retromusic.service.notification.PlayingNotificationOreo;
import code.name.monkey.retromusic.service.playback.Playback;
import code.name.monkey.retromusic.util.MusicUtil;
import code.name.monkey.retromusic.util.PreferenceUtil;
import code.name.monkey.retromusic.util.RetroUtil;

import static code.name.monkey.retromusic.Constants.ACTION_PAUSE;
import static code.name.monkey.retromusic.Constants.ACTION_PLAY;
import static code.name.monkey.retromusic.Constants.ACTION_PLAY_PLAYLIST;
import static code.name.monkey.retromusic.Constants.ACTION_QUIT;
import static code.name.monkey.retromusic.Constants.ACTION_REWIND;
import static code.name.monkey.retromusic.Constants.ACTION_SKIP;
import static code.name.monkey.retromusic.Constants.ACTION_STOP;
import static code.name.monkey.retromusic.Constants.ACTION_TOGGLE_PAUSE;
import static code.name.monkey.retromusic.Constants.APP_WIDGET_UPDATE;
import static code.name.monkey.retromusic.Constants.EXTRA_APP_WIDGET_NAME;
import static code.name.monkey.retromusic.Constants.INTENT_EXTRA_PLAYLIST;
import static code.name.monkey.retromusic.Constants.INTENT_EXTRA_SHUFFLE_MODE;
import static code.name.monkey.retromusic.Constants.MEDIA_STORE_CHANGED;
import static code.name.monkey.retromusic.Constants.META_CHANGED;
import static code.name.monkey.retromusic.Constants.MUSIC_PACKAGE_NAME;
import static code.name.monkey.retromusic.Constants.PLAY_STATE_CHANGED;
import static code.name.monkey.retromusic.Constants.QUEUE_CHANGED;
import static code.name.monkey.retromusic.Constants.REPEAT_MODE_CHANGED;
import static code.name.monkey.retromusic.Constants.RETRO_MUSIC_PACKAGE_NAME;
import static code.name.monkey.retromusic.Constants.SHUFFLE_MODE_CHANGED;

/**
 * @author Karim Abou Zeid (kabouzeid), Andrew Neal
 */
public class MusicService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener, Playback.PlaybackCallbacks {
    public static final String TAG = MusicService.class.getSimpleName();
    public static final String SAVED_POSITION = "POSITION";
    public static final String SAVED_POSITION_IN_TRACK = "POSITION_IN_TRACK";
    public static final String SAVED_SHUFFLE_MODE = "SHUFFLE_MODE";
    public static final String SAVED_REPEAT_MODE = "REPEAT_MODE";
    public static final int RELEASE_WAKELOCK = 0;
    public static final int TRACK_ENDED = 1;
    public static final int TRACK_WENT_TO_NEXT = 2;
    public static final int PLAY_SONG = 3;
    public static final int PREPARE_NEXT = 4;
    public static final int SET_POSITION = 5;
    public static final int RESTORE_QUEUES = 9;
    public static final int SHUFFLE_MODE_NONE = 0;
    public static final int SHUFFLE_MODE_SHUFFLE = 1;
    public static final int REPEAT_MODE_NONE = 0;
    public static final int REPEAT_MODE_ALL = 1;
    public static final int REPEAT_MODE_THIS = 2;
    public static final int SAVE_QUEUES = 0;
    private static final int FOCUS_CHANGE = 6;
    private static final int DUCK = 7;
    private static final int UNDUCK = 8;
    private static final long MEDIA_SESSION_ACTIONS = PlaybackStateCompat.ACTION_PLAY
            | PlaybackStateCompat.ACTION_PAUSE
            | PlaybackStateCompat.ACTION_PLAY_PAUSE
            | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
            | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
            | PlaybackStateCompat.ACTION_STOP
            | PlaybackStateCompat.ACTION_SEEK_TO;
    private final IBinder musicBind = new MusicBinder();
    private AppWidgetBig appWidgetBig = AppWidgetBig.Companion.getInstance();
    private AppWidgetClassic appWidgetClassic = AppWidgetClassic.Companion.getInstance();
    private AppWidgetSmall appWidgetSmall = AppWidgetSmall.Companion.getInstance();
    private AppWidgetCard appWidgetCard = AppWidgetCard.Companion.getInstance();
    private AppWidgetText appWidgetText = AppWidgetText.Companion.getInstance();
    private final BroadcastReceiver widgetIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String command = intent.getStringExtra(EXTRA_APP_WIDGET_NAME);

            final int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            switch (command) {
                case AppWidgetClassic.NAME: {
                    appWidgetClassic.performUpdate(MusicService.this, ids);
                    break;
                }
                case AppWidgetSmall.NAME: {
                    appWidgetSmall.performUpdate(MusicService.this, ids);
                    break;
                }
                case AppWidgetBig.NAME: {
                    appWidgetBig.performUpdate(MusicService.this, ids);
                    break;
                }
                case AppWidgetCard.NAME: {
                    appWidgetCard.performUpdate(MusicService.this, ids);
                    break;
                }
                case AppWidgetText.NAME: {
                    appWidgetText.performUpdate(MusicService.this, ids);
                    break;
                }
            }

        }
    };
    private Playback playback;
    private ArrayList<Song> playingQueue = new ArrayList<>();
    private ArrayList<Song> originalPlayingQueue = new ArrayList<>();
    private int position = -1;
    private int nextPosition = -1;
    private int shuffleMode;
    private int repeatMode;
    private boolean queuesRestored;
    private boolean pausedByTransientLossOfFocus;
    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, @NonNull Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
                pause();
            }
        }
    };
    private PlayingNotification playingNotification;
    private AudioManager audioManager;
    @SuppressWarnings("deprecation")
    private MediaSessionCompat mediaSession;
    private PowerManager.WakeLock wakeLock;
    private PlaybackHandler playerHandler;
    private final AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(final int focusChange) {
            playerHandler.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget();
        }
    };
    private QueueSaveHandler queueSaveHandler;
    private HandlerThread musicPlayerHandlerThread;
    private HandlerThread queueSaveHandlerThread;
    private SongPlayCountHelper songPlayCountHelper = new SongPlayCountHelper();
    private ThrottledSeekHandler throttledSeekHandler;
    private boolean becomingNoisyReceiverRegistered;
    private IntentFilter becomingNoisyReceiverIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private ContentObserver mediaStoreObserver;
    private boolean notHandledMetaChangedForCurrentTrack;
    private PhoneStateListener phoneStateListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //Not in call: Play music
                    play();
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //A call is dialing, active or on hold
                    pause();
                    break;
                default:
            }
            super.onCallStateChanged(state, incomingNumber);
        }
    };
    private boolean isServiceBound;
    private Handler uiThreadHandler;
    private IntentFilter headsetReceiverIntentFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
    private boolean headsetReceiverRegistered = false;
    private BroadcastReceiver headsetReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case Intent.ACTION_HEADSET_PLUG:
                        int state = intent.getIntExtra("state", -1);
                        switch (state) {
                            case 0:
                                Log.d(TAG, "Headset unplugged");
                                pause();
                                break;
                            case 1:
                                Log.d(TAG, "Headset plugged");
                                play();
                                break;
                        }
                        break;
                }
            }
        }
    };

    private static String getTrackUri(@NonNull Song song) {
        return MusicUtil.getSongFileUri(song.getId()).toString();
    }

    private static Bitmap copy(Bitmap bitmap) {
        Bitmap.Config config = bitmap.getConfig();
        if (config == null) {
            config = Bitmap.Config.RGB_565;
        }
        try {
            return bitmap.copy(config, false);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();

        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }

        final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        }
        wakeLock.setReferenceCounted(false);

        musicPlayerHandlerThread = new HandlerThread("PlaybackHandler");
        musicPlayerHandlerThread.start();
        playerHandler = new PlaybackHandler(this, musicPlayerHandlerThread.getLooper());

        playback = new MultiPlayer(this);
        playback.setCallbacks(this);

        setupMediaSession();

        // queue saving needs to run on a separate thread so that it doesn't block the playback handler events
        queueSaveHandlerThread = new HandlerThread("QueueSaveHandler", Process.THREAD_PRIORITY_BACKGROUND);
        queueSaveHandlerThread.start();
        queueSaveHandler = new QueueSaveHandler(this, queueSaveHandlerThread.getLooper());

        uiThreadHandler = new Handler();

        registerReceiver(widgetIntentReceiver, new IntentFilter(APP_WIDGET_UPDATE));

        initNotification();

        mediaStoreObserver = new MediaStoreObserver(playerHandler);
        throttledSeekHandler = new ThrottledSeekHandler(playerHandler);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, true, mediaStoreObserver);
        getContentResolver().registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mediaStoreObserver);

        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this);

        restoreState();

        mediaSession.setActive(true);

        sendBroadcast(new Intent("code.name.monkey.retromusic.RETRO_MUSIC_SERVICE_CREATED"));

        registerHeadsetEvents();


    }

    private AudioManager getAudioManager() {
        if (audioManager == null) {
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        }
        return audioManager;
    }

    private void setupMediaSession() {
        ComponentName mediaButtonReceiverComponentName = new ComponentName(getApplicationContext(), MediaButtonIntentReceiver.class);

        Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        mediaButtonIntent.setComponent(mediaButtonReceiverComponentName);


        PendingIntent mediaButtonReceiverPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, mediaButtonIntent, 0);

        mediaSession = new MediaSessionCompat(this, "RetroMusicPlayer", mediaButtonReceiverComponentName, mediaButtonReceiverPendingIntent);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                play();
            }

            @Override
            public void onPause() {
                pause();
            }

            @Override
            public void onSkipToNext() {
                playNextSong(true);
            }

            @Override
            public void onSkipToPrevious() {
                back(true);
            }

            @Override
            public void onStop() {
                quit();
            }

            @Override
            public void onSeekTo(long pos) {
                seek((int) pos);
            }

            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
                return MediaButtonIntentReceiver.Companion.handleIntent(MusicService.this, mediaButtonEvent);
            }
        });

        mediaSession.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS
                | MediaSession.FLAG_HANDLES_MEDIA_BUTTONS);

        mediaSession.setMediaButtonReceiver(mediaButtonReceiverPendingIntent);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction() != null) {
                restoreQueuesAndPositionIfNecessary();
                String action = intent.getAction();
                switch (action) {
                    case ACTION_TOGGLE_PAUSE:
                        if (isPlaying()) {
                            pause();
                        } else {
                            play();
                        }
                        break;
                    case ACTION_PAUSE:
                        pause();
                        break;
                    case ACTION_PLAY:
                        play();
                        break;
                    case ACTION_PLAY_PLAYLIST:
                        Playlist playlist = intent.getParcelableExtra(INTENT_EXTRA_PLAYLIST);
                        int shuffleMode = intent.getIntExtra(INTENT_EXTRA_SHUFFLE_MODE, getShuffleMode());
                        if (playlist != null) {
                            ArrayList<Song> playlistSongs;
                            if (playlist instanceof AbsCustomPlaylist) {
                                playlistSongs = ((AbsCustomPlaylist) playlist).getSongs(getApplicationContext()).blockingFirst();
                            } else {
                                //noinspection unchecked
                                playlistSongs = PlaylistSongsLoader.INSTANCE.getPlaylistSongList(getApplicationContext(), playlist.id).blockingFirst();
                            }
                            if (!playlistSongs.isEmpty()) {
                                if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                                    int startPosition;
                                    startPosition = new Random().nextInt(playlistSongs.size());
                                    openQueue(playlistSongs, startPosition, true);
                                    setShuffleMode(shuffleMode);
                                } else {
                                    openQueue(playlistSongs, 0, true);
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show();
                        }
                        break;
                    case ACTION_REWIND:
                        back(true);
                        break;
                    case ACTION_SKIP:
                        playNextSong(true);
                        break;
                    case ACTION_STOP:
                    case ACTION_QUIT:
                        return quit();
                }
            }
        }

        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        unregisterReceiver(widgetIntentReceiver);
        if (becomingNoisyReceiverRegistered) {
            unregisterReceiver(becomingNoisyReceiver);
            becomingNoisyReceiverRegistered = false;
        }
        if (headsetReceiverRegistered) {
            unregisterReceiver(headsetReceiver);
            headsetReceiverRegistered = false;
        }
        mediaSession.setActive(false);
        quit();
        releaseResources();
        getContentResolver().unregisterContentObserver(mediaStoreObserver);
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this);
        wakeLock.release();

        sendBroadcast(new Intent("code.name.monkey.retromusic.RETRO_MUSIC_MUSIC_SERVICE_DESTROYED"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        isServiceBound = true;
        return musicBind;
    }

    @Override
    public void onRebind(Intent intent) {
        isServiceBound = true;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isServiceBound = false;
        if (!isPlaying()) {
            stopSelf();
        }
        return true;
    }

    private void saveQueuesImpl() {
        MusicPlaybackQueueStore.getInstance(this).saveQueues(playingQueue, originalPlayingQueue);
    }

    private void savePosition() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(SAVED_POSITION, getPosition()).apply();
    }

    private void savePositionInTrack() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(SAVED_POSITION_IN_TRACK, getSongProgressMillis()).apply();
    }

    public void saveState() {
        saveQueues();
        savePosition();
        savePositionInTrack();
    }

    private void saveQueues() {
        queueSaveHandler.removeMessages(SAVE_QUEUES);
        queueSaveHandler.sendEmptyMessage(SAVE_QUEUES);
    }

    private void restoreState() {
        shuffleMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_SHUFFLE_MODE, 0);
        repeatMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_REPEAT_MODE, 0);
        handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED);
        handleAndSendChangeInternal(REPEAT_MODE_CHANGED);

        playerHandler.removeMessages(RESTORE_QUEUES);
        playerHandler.sendEmptyMessage(RESTORE_QUEUES);
    }

    private synchronized void restoreQueuesAndPositionIfNecessary() {
        if (!queuesRestored && playingQueue.isEmpty()) {
            ArrayList<Song> restoredQueue = MusicPlaybackQueueStore.getInstance(this).getSavedPlayingQueue()
                    .blockingFirst();

            ArrayList<Song> restoredOriginalQueue = MusicPlaybackQueueStore.getInstance(this).getSavedOriginalPlayingQueue()
                    .blockingFirst();

            int restoredPosition = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_POSITION, -1);
            int restoredPositionInTrack = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_POSITION_IN_TRACK, -1);

            if (restoredQueue.size() > 0 && restoredQueue.size() == restoredOriginalQueue.size() && restoredPosition != -1) {
                this.originalPlayingQueue = restoredOriginalQueue;
                this.playingQueue = restoredQueue;

                position = restoredPosition;
                openCurrent();
                prepareNext();

                if (restoredPositionInTrack > 0) seek(restoredPositionInTrack);

                notHandledMetaChangedForCurrentTrack = true;
                sendChangeInternal(META_CHANGED);
                sendChangeInternal(QUEUE_CHANGED);
            }
        }
        queuesRestored = true;
    }

    private int quit() {
        pause();
        playingNotification.stop();

        if (isServiceBound) {
            return START_STICKY;
        } else {
            closeAudioEffectSession();
            getAudioManager().abandonAudioFocus(audioFocusListener);
            stopSelf();
            return START_NOT_STICKY;
        }
    }

    private void releaseResources() {
        playerHandler.removeCallbacksAndMessages(null);
        musicPlayerHandlerThread.quitSafely();
        queueSaveHandler.removeCallbacksAndMessages(null);
        queueSaveHandlerThread.quitSafely();
        playback.release();
        playback = null;
        mediaSession.release();
    }

    public boolean isPlaying() {
        return playback != null && playback.isPlaying();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(final int position) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler.removeMessages(SET_POSITION);
        playerHandler.obtainMessage(SET_POSITION, position, 0).sendToTarget();
    }

    public void playNextSong(boolean force) {
        playSongAt(getNextPosition(force));
    }

    private boolean openTrackAndPrepareNextAt(int position) {
        synchronized (this) {
            this.position = position;
            boolean prepared = openCurrent();
            if (prepared) prepareNextImpl();
            notifyChange(META_CHANGED);
            notHandledMetaChangedForCurrentTrack = false;
            return prepared;
        }
    }

    private boolean openCurrent() {
        synchronized (this) {
            try {
                return playback.setDataSource(getTrackUri(getCurrentSong()));
            } catch (Exception e) {
                return false;
            }
        }
    }

    private void prepareNext() {
        playerHandler.removeMessages(PREPARE_NEXT);
        playerHandler.obtainMessage(PREPARE_NEXT).sendToTarget();
    }

    private boolean prepareNextImpl() {
        synchronized (this) {
            try {
                int nextPosition = getNextPosition(false);
                playback.setNextDataSource(getTrackUri(getSongAt(nextPosition)));
                this.nextPosition = nextPosition;
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

    private void closeAudioEffectSession() {
        final Intent audioEffectsIntent = new Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION);
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, playback.getAudioSessionId());
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, getPackageName());
        sendBroadcast(audioEffectsIntent);
    }

    private boolean requestFocus() {
        return (getAudioManager().requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);
    }

    public void initNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !PreferenceUtil.getInstance().classicNotification()) {
            playingNotification = new PlayingNotificationImpl24();
        } else {
            playingNotification = new PlayingNotificationOreo();
        }
        playingNotification.init(this);
    }

    public void updateNotification() {
        if (playingNotification != null && getCurrentSong().getId() != -1) {
            playingNotification.update();
        }
    }

    private void updateMediaSessionPlaybackState() {
        mediaSession.setPlaybackState(
                new PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(isPlaying() ? PlaybackStateCompat.STATE_PLAYING : PlaybackStateCompat.STATE_PAUSED,
                                getPosition(), 1)
                        .build());
    }

    private void updateMediaSessionMetaData() {
        final Song song = getCurrentSong();

        if (song.getId() == -1) {
            mediaSession.setMetadata(null);
            return;
        }

        final MediaMetadataCompat.Builder metaData = new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, song.getArtistName())
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.getAlbumName())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle())
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.getDuration())
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, getPosition() + 1)
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, song.getYear())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getPlayingQueue().size());
        }

        if (PreferenceUtil.getInstance().albumArtOnLockscreen()) {
            final Point screenSize = RetroUtil.getScreenSize(MusicService.this);
            GlideRequest request = GlideApp.with(MusicService.this)
                    .asBitmap()
                    .load(RetroGlideExtension.getSongModel(song))
                    .transition(RetroGlideExtension.getDefaultTransition())
                    .songOptions(song);
            if (PreferenceUtil.getInstance().blurredAlbumArt()) {
                request.transform(new BlurTransformation.Builder(MusicService.this).build());
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    request.into(new RetroSimpleTarget<Bitmap>(screenSize.x, screenSize.y) {
                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            mediaSession.setMetadata(metaData.build());
                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, Transition<? super Bitmap> glideAnimation) {
                            metaData.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, copy(resource));
                            mediaSession.setMetadata(metaData.build());
                        }
                    });
                }
            });
        } else {
            mediaSession.setMetadata(metaData.build());
        }
    }

    public void runOnUiThread(Runnable runnable) {
        uiThreadHandler.post(runnable);
    }

    public Song getCurrentSong() {
        return getSongAt(getPosition());
    }

    public Song getSongAt(int position) {
        if (position >= 0 && position < getPlayingQueue().size()) {
            return getPlayingQueue().get(position);
        } else {
            return Song.Companion.getEmptySong();
        }
    }

    public int getNextPosition(boolean force) {
        int position = getPosition() + 1;
        switch (getRepeatMode()) {
            case REPEAT_MODE_ALL:
                if (isLastTrack()) {
                    position = 0;
                }
                break;
            case REPEAT_MODE_THIS:
                if (force) {
                    if (isLastTrack()) {
                        position = 0;
                    }
                } else {
                    position -= 1;
                }
                break;
            default:
            case REPEAT_MODE_NONE:
                if (isLastTrack()) {
                    position -= 1;
                }
                break;
        }
        return position;
    }

    private boolean isLastTrack() {
        return getPosition() == getPlayingQueue().size() - 1;
    }

    public ArrayList<Song> getPlayingQueue() {
        return playingQueue;
    }

    public int getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(final int repeatMode) {
        switch (repeatMode) {
            case REPEAT_MODE_NONE:
            case REPEAT_MODE_ALL:
            case REPEAT_MODE_THIS:
                this.repeatMode = repeatMode;
                PreferenceManager.getDefaultSharedPreferences(this).edit()
                        .putInt(SAVED_REPEAT_MODE, repeatMode)
                        .apply();
                prepareNext();
                handleAndSendChangeInternal(REPEAT_MODE_CHANGED);
                break;
        }
    }

    public void openQueue(@Nullable final ArrayList<Song> playingQueue, final int startPosition, final boolean startPlaying) {
        if (playingQueue != null && !playingQueue.isEmpty() && startPosition >= 0 && startPosition < playingQueue.size()) {
            // it is important to copy the playing queue here first as we might add/remove songs later
            originalPlayingQueue = new ArrayList<>(playingQueue);
            this.playingQueue = new ArrayList<>(originalPlayingQueue);

            int position = startPosition;
            if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                ShuffleHelper.INSTANCE.makeShuffleList(this.playingQueue, startPosition);
                position = 0;
            }
            if (startPlaying) {
                playSongAt(position);
            } else {
                setPosition(position);
            }
            notifyChange(QUEUE_CHANGED);
        }
    }

    public void addSong(int position, Song song) {
        playingQueue.add(position, song);
        originalPlayingQueue.add(position, song);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSong(Song song) {
        playingQueue.add(song);
        originalPlayingQueue.add(song);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSongs(int position, List<Song> songs) {
        playingQueue.addAll(position, songs);
        originalPlayingQueue.addAll(position, songs);
        notifyChange(QUEUE_CHANGED);
    }

    public void addSongs(List<Song> songs) {
        playingQueue.addAll(songs);
        originalPlayingQueue.addAll(songs);
        notifyChange(QUEUE_CHANGED);
    }

    public void removeSong(int position) {
        if (getShuffleMode() == SHUFFLE_MODE_NONE) {
            playingQueue.remove(position);
            originalPlayingQueue.remove(position);
        } else {
            originalPlayingQueue.remove(playingQueue.remove(position));
        }

        rePosition(position);

        notifyChange(QUEUE_CHANGED);
    }

    public void removeSong(@NonNull Song song) {
        for (int i = 0; i < playingQueue.size(); i++) {
            if (playingQueue.get(i).getId() == song.getId()) {
                playingQueue.remove(i);
                rePosition(i);
            }
        }
        for (int i = 0; i < originalPlayingQueue.size(); i++) {
            if (originalPlayingQueue.get(i).getId() == song.getId()) {
                originalPlayingQueue.remove(i);
            }
        }
        notifyChange(QUEUE_CHANGED);
    }

    private void rePosition(int deletedPosition) {
        int currentPosition = getPosition();
        if (deletedPosition < currentPosition) {
            position = currentPosition - 1;
        } else if (deletedPosition == currentPosition) {
            if (playingQueue.size() > deletedPosition) {
                setPosition(position);
            } else {
                setPosition(position - 1);
            }
        }
    }

    public void moveSong(int from, int to) {
        if (from == to) return;
        final int currentPosition = getPosition();
        Song songToMove = playingQueue.remove(from);
        playingQueue.add(to, songToMove);
        if (getShuffleMode() == SHUFFLE_MODE_NONE) {
            Song tmpSong = originalPlayingQueue.remove(from);
            originalPlayingQueue.add(to, tmpSong);
        }
        if (from > currentPosition && to <= currentPosition) {
            position = currentPosition + 1;
        } else if (from < currentPosition && to >= currentPosition) {
            position = currentPosition - 1;
        } else if (from == currentPosition) {
            position = to;
        }
        notifyChange(QUEUE_CHANGED);
    }

    public void clearQueue() {
        playingQueue.clear();
        originalPlayingQueue.clear();

        setPosition(-1);
        notifyChange(QUEUE_CHANGED);
    }

    public void playSongAt(final int position) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler.removeMessages(PLAY_SONG);
        playerHandler.obtainMessage(PLAY_SONG, position, 0).sendToTarget();
    }

    private void playSongAtImpl(int position) {
        if (openTrackAndPrepareNextAt(position)) {
            play();
        } else {
            Toast.makeText(this, getResources().getString(R.string.unplayable_file), Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        pausedByTransientLossOfFocus = false;
        if (playback.isPlaying()) {
            playback.pause();
            notifyChange(PLAY_STATE_CHANGED);
        }
    }

    public void play() {
        synchronized (this) {
            if (requestFocus()) {
                if (!playback.isPlaying()) {
                    if (!playback.isInitialized()) {
                        playSongAt(getPosition());
                    } else {
                        playback.start();
                        if (!becomingNoisyReceiverRegistered) {
                            registerReceiver(becomingNoisyReceiver, becomingNoisyReceiverIntentFilter);
                            becomingNoisyReceiverRegistered = true;
                        }
                        if (notHandledMetaChangedForCurrentTrack) {
                            handleChangeInternal(META_CHANGED);
                            notHandledMetaChangedForCurrentTrack = false;
                        }
                        notifyChange(PLAY_STATE_CHANGED);

                        // fixes a bug where the volume would stay ducked because the AudioManager.AUDIOFOCUS_GAIN event is not sent
                        playerHandler.removeMessages(DUCK);
                        playerHandler.sendEmptyMessage(UNDUCK);
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.audio_focus_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void playSongs(ArrayList<Song> songs, int shuffleMode) {
        if (songs != null && !songs.isEmpty()) {
            if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                int startPosition = 0;
                if (!songs.isEmpty()) {
                    startPosition = new Random().nextInt(songs.size());
                }
                openQueue(songs, startPosition, false);
                setShuffleMode(shuffleMode);
            } else {
                openQueue(songs, 0, false);
            }
            play();
        } else {
            Toast.makeText(getApplicationContext(), R.string.playlist_is_empty, Toast.LENGTH_LONG).show();
        }
    }

    public void playPreviousSong(boolean force) {
        playSongAt(getPreviousPosition(force));
    }

    public void back(boolean force) {
        if (getSongProgressMillis() > 2000) {
            seek(0);
        } else {
            playPreviousSong(force);
        }
    }

    public int getPreviousPosition(boolean force) {
        int newPosition = getPosition() - 1;
        switch (repeatMode) {
            case REPEAT_MODE_ALL:
                if (newPosition < 0) {
                    newPosition = getPlayingQueue().size() - 1;
                }
                break;
            case REPEAT_MODE_THIS:
                if (force) {
                    if (newPosition < 0) {
                        newPosition = getPlayingQueue().size() - 1;
                    }
                } else {
                    newPosition = getPosition();
                }
                break;
            default:
            case REPEAT_MODE_NONE:
                if (newPosition < 0) {
                    newPosition = 0;
                }
                break;
        }
        return newPosition;
    }

    public int getSongProgressMillis() {
        return playback.position();
    }

    public int getSongDurationMillis() {
        return playback.duration();
    }

    public long getQueueDurationMillis(int position) {
        long duration = 0;
        for (int i = position + 1; i < playingQueue.size(); i++)
            duration += playingQueue.get(i).getDuration();
        return duration;
    }

    public int seek(int millis) {
        synchronized (this) {
            try {
                int newPosition = playback.seek(millis);
                throttledSeekHandler.notifySeek();
                return newPosition;
            } catch (Exception e) {
                return -1;
            }
        }
    }

    public void cycleRepeatMode() {
        switch (getRepeatMode()) {
            case REPEAT_MODE_NONE:
                setRepeatMode(REPEAT_MODE_ALL);
                break;
            case REPEAT_MODE_ALL:
                setRepeatMode(REPEAT_MODE_THIS);
                break;
            default:
                setRepeatMode(REPEAT_MODE_NONE);
                break;
        }
    }

    public void toggleShuffle() {
        if (getShuffleMode() == SHUFFLE_MODE_NONE) {
            setShuffleMode(SHUFFLE_MODE_SHUFFLE);
        } else {
            setShuffleMode(SHUFFLE_MODE_NONE);
        }
    }

    public int getShuffleMode() {
        return shuffleMode;
    }

    public void setShuffleMode(final int shuffleMode) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putInt(SAVED_SHUFFLE_MODE, shuffleMode)
                .apply();
        switch (shuffleMode) {
            case SHUFFLE_MODE_SHUFFLE:
                this.shuffleMode = shuffleMode;
                ShuffleHelper.INSTANCE.makeShuffleList(this.getPlayingQueue(), getPosition());
                position = 0;
                break;
            case SHUFFLE_MODE_NONE:
                this.shuffleMode = shuffleMode;
                int currentSongId = getCurrentSong().getId();
                playingQueue = new ArrayList<>(originalPlayingQueue);
                int newPosition = 0;
                for (Song song : getPlayingQueue()) {
                    if (song.getId() == currentSongId) {
                        newPosition = getPlayingQueue().indexOf(song);
                    }
                }
                position = newPosition;
                break;
        }
        handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED);
        notifyChange(QUEUE_CHANGED);
    }

    private void notifyChange(@NonNull final String what) {
        handleAndSendChangeInternal(what);
        sendPublicIntent(what);
    }

    private void handleAndSendChangeInternal(@NonNull final String what) {
        handleChangeInternal(what);
        sendChangeInternal(what);
    }

    // to let other apps know whats playing. i.E. last.fm (scrobbling) or musixmatch
    private void sendPublicIntent(@NonNull final String what) {
        final Intent intent = new Intent(what.replace(RETRO_MUSIC_PACKAGE_NAME, MUSIC_PACKAGE_NAME));

        final Song song = getCurrentSong();

        intent.putExtra("id", song.getId());
        intent.putExtra("artist", song.getArtistName());
        intent.putExtra("album", song.getAlbumName());
        intent.putExtra("track", song.getTitle());
        intent.putExtra("duration", song.getDuration());
        intent.putExtra("position", (long) getSongProgressMillis());
        intent.putExtra("playing", isPlaying());
        intent.putExtra("scrobbling_source", RETRO_MUSIC_PACKAGE_NAME);

        sendStickyBroadcast(intent);

    }

    private void sendChangeInternal(final String what) {
        sendBroadcast(new Intent(what));
        appWidgetBig.notifyChange(this, what);
        appWidgetClassic.notifyChange(this, what);
        appWidgetSmall.notifyChange(this, what);
        appWidgetCard.notifyChange(this, what);
        appWidgetText.notifyChange(this, what);
    }

    private void handleChangeInternal(@NonNull final String what) {
        switch (what) {
            case PLAY_STATE_CHANGED:
                updateNotification();
                updateMediaSessionPlaybackState();
                final boolean isPlaying = isPlaying();
                if (!isPlaying && getSongProgressMillis() > 0) {
                    savePositionInTrack();
                }
                songPlayCountHelper.notifyPlayStateChanged(isPlaying);
                break;
            case META_CHANGED:
                updateNotification();
                updateMediaSessionMetaData();
                savePosition();
                savePositionInTrack();
                final Song currentSong = getCurrentSong();
                HistoryStore.getInstance(this).addSongId(currentSong.getId());
                if (songPlayCountHelper.shouldBumpPlayCount()) {
                    SongPlayCountStore.getInstance(this).bumpPlayCount(songPlayCountHelper.getSong().getId());
                }
                songPlayCountHelper.notifySongChanged(currentSong);
                break;
            case QUEUE_CHANGED:
                updateMediaSessionMetaData(); // because playing queue size might have changed
                saveState();
                if (playingQueue.size() > 0) {
                    prepareNext();
                } else {
                    playingNotification.stop();
                }
                break;
        }
    }

    public int getAudioSessionId() {
        return playback.getAudioSessionId();
    }

    public MediaSessionCompat getMediaSession() {
        return mediaSession;
    }

    public void releaseWakeLock() {
        if (wakeLock.isHeld()) {
            wakeLock.release();
        }
    }

    public void acquireWakeLock(long milli) {
        wakeLock.acquire(milli);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceUtil.GAPLESS_PLAYBACK:
                if (sharedPreferences.getBoolean(key, false)) {
                    prepareNext();
                } else {
                    playback.setNextDataSource(null);
                }
                break;
            case PreferenceUtil.ALBUM_ART_ON_LOCKSCREEN:
            case PreferenceUtil.BLURRED_ALBUM_ART:
                updateMediaSessionMetaData();
                break;
            case PreferenceUtil.COLORED_NOTIFICATION:
            case PreferenceUtil.DOMINANT_COLOR:
                updateNotification();
                break;
            case PreferenceUtil.CLASSIC_NOTIFICATION:
                initNotification();
                updateNotification();
                break;
            case PreferenceUtil.TOGGLE_HEADSET:
                registerHeadsetEvents();
                break;
        }
    }

    private void registerHeadsetEvents() {
        if (!headsetReceiverRegistered && PreferenceUtil.getInstance().getHeadsetPlugged()) {
            registerReceiver(headsetReceiver, headsetReceiverIntentFilter);
            headsetReceiverRegistered = true;
        }
    }

    @Override
    public void onTrackWentToNext() {
        playerHandler.sendEmptyMessage(TRACK_WENT_TO_NEXT);
    }

    @Override
    public void onTrackEnded() {
        acquireWakeLock(30000);
        playerHandler.sendEmptyMessage(TRACK_ENDED);
    }


    private static final class QueueSaveHandler extends Handler {
        @NonNull
        private final WeakReference<MusicService> mService;

        QueueSaveHandler(final MusicService service, @NonNull final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            final MusicService service = mService.get();
            switch (msg.what) {
                case SAVE_QUEUES:
                    service.saveQueuesImpl();
                    break;
            }
        }
    }

    private static final class PlaybackHandler extends Handler {
        @NonNull
        private final WeakReference<MusicService> mService;
        private float currentDuckVolume = 1.0f;

        PlaybackHandler(final MusicService service, @NonNull final Looper looper) {
            super(looper);
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(@NonNull final Message msg) {
            final MusicService service = mService.get();
            if (service == null) {
                return;
            }

            switch (msg.what) {
                case DUCK:
                    if (PreferenceUtil.getInstance().audioDucking()) {
                        currentDuckVolume -= .05f;
                        if (currentDuckVolume > .2f) {
                            sendEmptyMessageDelayed(DUCK, 10);
                        } else {
                            currentDuckVolume = .2f;
                        }
                    } else {
                        currentDuckVolume = 1f;
                    }
                    service.playback.setVolume(currentDuckVolume);
                    break;

                case UNDUCK:
                    if (PreferenceUtil.getInstance().audioDucking()) {
                        currentDuckVolume += .03f;
                        if (currentDuckVolume < 1f) {
                            sendEmptyMessageDelayed(UNDUCK, 10);
                        } else {
                            currentDuckVolume = 1f;
                        }
                    } else {
                        currentDuckVolume = 1f;
                    }
                    service.playback.setVolume(currentDuckVolume);
                    break;

                case TRACK_WENT_TO_NEXT:
                    if (service.getRepeatMode() == REPEAT_MODE_NONE && service.isLastTrack()) {
                        service.pause();
                        service.seek(0);
                    } else {
                        service.position = service.nextPosition;
                        service.prepareNextImpl();
                        service.notifyChange(META_CHANGED);
                    }
                    break;

                case TRACK_ENDED:
                    if (service.getRepeatMode() == REPEAT_MODE_NONE && service.isLastTrack()) {
                        service.notifyChange(PLAY_STATE_CHANGED);
                        service.seek(0);
                    } else {
                        service.playNextSong(false);
                    }
                    sendEmptyMessage(RELEASE_WAKELOCK);
                    break;

                case RELEASE_WAKELOCK:
                    service.releaseWakeLock();
                    break;

                case PLAY_SONG:
                    service.playSongAtImpl(msg.arg1);
                    break;

                case SET_POSITION:
                    service.openTrackAndPrepareNextAt(msg.arg1);
                    service.notifyChange(PLAY_STATE_CHANGED);
                    break;

                case PREPARE_NEXT:
                    service.prepareNextImpl();
                    break;

                case RESTORE_QUEUES:
                    service.restoreQueuesAndPositionIfNecessary();
                    break;

                case FOCUS_CHANGE:
                    switch (msg.arg1) {
                        case AudioManager.AUDIOFOCUS_GAIN:
                            if (!service.isPlaying() && service.pausedByTransientLossOfFocus) {
                                service.play();
                                service.pausedByTransientLossOfFocus = false;
                            }
                            removeMessages(DUCK);
                            sendEmptyMessage(UNDUCK);
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS:
                            // Lost focus for an unbounded amount of time: stop playback and release media playback
                            service.pause();
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            // Lost focus for a short time, but we have to stop
                            // playback. We don't release the media playback because playback
                            // is likely to resume
                            boolean wasPlaying = service.isPlaying();
                            service.pause();
                            service.pausedByTransientLossOfFocus = wasPlaying;
                            break;

                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            // Lost focus for a short time, but it's ok to keep playing
                            // at an attenuated level
                            removeMessages(UNDUCK);
                            sendEmptyMessage(DUCK);
                            break;
                    }
                    break;
            }
        }
    }

    private static class SongPlayCountHelper {
        public static final String TAG = SongPlayCountHelper.class.getSimpleName();

        private StopWatch stopWatch = new StopWatch();
        private Song song = Song.Companion.getEmptySong();

        public Song getSong() {
            return song;
        }

        boolean shouldBumpPlayCount() {
            return song.getDuration() * 0.5d < stopWatch.getElapsedTime();
        }

        void notifySongChanged(Song song) {
            synchronized (this) {
                stopWatch.reset();
                this.song = song;
            }
        }

        void notifyPlayStateChanged(boolean isPlaying) {
            synchronized (this) {
                if (isPlaying) {
                    stopWatch.start();
                } else {
                    stopWatch.pause();
                }
            }
        }
    }

    public class MusicBinder extends Binder {
        @NonNull
        public MusicService getService() {
            return MusicService.this;
        }
    }

    private class MediaStoreObserver extends ContentObserver implements Runnable {
        // milliseconds to delay before calling refresh to aggregate events
        private static final long REFRESH_DELAY = 500;
        private Handler mHandler;

        MediaStoreObserver(Handler handler) {
            super(handler);
            mHandler = handler;
        }

        @Override
        public void onChange(boolean selfChange) {
            // if a change is detected, remove any scheduled callback
            // then post a new one. This is intended to prevent closely
            // spaced events from generating multiple refresh calls
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, REFRESH_DELAY);
        }

        @Override
        public void run() {
            // actually call refresh when the delayed callback fires
            // do not send a sticky broadcast here
            handleAndSendChangeInternal(MEDIA_STORE_CHANGED);
        }
    }

    private class ThrottledSeekHandler implements Runnable {
        // milliseconds to throttle before calling run() to aggregate events
        private static final long THROTTLE = 500;
        private Handler mHandler;

        ThrottledSeekHandler(Handler handler) {
            mHandler = handler;
        }

        void notifySeek() {
            mHandler.removeCallbacks(this);
            mHandler.postDelayed(this, THROTTLE);
        }

        @Override
        public void run() {
            savePositionInTrack();
            sendPublicIntent(PLAY_STATE_CHANGED); // for musixmatch synced lyrics
        }
    }
}