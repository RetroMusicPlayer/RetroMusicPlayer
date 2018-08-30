package code.name.monkey.retromusic.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.WeakHashMap;

import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.loaders.SongLoader;
import code.name.monkey.retromusic.model.Song;
import code.name.monkey.retromusic.service.MusicService;
import code.name.monkey.retromusic.util.PreferenceUtil;
import io.reactivex.schedulers.Schedulers;


public class MusicPlayerRemote {
    public static final String TAG = MusicPlayerRemote.class.getSimpleName();
    private static final WeakHashMap<Context, ServiceBinder> mConnectionMap = new WeakHashMap<>();
    @Nullable
    public static MusicService musicService;
    
    public static ServiceToken bindToService(@NonNull final Context context,
                                             final ServiceConnection callback) {
        Activity realActivity = ((Activity) context).getParent();
        if (realActivity == null) {
            realActivity = (Activity) context;
        }

        final ContextWrapper contextWrapper = new ContextWrapper(realActivity);
        contextWrapper.startService(new Intent(contextWrapper, MusicService.class));

        final ServiceBinder binder = new ServiceBinder(callback);

        if (contextWrapper.bindService(new Intent().setClass(contextWrapper, MusicService.class), binder, Context.BIND_AUTO_CREATE)) {
            mConnectionMap.put(contextWrapper, binder);
            return new ServiceToken(contextWrapper);
        }
        return null;
    }

    public static void unbindFromService(@Nullable final ServiceToken token) {
        if (token == null) {
            return;
        }
        final ContextWrapper mContextWrapper = token.mWrappedContext;
        final ServiceBinder mBinder = mConnectionMap.remove(mContextWrapper);
        if (mBinder == null) {
            return;
        }
        mContextWrapper.unbindService(mBinder);
        if (mConnectionMap.isEmpty()) {
            musicService = null;
        }
    }

    @Nullable
    private static String getFilePathFromUri(Context context, Uri uri) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * Async
     */
    public static void playSongAt(final int position) {
        if (musicService != null) {
            musicService.playSongAt(position);
        }
    }

    public static void pauseSong() {
        if (musicService != null) {
            musicService.pause();
        }
    }

    /**
     * Async
     */
    public static void playNextSong() {
        if (musicService != null) {
            musicService.playNextSong(true);
        }
    }

    /**
     * Async
     */
    public static void playPreviousSong() {
        if (musicService != null) {
            musicService.playPreviousSong(true);
        }
    }

    /**
     * Async
     */
    public static void back() {
        if (musicService != null) {
            musicService.back(true);
        }
    }

    public static boolean isPlaying() {
        return musicService != null && musicService.isPlaying();
    }

    public static void resumePlaying() {
        if (musicService != null) {
            musicService.play();
        }
    }

    /**
     * Async
     */
    public static void openQueue(final ArrayList<Song> queue, final int startPosition, final boolean startPlaying) {
        if (!tryToHandleOpenPlayingQueue(queue, startPosition, startPlaying) && musicService != null) {
            musicService.openQueue(queue, startPosition, startPlaying);
            if (PreferenceUtil.getInstance(musicService).isShuffleModeOn())
                setShuffleMode(MusicService.SHUFFLE_MODE_NONE);
        }
    }

    /**
     * Async
     */
    public static void openAndShuffleQueue(final ArrayList<Song> queue, boolean startPlaying) {
        int startPosition = 0;
        if (!queue.isEmpty()) {
            startPosition = new Random().nextInt(queue.size());
        }

        if (!tryToHandleOpenPlayingQueue(queue, startPosition, startPlaying) && musicService != null) {
            openQueue(queue, startPosition, startPlaying);
            setShuffleMode(MusicService.SHUFFLE_MODE_SHUFFLE);
        }
    }

    private static boolean tryToHandleOpenPlayingQueue(final ArrayList<Song> queue, final int startPosition, final boolean startPlaying) {
        if (getPlayingQueue() == queue) {
            if (startPlaying) {
                playSongAt(startPosition);
            } else {
                setPosition(startPosition);
            }
            return true;
        }
        return false;
    }

    public static Song getCurrentSong() {
        if (musicService != null) {
            return musicService.getCurrentSong();
        }
        return Song.EMPTY_SONG;
    }

    public static int getPosition() {
        if (musicService != null) {
            return musicService.getPosition();
        }
        return -1;
    }

    /**
     * Async
     */
    public static void setPosition(final int position) {
        if (musicService != null) {
            musicService.setPosition(position);
        }
    }

    public static ArrayList<Song> getPlayingQueue() {
        if (musicService != null) {
            return musicService.getPlayingQueue();
        }
        return new ArrayList<>();
    }

    public static int getSongProgressMillis() {
        if (musicService != null) {
            return musicService.getSongProgressMillis();
        }
        return -1;
    }

    public static int getSongDurationMillis() {
        if (musicService != null) {
            return musicService.getSongDurationMillis();
        }
        return -1;
    }

    public static long getQueueDurationMillis(int position) {
        if (musicService != null) {
            return musicService.getQueueDurationMillis(position);
        }
        return -1;
    }

    public static int seekTo(int millis) {
        if (musicService != null) {
            return musicService.seek(millis);
        }
        return -1;
    }

    public static int getRepeatMode() {
        if (musicService != null) {
            return musicService.getRepeatMode();
        }
        return MusicService.REPEAT_MODE_NONE;
    }

    public static int getShuffleMode() {
        if (musicService != null) {
            return musicService.getShuffleMode();
        }
        return MusicService.SHUFFLE_MODE_NONE;
    }

    public static boolean cycleRepeatMode() {
        if (musicService != null) {
            musicService.cycleRepeatMode();
            return true;
        }
        return false;
    }

