package code.name.monkey.retromusic.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.Service
import android.content.*
import android.database.ContentObserver
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.media.AudioManager
import android.media.audiofx.AudioEffect
import android.media.session.MediaSession
import android.os.*
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import code.name.monkey.retromusic.Constants.ACTION_PAUSE
import code.name.monkey.retromusic.Constants.ACTION_PLAY
import code.name.monkey.retromusic.Constants.ACTION_PLAY_PLAYLIST
import code.name.monkey.retromusic.Constants.ACTION_QUIT
import code.name.monkey.retromusic.Constants.ACTION_SKIP
import code.name.monkey.retromusic.Constants.ACTION_STOP
import code.name.monkey.retromusic.Constants.ACTION_TOGGLE_PAUSE
import code.name.monkey.retromusic.Constants.APP_WIDGET_UPDATE
import code.name.monkey.retromusic.Constants.EXTRA_APP_WIDGET_NAME
import code.name.monkey.retromusic.Constants.INTENT_EXTRA_PLAYLIST
import code.name.monkey.retromusic.Constants.INTENT_EXTRA_SHUFFLE_MODE
import code.name.monkey.retromusic.Constants.MEDIA_STORE_CHANGED
import code.name.monkey.retromusic.Constants.META_CHANGED
import code.name.monkey.retromusic.Constants.MUSIC_PACKAGE_NAME
import code.name.monkey.retromusic.Constants.PLAY_STATE_CHANGED
import code.name.monkey.retromusic.Constants.QUEUE_CHANGED
import code.name.monkey.retromusic.Constants.REPEAT_MODE_CHANGED
import code.name.monkey.retromusic.Constants.RETRO_MUSIC_PACKAGE_NAME
import code.name.monkey.retromusic.Constants.SHUFFLE_MODE_CHANGED
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.BlurTransformation
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote.setShuffleMode
import code.name.monkey.retromusic.helper.ShuffleHelper
import code.name.monkey.retromusic.helper.StopWatch
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.AbsCustomPlaylist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.HistoryStore
import code.name.monkey.retromusic.providers.MusicPlaybackQueueStore
import code.name.monkey.retromusic.providers.SongPlayCountStore
import code.name.monkey.retromusic.service.notification.PlayingNotification
import code.name.monkey.retromusic.service.notification.PlayingNotificationImpl24
import code.name.monkey.retromusic.service.notification.PlayingNotificationOreo
import code.name.monkey.retromusic.service.playback.Playback
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.google.android.gms.cast.framework.media.MediaIntentReceiver.ACTION_REWIND
import java.lang.ref.WeakReference
import java.util.*

/**
 * @author Karim Abou Zeid (kabouzeid), Andrew Neal
 */
class MusicService : Service(), SharedPreferences.OnSharedPreferenceChangeListener, Playback.PlaybackCallbacks {
    private val musicBind = MusicBinder()
    private var playback: Playback? = null
    var playingQueue = ArrayList<Song>()
        private set
    private var originalPlayingQueue = ArrayList<Song>()
    var position = -1
        set(value) {
            playerHandler!!.removeMessages(SET_POSITION)
            playerHandler!!.obtainMessage(SET_POSITION, value, 0).sendToTarget()
        }
    private var nextPosition = -1
    var shuffleMode: Int = 0
        set(value) {
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putInt(SAVED_SHUFFLE_MODE, value)
                    .apply()
            when (value) {
                SHUFFLE_MODE_SHUFFLE -> {
                    field = value
                    ShuffleHelper.makeShuffleList(this.playingQueue, position)
                    position = 0
                }
                SHUFFLE_MODE_NONE -> {
                    field = value
                    val currentSongId = currentSong.id
                    playingQueue = ArrayList(originalPlayingQueue)
                    var newPosition = 0
                    for (song in playingQueue) {
                        if (song.id == currentSongId) {
                            newPosition = playingQueue.indexOf(song)
                        }
                    }
                    position = newPosition
                }
            }
            handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED)
            notifyChange(QUEUE_CHANGED)
        }
    var repeatMode: Int = 0
        set(value) {
            when (value) {
                REPEAT_MODE_NONE, REPEAT_MODE_ALL, REPEAT_MODE_THIS -> {
                    field = value
                    PreferenceManager.getDefaultSharedPreferences(this).edit()
                            .putInt(SAVED_REPEAT_MODE, repeatMode)
                            .apply()
                    prepareNext()
                    handleAndSendChangeInternal(REPEAT_MODE_CHANGED)
                }
            }
        }
    private var queuesRestored: Boolean = false
    private var pausedByTransientLossOfFocus: Boolean = false
    private val becomingNoisyReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != null && intent.action == AudioManager.ACTION_AUDIO_BECOMING_NOISY) {
                pause()
            }
        }
    }
    private var playingNotification: PlayingNotification? = null
    private var audioManager: AudioManager? = null
    var mediaSession: MediaSessionCompat? = null
        private set
    private var wakeLock: PowerManager.WakeLock? = null
    private var playerHandler: PlaybackHandler? = null
    private val audioFocusListener = AudioManager.OnAudioFocusChangeListener { focusChange -> playerHandler!!.obtainMessage(FOCUS_CHANGE, focusChange, 0).sendToTarget() }
    private var queueSaveHandler: QueueSaveHandler? = null
    private var musicPlayerHandlerThread: HandlerThread? = null
    private var queueSaveHandlerThread: HandlerThread? = null
    private val songPlayCountHelper = SongPlayCountHelper()
    private var throttledSeekHandler: ThrottledSeekHandler? = null
    private var becomingNoisyReceiverRegistered: Boolean = false
    private val becomingNoisyReceiverIntentFilter = IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY)
    private var mediaStoreObserver: ContentObserver? = null
    private var notHandledMetaChangedForCurrentTrack: Boolean = false
    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            when (state) {
                TelephonyManager.CALL_STATE_IDLE ->
                    //Not in call: Play music
                    play()
                TelephonyManager.CALL_STATE_RINGING, TelephonyManager.CALL_STATE_OFFHOOK ->
                    //A call is dialing, active or on hold
                    pause()
            }
            super.onCallStateChanged(state, incomingNumber)
        }
    }
    private var isServiceBound: Boolean = false
    private var uiThreadHandler: Handler? = null
    private val widgetIntentReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val command = intent.getStringExtra(EXTRA_APP_WIDGET_NAME)
        }
    }
    private val headsetReceiverIntentFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
    private var headsetReceiverRegistered = false
    private val headsetReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    Intent.ACTION_HEADSET_PLUG -> {
                        val state = intent.getIntExtra("state", -1)
                        when (state) {
                            0 -> {
                                Log.d(TAG, "Headset unplugged")
                                pause()
                            }
                            1 -> {
                                Log.d(TAG, "Headset plugged")
                                play()
                            }
                        }
                    }
                }
            }
        }
    }

    val isPlaying: Boolean
        get() = playback != null && playback!!.isPlaying

    val currentSong: Song
        get() = getSongAt(position)

    private val isLastTrack: Boolean
        get() = position == playingQueue.size - 1

    val songProgressMillis: Int
        get() = playback!!.position()

    val songDurationMillis: Int
        get() = playback!!.duration()

    val audioSessionId: Int
        get() = playback!!.audioSessionId


    override fun onCreate() {
        super.onCreate()

        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)

        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name)
        wakeLock!!.setReferenceCounted(false)

        musicPlayerHandlerThread = HandlerThread("PlaybackHandler")
        musicPlayerHandlerThread!!.start()
        playerHandler = PlaybackHandler(this, musicPlayerHandlerThread!!.looper)

        playback = MultiPlayer(this)
        playback!!.setCallbacks(this)

        setupMediaSession()

        // queue saving needs to run on a separate thread so that it doesn't block the playback handler events
        queueSaveHandlerThread = HandlerThread("QueueSaveHandler", Process.THREAD_PRIORITY_BACKGROUND)
        queueSaveHandlerThread!!.start()
        queueSaveHandler = QueueSaveHandler(this, queueSaveHandlerThread!!.looper)

        uiThreadHandler = Handler()

        registerReceiver(widgetIntentReceiver, IntentFilter(APP_WIDGET_UPDATE))

        initNotification()

        mediaStoreObserver = MediaStoreObserver(playerHandler!!)
        throttledSeekHandler = ThrottledSeekHandler(playerHandler!!)
        contentResolver.registerContentObserver(
                MediaStore.Audio.Media.INTERNAL_CONTENT_URI, true, mediaStoreObserver!!)
        contentResolver.registerContentObserver(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, true, mediaStoreObserver!!)

        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this)

        restoreState()

        mediaSession!!.isActive = true

        sendBroadcast(Intent("code.name.monkey.retromusic.RETRO_MUSIC_SERVICE_CREATED"))

        registerHeadsetEvents()


    }

    private fun getAudioManager(): AudioManager? {
        if (audioManager == null) {
            audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        }
        return audioManager
    }

    @SuppressLint("WrongConstant")
    private fun setupMediaSession() {
        val mediaButtonReceiverComponentName = ComponentName(applicationContext, MediaButtonIntentReceiver::class.java)

        val mediaButtonIntent = Intent(Intent.ACTION_MEDIA_BUTTON)
        mediaButtonIntent.component = mediaButtonReceiverComponentName


        val mediaButtonReceiverPendingIntent = PendingIntent.getBroadcast(applicationContext, 0, mediaButtonIntent, 0)

        mediaSession = MediaSessionCompat(this, "RetroMusicPlayer", mediaButtonReceiverComponentName, mediaButtonReceiverPendingIntent)
        mediaSession!!.setCallback(object : MediaSessionCompat.Callback() {
            override fun onPlay() {
                play()
            }

            override fun onPause() {
                pause()
            }

            override fun onSkipToNext() {
                playNextSong(true)
            }

            override fun onSkipToPrevious() {
                back(true)
            }

            override fun onStop() {
                quit()
            }

            override fun onSeekTo(pos: Long) {
                seek(pos.toInt())
            }

            override fun onMediaButtonEvent(mediaButtonEvent: Intent): Boolean {
                return MediaButtonIntentReceiver.handleIntent(this@MusicService, mediaButtonEvent)
            }
        })

        mediaSession!!.setFlags(MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS or MediaSession.FLAG_HANDLES_MEDIA_BUTTONS)

        mediaSession!!.setMediaButtonReceiver(mediaButtonReceiverPendingIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (intent.action != null) {
                restoreQueuesAndPositionIfNecessary()
                val action = intent.action
                when (action) {
                    ACTION_TOGGLE_PAUSE -> if (isPlaying) {
                        pause()
                    } else {
                        play()
                    }
                    ACTION_PAUSE -> pause()
                    ACTION_PLAY -> play()
                    ACTION_PLAY_PLAYLIST -> {
                        val playlist = intent.getParcelableExtra<Playlist>(INTENT_EXTRA_PLAYLIST)
                        val shuffleMode = intent.getIntExtra(INTENT_EXTRA_SHUFFLE_MODE, shuffleMode)
                        if (playlist != null) {
                            val playlistSongs: ArrayList<Song>
                            if (playlist is AbsCustomPlaylist) {
                                playlistSongs = playlist.getSongs(applicationContext).blockingFirst()
                            } else {

                                playlistSongs = PlaylistSongsLoader.getPlaylistSongList(applicationContext, playlist.id).blockingFirst()
                            }
                            if (!playlistSongs.isEmpty()) {
                                if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                                    val startPosition: Int = Random().nextInt(playlistSongs.size)
                                    openQueue(playlistSongs, startPosition, true)
                                    setShuffleMode(shuffleMode)
                                } else {
                                    openQueue(playlistSongs, 0, true)
                                }
                            } else {
                                Toast.makeText(applicationContext, R.string.playlist_is_empty, Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(applicationContext, R.string.playlist_is_empty, Toast.LENGTH_LONG).show()
                        }
                    }
                    ACTION_REWIND -> back(true)
                    ACTION_SKIP -> playNextSong(true)
                    ACTION_STOP, ACTION_QUIT -> return quit()
                }
            }
        }

        return Service.START_STICKY
    }


    override fun onDestroy() {
        unregisterReceiver(widgetIntentReceiver)
        if (becomingNoisyReceiverRegistered) {
            unregisterReceiver(becomingNoisyReceiver)
            becomingNoisyReceiverRegistered = false
        }
        if (headsetReceiverRegistered) {
            unregisterReceiver(headsetReceiver)
            headsetReceiverRegistered = false
        }
        mediaSession!!.isActive = false
        quit()
        releaseResources()
        contentResolver.unregisterContentObserver(mediaStoreObserver!!)
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this)
        wakeLock!!.release()

        sendBroadcast(Intent("code.name.monkey.retromusic.RETRO_MUSIC_MUSIC_SERVICE_DESTROYED"))
    }

    override fun onBind(intent: Intent): IBinder? {
        isServiceBound = true
        return musicBind
    }

    override fun onRebind(intent: Intent) {
        isServiceBound = true
    }

    override fun onUnbind(intent: Intent): Boolean {
        isServiceBound = false
        if (!isPlaying) {
            stopSelf()
        }
        return true
    }

    private fun saveQueuesImpl() {
        MusicPlaybackQueueStore.getInstance(this).saveQueues(playingQueue, originalPlayingQueue)
    }

    private fun savePosition() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(SAVED_POSITION, position).apply()
    }

    private fun savePositionInTrack() {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(SAVED_POSITION_IN_TRACK, songProgressMillis).apply()
    }

    private fun saveState() {
        saveQueues()
        savePosition()
        savePositionInTrack()
    }

    private fun saveQueues() {
        queueSaveHandler!!.removeMessages(SAVE_QUEUES)
        queueSaveHandler!!.sendEmptyMessage(SAVE_QUEUES)
    }

    private fun restoreState() {
        shuffleMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_SHUFFLE_MODE, 0)
        repeatMode = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_REPEAT_MODE, 0)
        handleAndSendChangeInternal(SHUFFLE_MODE_CHANGED)
        handleAndSendChangeInternal(REPEAT_MODE_CHANGED)

        playerHandler!!.removeMessages(RESTORE_QUEUES)
        playerHandler!!.sendEmptyMessage(RESTORE_QUEUES)
    }

    @Synchronized
    private fun restoreQueuesAndPositionIfNecessary() {
        if (!queuesRestored && playingQueue.isEmpty()) {
            val restoredQueue = MusicPlaybackQueueStore.getInstance(this).savedPlayingQueue
                    .blockingFirst()

            val restoredOriginalQueue = MusicPlaybackQueueStore.getInstance(this).savedOriginalPlayingQueue
                    .blockingFirst()

            val restoredPosition = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_POSITION, -1)
            val restoredPositionInTrack = PreferenceManager.getDefaultSharedPreferences(this).getInt(SAVED_POSITION_IN_TRACK, -1)

            if (restoredQueue.size > 0 && restoredQueue.size == restoredOriginalQueue.size && restoredPosition != -1) {
                this.originalPlayingQueue = restoredOriginalQueue
                this.playingQueue = restoredQueue

                position = restoredPosition
                openCurrent()
                prepareNext()

                if (restoredPositionInTrack > 0) seek(restoredPositionInTrack)

                notHandledMetaChangedForCurrentTrack = true
                sendChangeInternal(META_CHANGED)
                sendChangeInternal(QUEUE_CHANGED)
            }
        }
        queuesRestored = true
    }

    @SuppressLint("WrongConstant")
    private fun quit(): Int {
        pause()
        playingNotification!!.stop()

        if (isServiceBound) {
            return Service.START_STICKY
        } else {
            closeAudioEffectSession()
            getAudioManager()!!.abandonAudioFocus(audioFocusListener)
            stopSelf()
            return Service.START_NOT_STICKY
        }
    }

    private fun releaseResources() {
        playerHandler!!.removeCallbacksAndMessages(null)
        musicPlayerHandlerThread!!.quitSafely()
        queueSaveHandler!!.removeCallbacksAndMessages(null)
        queueSaveHandlerThread!!.quitSafely()
        playback!!.release()
        playback = null
        mediaSession!!.release()
    }

    fun playNextSong(force: Boolean) {
        playSongAt(getNextPosition(force))
    }

    private fun openTrackAndPrepareNextAt(position: Int): Boolean {
        synchronized(this) {
            this.position = position
            val prepared = openCurrent()
            if (prepared) prepareNextImpl()
            notifyChange(META_CHANGED)
            notHandledMetaChangedForCurrentTrack = false
            return prepared
        }
    }

    private fun openCurrent(): Boolean {
        synchronized(this) {
            try {
                return playback!!.setDataSource(getTrackUri(currentSong))
            } catch (e: Exception) {
                return false
            }

        }
    }

    private fun prepareNext() {
        playerHandler!!.removeMessages(PREPARE_NEXT)
        playerHandler!!.obtainMessage(PREPARE_NEXT).sendToTarget()
    }

    private fun prepareNextImpl(): Boolean {
        synchronized(this) {
            return try {
                val nextPosition = getNextPosition(false)
                playback!!.setNextDataSource(getTrackUri(getSongAt(nextPosition)))
                this.nextPosition = nextPosition
                true
            } catch (e: Exception) {
                false
            }

        }
    }

    private fun closeAudioEffectSession() {
        val audioEffectsIntent = Intent(AudioEffect.ACTION_CLOSE_AUDIO_EFFECT_CONTROL_SESSION)
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, playback!!.audioSessionId)
        audioEffectsIntent.putExtra(AudioEffect.EXTRA_PACKAGE_NAME, packageName)
        sendBroadcast(audioEffectsIntent)
    }

    private fun requestFocus(): Boolean {
        return getAudioManager()!!.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
    }

    fun initNotification() {
        playingNotification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !PreferenceUtil.getInstance().classicNotification()) {
            PlayingNotificationImpl24()
        } else {
            PlayingNotificationOreo()
        }
        playingNotification!!.init(this)
    }

    fun updateNotification() {
        if (playingNotification != null && currentSong.id != -1) {
            playingNotification!!.update()
        }
    }

    private fun updateMediaSessionPlaybackState() {
        mediaSession!!.setPlaybackState(
                PlaybackStateCompat.Builder()
                        .setActions(MEDIA_SESSION_ACTIONS)
                        .setState(if (isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED,
                                position.toLong(), 1f)
                        .build())
    }

    private fun updateMediaSessionMetaData() {
        val song = currentSong

        if (song.id == -1) {
            mediaSession!!.setMetadata(null)
            return
        }

        val metaData = MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.artistName)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, song.artistName)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, song.albumName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.title)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, song.duration)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, (position + 1).toLong())
                .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, song.year.toLong())
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            metaData.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, playingQueue.size.toLong())
        }

        if (PreferenceUtil.getInstance().albumArtOnLockscreen()) {
            val screenSize = RetroUtil.getScreenSize(this@MusicService)
            val request = SongGlideRequest.Builder.from(Glide.with(this@MusicService), song)
                    .checkIgnoreMediaStore(this@MusicService)
                    .asBitmap().build()
            if (PreferenceUtil.getInstance().blurredAlbumArt()) {
                request.transform(BlurTransformation.Builder(this@MusicService).build())
            }
            runOnUiThread(Runnable {
                request.into(object : SimpleTarget<Bitmap>(screenSize.x, screenSize.y) {
                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        super.onLoadFailed(e, errorDrawable)
                        mediaSession!!.setMetadata(metaData.build())
                    }

                    override fun onResourceReady(resource: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        metaData.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, copy(resource))
                        mediaSession!!.setMetadata(metaData.build())
                    }
                })
            })
        } else {
            mediaSession!!.setMetadata(metaData.build())
        }
    }

    fun runOnUiThread(runnable: Runnable) {
        uiThreadHandler!!.post(runnable)
    }

    private fun getSongAt(position: Int): Song {
        return if (position >= 0 && position < playingQueue.size) {
            playingQueue[position]
        } else {
            Song.EMPTY_SONG
        }
    }

    private fun getNextPosition(force: Boolean): Int {
        var position = position + 1
        when (repeatMode) {
            REPEAT_MODE_ALL -> if (isLastTrack) {
                position = 0
            }
            REPEAT_MODE_THIS -> if (force) {
                if (isLastTrack) {
                    position = 0
                }
            } else {
                position -= 1
            }
            REPEAT_MODE_NONE -> if (isLastTrack) {
                position -= 1
            }
            else -> if (isLastTrack) {
                position -= 1
            }
        }
        return position
    }


    fun openQueue(playingQueue: ArrayList<Song>?, startPosition: Int, startPlaying: Boolean) {
        if (playingQueue != null && !playingQueue.isEmpty() && startPosition >= 0 && startPosition < playingQueue.size) {
            // it is important to copy the playing queue here first as we might add/remove songs later
            originalPlayingQueue = ArrayList(playingQueue)
            this.playingQueue = ArrayList(originalPlayingQueue)

            var position = startPosition
            if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                ShuffleHelper.makeShuffleList(this.playingQueue, startPosition)
                position = 0
            }
            if (startPlaying) {
                playSongAt(position)
            } else {
                this.position = position
            }
            notifyChange(QUEUE_CHANGED)
        }
    }

    fun addSong(position: Int, song: Song) {
        playingQueue.add(position, song)
        originalPlayingQueue.add(position, song)
        notifyChange(QUEUE_CHANGED)
    }

    fun addSong(song: Song) {
        playingQueue.add(song)
        originalPlayingQueue.add(song)
        notifyChange(QUEUE_CHANGED)
    }

    fun addSongs(position: Int, songs: List<Song>) {
        playingQueue.addAll(position, songs)
        originalPlayingQueue.addAll(position, songs)
        notifyChange(QUEUE_CHANGED)
    }

    fun addSongs(songs: List<Song>) {
        playingQueue.addAll(songs)
        originalPlayingQueue.addAll(songs)
        notifyChange(QUEUE_CHANGED)
    }

    fun removeSong(position: Int) {
        if (shuffleMode == SHUFFLE_MODE_NONE) {
            playingQueue.removeAt(position)
            originalPlayingQueue.removeAt(position)
        } else {
            originalPlayingQueue.remove(playingQueue.removeAt(position))
        }

        rePosition(position)

        notifyChange(QUEUE_CHANGED)
    }

    fun removeSong(song: Song) {
        for (i in playingQueue.indices) {
            if (playingQueue[i].id == song.id) {
                playingQueue.removeAt(i)
                rePosition(i)
            }
        }
        for (i in originalPlayingQueue.indices) {
            if (originalPlayingQueue[i].id == song.id) {
                originalPlayingQueue.removeAt(i)
            }
        }
        notifyChange(QUEUE_CHANGED)
    }

    private fun rePosition(deletedPosition: Int) {
        val currentPosition = position
        if (deletedPosition < currentPosition) {
            position = currentPosition - 1
        } else if (deletedPosition == currentPosition) {
            if (playingQueue.size > deletedPosition) {
                this.position = position
            } else {
                this.position = position - 1
            }
        }
    }

    fun moveSong(from: Int, to: Int) {
        if (from == to) return
        val currentPosition = position
        val songToMove = playingQueue.removeAt(from)
        playingQueue.add(to, songToMove)
        if (shuffleMode == SHUFFLE_MODE_NONE) {
            val tmpSong = originalPlayingQueue.removeAt(from)
            originalPlayingQueue.add(to, tmpSong)
        }
        if (currentPosition in to..(from - 1)) {
            position = currentPosition + 1
        } else if (currentPosition in (from + 1)..to) {
            position = currentPosition - 1
        } else if (from == currentPosition) {
            position = to
        }
        notifyChange(QUEUE_CHANGED)
    }

    fun clearQueue() {
        playingQueue.clear()
        originalPlayingQueue.clear()

        position = -1
        notifyChange(QUEUE_CHANGED)
    }

    fun playSongAt(position: Int) {
        // handle this on the handlers thread to avoid blocking the ui thread
        playerHandler!!.removeMessages(PLAY_SONG)
        playerHandler!!.obtainMessage(PLAY_SONG, position, 0).sendToTarget()
    }

    private fun playSongAtImpl(position: Int) {
        if (openTrackAndPrepareNextAt(position)) {
            play()
        } else {
            Toast.makeText(this, resources.getString(R.string.unplayable_file), Toast.LENGTH_SHORT).show()
        }
    }

    fun pause() {
        pausedByTransientLossOfFocus = false
        if (playback!!.isPlaying) {
            playback!!.pause()
            notifyChange(PLAY_STATE_CHANGED)
        }
    }

    fun play() {
        synchronized(this) {
            if (requestFocus()) {
                if (!playback!!.isPlaying) {
                    if (!playback!!.isInitialized) {
                        playSongAt(position)
                    } else {
                        playback!!.start()
                        if (!becomingNoisyReceiverRegistered) {
                            registerReceiver(becomingNoisyReceiver, becomingNoisyReceiverIntentFilter)
                            becomingNoisyReceiverRegistered = true
                        }
                        if (notHandledMetaChangedForCurrentTrack) {
                            handleChangeInternal(META_CHANGED)
                            notHandledMetaChangedForCurrentTrack = false
                        }
                        notifyChange(PLAY_STATE_CHANGED)

                        // fixes a bug where the volume would stay ducked because the AudioManager.AUDIOFOCUS_GAIN event is not sent
                        playerHandler!!.removeMessages(DUCK)
                        playerHandler!!.sendEmptyMessage(UN_DUCK)
                    }
                }
            } else {
                Toast.makeText(this, resources.getString(R.string.audio_focus_denied), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun playSongs(songs: ArrayList<Song>?, shuffleMode: Int) {
        if (songs != null && !songs.isEmpty()) {
            if (shuffleMode == SHUFFLE_MODE_SHUFFLE) {
                var startPosition = 0
                if (!songs.isEmpty()) {
                    startPosition = Random().nextInt(songs.size)
                }
                openQueue(songs, startPosition, false)
                setShuffleMode(shuffleMode)
            } else {
                openQueue(songs, 0, false)
            }
            play()
        } else {
            Toast.makeText(applicationContext, R.string.playlist_is_empty, Toast.LENGTH_LONG).show()
        }
    }

    fun playPreviousSong(force: Boolean) {
        playSongAt(getPreviousPosition(force))
    }

    fun back(force: Boolean) {
        if (songProgressMillis > 2000) {
            seek(0)
        } else {
            playPreviousSong(force)
        }
    }

    private fun getPreviousPosition(force: Boolean): Int {
        var newPosition = position - 1
        when (repeatMode) {
            REPEAT_MODE_ALL -> if (newPosition < 0) {
                newPosition = playingQueue.size - 1
            }
            REPEAT_MODE_THIS -> if (force) {
                if (newPosition < 0) {
                    newPosition = playingQueue.size - 1
                }
            } else {
                newPosition = position
            }
            REPEAT_MODE_NONE -> if (newPosition < 0) {
                newPosition = 0
            }
            else -> if (newPosition < 0) {
                newPosition = 0
            }
        }
        return newPosition
    }

    fun getQueueDurationMillis(position: Int): Long {
        var duration: Long = 0
        for (i in position + 1 until playingQueue.size)
            duration += playingQueue[i].duration
        return duration
    }

    fun seek(millis: Int): Int {
        synchronized(this) {
            try {
                val newPosition = playback!!.seek(millis)
                throttledSeekHandler!!.notifySeek()
                return newPosition
            } catch (e: Exception) {
                return -1
            }

        }
    }

    fun cycleRepeatMode() {
        repeatMode = when (repeatMode) {
            REPEAT_MODE_NONE -> REPEAT_MODE_ALL
            REPEAT_MODE_ALL -> REPEAT_MODE_THIS
            else -> REPEAT_MODE_NONE
        }
    }

    fun toggleShuffle() {
        if (shuffleMode == SHUFFLE_MODE_NONE) {
            setShuffleMode(SHUFFLE_MODE_SHUFFLE)
        } else {
            setShuffleMode(SHUFFLE_MODE_NONE)
        }
    }


    private fun notifyChange(what: String) {
        handleAndSendChangeInternal(what)
        sendPublicIntent(what)
    }

    private fun handleAndSendChangeInternal(what: String) {
        handleChangeInternal(what)
        sendChangeInternal(what)
    }

    // to let other apps know whats playing. i.E. last.fm (scrobbling) or musixmatch
    @SuppressLint("WrongConstant")
    private fun sendPublicIntent(what: String) {
        val intent = Intent(what.replace(RETRO_MUSIC_PACKAGE_NAME, MUSIC_PACKAGE_NAME))

        val song = currentSong

        intent.putExtra("id", song.id)
        intent.putExtra("artist", song.artistName)
        intent.putExtra("album", song.albumName)
        intent.putExtra("track", song.title)
        intent.putExtra("duration", song.duration)
        intent.putExtra("position", songProgressMillis.toLong())
        intent.putExtra("playing", isPlaying)
        intent.putExtra("scrobbling_source", RETRO_MUSIC_PACKAGE_NAME)

        sendStickyBroadcast(intent)

    }

    private fun sendChangeInternal(what: String) {
        sendBroadcast(Intent(what))
    }

    private fun handleChangeInternal(what: String) {
        when (what) {
            PLAY_STATE_CHANGED -> {
                updateNotification()
                updateMediaSessionPlaybackState()
                val isPlaying = isPlaying
                if (!isPlaying && songProgressMillis > 0) {
                    savePositionInTrack()
                }
                songPlayCountHelper.notifyPlayStateChanged(isPlaying)
            }
            META_CHANGED -> {
                updateNotification()
                updateMediaSessionMetaData()
                savePosition()
                savePositionInTrack()
                val currentSong = currentSong
                HistoryStore.getInstance(this).addSongId(currentSong.id.toLong())
                if (songPlayCountHelper.shouldBumpPlayCount()) {
                    SongPlayCountStore.getInstance(this).bumpPlayCount(songPlayCountHelper.song.id.toLong())
                }
                songPlayCountHelper.notifySongChanged(currentSong)
            }
            QUEUE_CHANGED -> {
                updateMediaSessionMetaData() // because playing queue size might have changed
                saveState()
                if (playingQueue.size > 0) {
                    prepareNext()
                } else {
                    playingNotification!!.stop()
                }
            }
        }
    }

    fun releaseWakeLock() {
        if (wakeLock!!.isHeld) {
            wakeLock!!.release()
        }
    }

    private fun acquireWakeLock(milli: Long) {
        wakeLock!!.acquire(milli)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            PreferenceUtil.GAPLESS_PLAYBACK -> if (sharedPreferences.getBoolean(key, false)) {
                prepareNext()
            } else {
                playback!!.setNextDataSource(null)
            }
            PreferenceUtil.ALBUM_ART_ON_LOCKSCREEN, PreferenceUtil.BLURRED_ALBUM_ART -> updateMediaSessionMetaData()
            PreferenceUtil.COLORED_NOTIFICATION, PreferenceUtil.DOMINANT_COLOR -> updateNotification()
            PreferenceUtil.CLASSIC_NOTIFICATION -> {
                initNotification()
                updateNotification()
            }
            PreferenceUtil.TOGGLE_HEADSET -> registerHeadsetEvents()
        }
    }

    private fun registerHeadsetEvents() {
        if (!headsetReceiverRegistered && PreferenceUtil.getInstance().headsetPlugged) {
            registerReceiver(headsetReceiver, headsetReceiverIntentFilter)
            headsetReceiverRegistered = true
        }
    }

    override fun onTrackWentToNext() {
        playerHandler!!.sendEmptyMessage(TRACK_WENT_TO_NEXT)
    }

    override fun onTrackEnded() {
        acquireWakeLock(30000)
        playerHandler!!.sendEmptyMessage(TRACK_ENDED)
    }


    inner class QueueSaveHandler internal constructor(service: MusicService, looper: Looper) : Handler(looper) {
        val mService: WeakReference<MusicService> = WeakReference(service)

        override fun handleMessage(msg: Message) {
            val service = mService.get()
            when (msg.what) {
                SAVE_QUEUES -> service!!.saveQueuesImpl()
            }
        }
    }

    inner class PlaybackHandler internal constructor(service: MusicService, looper: Looper) : Handler(looper) {
        val mService: WeakReference<MusicService> = WeakReference(service)
        var currentDuckVolume = 1.0f

        override fun handleMessage(msg: Message) {
            val service = mService.get() ?: return

            when (msg.what) {
                DUCK -> {
                    if (PreferenceUtil.getInstance().audioDucking()) {
                        currentDuckVolume -= .05f
                        if (currentDuckVolume > .2f) {
                            sendEmptyMessageDelayed(DUCK, 10)
                        } else {
                            currentDuckVolume = .2f
                        }
                    } else {
                        currentDuckVolume = 1f
                    }
                    service.playback!!.setVolume(currentDuckVolume)
                }

                UN_DUCK -> {
                    if (PreferenceUtil.getInstance().audioDucking()) {
                        currentDuckVolume += .03f
                        if (currentDuckVolume < 1f) {
                            sendEmptyMessageDelayed(UN_DUCK, 10)
                        } else {
                            currentDuckVolume = 1f
                        }
                    } else {
                        currentDuckVolume = 1f
                    }
                    service.playback!!.setVolume(currentDuckVolume)
                }

                TRACK_WENT_TO_NEXT -> if (service.repeatMode == REPEAT_MODE_NONE && service.isLastTrack) {
                    service.pause()
                    service.seek(0)
                } else {
                    service.position = service.nextPosition
                    service.prepareNextImpl()
                    service.notifyChange(META_CHANGED)
                }

                TRACK_ENDED -> {
                    if (service.repeatMode == REPEAT_MODE_NONE && service.isLastTrack) {
                        service.notifyChange(PLAY_STATE_CHANGED)
                        service.seek(0)
                    } else {
                        service.playNextSong(false)
                    }
                    sendEmptyMessage(RELEASE_WAKELOCK)
                }

                RELEASE_WAKELOCK -> service.releaseWakeLock()

                PLAY_SONG -> service.playSongAtImpl(msg.arg1)

                SET_POSITION -> {
                    service.openTrackAndPrepareNextAt(msg.arg1)
                    service.notifyChange(PLAY_STATE_CHANGED)
                }

                PREPARE_NEXT -> service.prepareNextImpl()

                RESTORE_QUEUES -> service.restoreQueuesAndPositionIfNecessary()

                FOCUS_CHANGE -> when (msg.arg1) {
                    AudioManager.AUDIOFOCUS_GAIN -> {
                        if (!service.isPlaying && service.pausedByTransientLossOfFocus) {
                            service.play()
                            service.pausedByTransientLossOfFocus = false
                        }
                        removeMessages(DUCK)
                        sendEmptyMessage(UN_DUCK)
                    }

                    AudioManager.AUDIOFOCUS_LOSS ->
                        // Lost focus for an unbounded amount of time: stop playback and release media playback
                        service.pause()

                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                        // Lost focus for a short time, but we have to stop
                        // playback. We don't release the media playback because playback
                        // is likely to resume
                        val wasPlaying = service.isPlaying
                        service.pause()
                        service.pausedByTransientLossOfFocus = wasPlaying
                    }

                    AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                        // Lost focus for a short time, but it's ok to keep playing
                        // at an attenuated level
                        removeMessages(UN_DUCK)
                        sendEmptyMessage(DUCK)
                    }
                }
            }
        }
    }

    inner class SongPlayCountHelper {

        private val stopWatch = StopWatch()
        var song = Song.EMPTY_SONG
            private set

        internal fun shouldBumpPlayCount(): Boolean {
            return song.duration * 0.5 < stopWatch.elapsedTime
        }

        internal fun notifySongChanged(song: Song) {
            synchronized(this) {
                stopWatch.reset()
                this.song = song
            }
        }

        internal fun notifyPlayStateChanged(isPlaying: Boolean) {
            synchronized(this) {
                if (isPlaying) {
                    stopWatch.start()
                } else {
                    stopWatch.pause()
                }
            }
        }
    }

    inner class MusicBinder : Binder() {
        val service: MusicService
            get() = this@MusicService
    }

    inner class MediaStoreObserver internal constructor(private val mHandler: Handler) : ContentObserver(mHandler), Runnable {

        override fun onChange(selfChange: Boolean) {
            // if a change is detected, remove any scheduled callback
            // then post a new one. This is intended to prevent closely
            // spaced events from generating multiple refresh calls
            mHandler.removeCallbacks(this)
            mHandler.postDelayed(this, 500)
        }

        override fun run() {
            // actually call refresh when the delayed callback fires
            // do not send a sticky broadcast here
            handleAndSendChangeInternal(MEDIA_STORE_CHANGED)
        }
    }

    inner class ThrottledSeekHandler internal constructor(private val mHandler: Handler) : Runnable {

        internal fun notifySeek() {
            mHandler.removeCallbacks(this)
            mHandler.postDelayed(this, 500)
        }

        override fun run() {
            savePositionInTrack()
            sendPublicIntent(PLAY_STATE_CHANGED) // for musixmatch synced lyrics
        }
    }

    companion object {
        val TAG: String = MusicService::class.java.simpleName

        const val SAVED_POSITION = "POSITION"
        const val SAVED_POSITION_IN_TRACK = "POSITION_IN_TRACK"
        const val SAVED_SHUFFLE_MODE = "SHUFFLE_MODE"
        const val SAVED_REPEAT_MODE = "REPEAT_MODE"

        const val RELEASE_WAKELOCK = 0
        const val TRACK_ENDED = 1
        const val TRACK_WENT_TO_NEXT = 2
        const val PLAY_SONG = 3
        const val PREPARE_NEXT = 4
        const val SET_POSITION = 5
        const val RESTORE_QUEUES = 9
        const val SHUFFLE_MODE_NONE = 0
        const val SHUFFLE_MODE_SHUFFLE = 1
        const val REPEAT_MODE_NONE = 0
        const val REPEAT_MODE_ALL = 1
        const val REPEAT_MODE_THIS = 2
        const val SAVE_QUEUES = 0
        const val FOCUS_CHANGE = 6
        const val DUCK = 7
        const val UN_DUCK = 8
        const val MEDIA_SESSION_ACTIONS = (PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                or PlaybackStateCompat.ACTION_STOP
                or PlaybackStateCompat.ACTION_SEEK_TO)

        private fun getTrackUri(song: Song): String {
            return MusicUtil.getSongFileUri(song.id).toString()
        }

        private fun copy(bitmap: Bitmap): Bitmap? {
            var config: Bitmap.Config? = bitmap.config
            if (config == null) {
                config = Bitmap.Config.RGB_565
            }
            try {
                return bitmap.copy(config, false)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
                return null
            }

        }
    }
}