    public static boolean toggleShuffleMode() {
        if (musicService != null) {
            musicService.toggleShuffle();
            return true;
        }
        return false;
    }

    public static boolean setShuffleMode(final int shuffleMode) {
        if (musicService != null) {
            musicService.setShuffleMode(shuffleMode);
            return true;
        }
        return false;
    }

    public static boolean playNext(Song song) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                musicService.addSong(getPosition() + 1, song);
            } else {
                ArrayList<Song> queue = new ArrayList<>();
                queue.add(song);
                openQueue(queue, 0, false);
            }
            Toast.makeText(musicService, musicService.getResources().getString(R.string.added_title_to_playing_queue), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean playNext(@NonNull ArrayList<Song> songs) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                musicService.addSongs(getPosition() + 1, songs);
            } else {
                openQueue(songs, 0, false);
            }
            final String toast = songs.size() == 1 ? musicService.getResources().getString(R.string.added_title_to_playing_queue) : musicService.getResources().getString(R.string.added_x_titles_to_playing_queue, songs.size());
            Toast.makeText(musicService, toast, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean enqueue(Song song) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                musicService.addSong(song);
            } else {
                ArrayList<Song> queue = new ArrayList<>();
                queue.add(song);
                openQueue(queue, 0, false);
            }
            Toast.makeText(musicService, musicService.getResources().getString(R.string.added_title_to_playing_queue), Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean enqueue(@NonNull ArrayList<Song> songs) {
        if (musicService != null) {
            if (getPlayingQueue().size() > 0) {
                musicService.addSongs(songs);
            } else {
                openQueue(songs, 0, false);
            }
            final String toast = songs.size() == 1 ? musicService.getResources().getString(R.string.added_title_to_playing_queue) : musicService.getResources().getString(R.string.added_x_titles_to_playing_queue, songs.size());
            Toast.makeText(musicService, toast, Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public static boolean removeFromQueue(@NonNull Song song) {
        if (musicService != null) {
            musicService.removeSong(song);
            return true;
        }
        return false;
    }

    public static boolean removeFromQueue(int position) {
        if (musicService != null && position >= 0 && position < getPlayingQueue().size()) {
            musicService.removeSong(position);
            return true;
        }
        return false;
    }

    public static boolean moveSong(int from, int to) {
        if (musicService != null && from >= 0 && to >= 0 && from < getPlayingQueue().size() && to < getPlayingQueue().size()) {
            musicService.moveSong(from, to);
            return true;
        }
        return false;
    }

    public static boolean clearQueue() {
        if (musicService != null) {
            musicService.clearQueue();
            return true;
        }
        return false;
    }

    public static int getAudioSessionId() {
        if (musicService != null) {
            return musicService.getAudioSessionId();
        }
        return -1;
    }

    public static void playFromUri(Uri uri) {
        if (musicService != null) {
            ArrayList<Song> songs = null;
            if (uri.getScheme() != null && uri.getAuthority() != null) {
                if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
                    String songId = null;
                    if (uri.getAuthority().equals("com.android.providers.media.documents")) {
                        songId = getSongIdFromMediaProvider(uri);
                    } else if (uri.getAuthority().equals("media")) {
                        songId = uri.getLastPathSegment();
                    }
                    if (songId != null) {
                       /* songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                                musicService,
                                MediaStore.Audio.AudioColumns._ID + "=?",
                                new String[]{songId}
                        ));*/
                        songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                                musicService,
                                MediaStore.Audio.AudioColumns._ID + "=?",
                                new String[]{songId}))
                                .subscribeOn(Schedulers.io()).blockingFirst();
                    }
                }
            }
            if (songs == null) {
                File songFile = null;
                if (uri.getAuthority() != null && uri.getAuthority().equals("com.android.externalstorage.documents")) {
                    songFile = new File(Environment.getExternalStorageDirectory(), uri.getPath().split(":", 2)[1]);
                }
                if (songFile == null) {
                    String path = getFilePathFromUri(musicService, uri);
                    if (path != null)
                        songFile = new File(path);
                }
                if (songFile == null && uri.getPath() != null) {
                    songFile = new File(uri.getPath());
                }
                if (songFile != null) {
                    songs = SongLoader.getSongs(SongLoader.makeSongCursor(
                            musicService,
                            MediaStore.Audio.AudioColumns.DATA + "=?",
                            new String[]{songFile.getAbsolutePath()}
                    )).blockingFirst();
                }
            }
            if (songs != null && !songs.isEmpty()) {
                openQueue(songs, 0, true);
            } else {
                //TODO the file is not listed in the media store
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getSongIdFromMediaProvider(Uri uri) {
        return DocumentsContract.getDocumentId(uri).split(":")[1];
    }

    public static boolean isServiceConnected() {
        return musicService != null;
    }

    @interface PlaybackLocation {
        int REMOTE = 0;
        int LOCAL = 1;
    }

    public static final class ServiceBinder implements ServiceConnection {
        private final ServiceConnection mCallback;

        ServiceBinder(final ServiceConnection callback) {
            mCallback = callback;
        }

        @Override
        public void onServiceConnected(final ComponentName className, final IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            if (mCallback != null) {
                mCallback.onServiceConnected(className, service);
            }
        }

        @Override
        public void onServiceDisconnected(final ComponentName className) {
            if (mCallback != null) {
                mCallback.onServiceDisconnected(className);
            }
            musicService = null;
        }
    }

    public static final class ServiceToken {
        ContextWrapper mWrappedContext;

        ServiceToken(final ContextWrapper context) {
            mWrappedContext = context;
        }
    }
}
