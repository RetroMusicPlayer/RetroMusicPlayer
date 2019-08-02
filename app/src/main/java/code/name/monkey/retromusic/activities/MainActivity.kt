package code.name.monkey.retromusic.activities

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.fragments.mainactivity.LibraryFragment
import code.name.monkey.retromusic.fragments.mainactivity.folders.FoldersFragment
import code.name.monkey.retromusic.fragments.mainactivity.home.BannerHomeFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SearchQueryHelper
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class MainActivity : AbsSlidingMusicPanelActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var currentFragment: MainActivityFragmentCallbacks

    private var blockRequestPermissions: Boolean = false
    private val disposable = CompositeDisposable()
    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null && action == Intent.ACTION_SCREEN_OFF) {
                if (PreferenceUtil.getInstance().lockScreen && MusicPlayerRemote.isPlaying) {
                    val activity = Intent(context, LockScreenActivity::class.java)
                    activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    ActivityCompat.startActivity(context, activity, null)
                }
            }
        }
    }

    override fun createContentView(): View {
        @SuppressLint("InflateParams")
        val contentView = layoutInflater.inflate(R.layout.activity_main_drawer_layout, null)
        val drawerContent = contentView.findViewById<ViewGroup>(R.id.drawer_content_container)
        drawerContent.addView(wrapSlidingMusicPanel(R.layout.activity_main_content))
        return contentView
    }

    override fun onCreate(
            savedInstanceState: Bundle?
    ) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)

        getBottomNavigationView().selectedItemId = PreferenceUtil.getInstance().lastPage

        getBottomNavigationView().setOnNavigationItemSelectedListener {
            PreferenceUtil.getInstance().lastPage = it.itemId
            selectedFragment(it.itemId)
            true
        }

        if (savedInstanceState == null) {
            selectedFragment(PreferenceUtil.getInstance().lastPage)
        } else {
            restoreCurrentFragment();
        }

        checkShowChangelog()

        /*if (!App.isProVersion && !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("shown", false)) {
            showPromotionalOffer()
        }*/
    }

    private fun checkShowChangelog() {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val currentVersion = pInfo.versionCode
            if (currentVersion != PreferenceUtil.getInstance().lastChangelogVersion) {
                startActivityForResult(Intent(this, WhatsNewActivity::class.java), APP_INTRO_REQUEST)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        val screenOnOff = IntentFilter()
        screenOnOff.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(broadcastReceiver, screenOnOff)

        PreferenceUtil.getInstance().registerOnSharedPreferenceChangedListener(this)

        if (intent.hasExtra("expand")) {
            if (intent.getBooleanExtra("expand", false)) {
                expandPanel()
                intent.putExtra("expand", false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear()
        unregisterReceiver(broadcastReceiver)
        PreferenceUtil.getInstance().unregisterOnSharedPreferenceChangedListener(this)
    }

    private fun setCurrentFragment(fragment: Fragment, b: Boolean = false) {
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragment_container, fragment, null)
        if (b) {
            trans.addToBackStack(null)
        }
        trans.commit()
        currentFragment = fragment as MainActivityFragmentCallbacks
    }


    private fun restoreCurrentFragment() {
        currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as MainActivityFragmentCallbacks
    }

    private fun handlePlaybackIntent(intent: Intent?) {
        if (intent == null) {
            return
        }

        val uri = intent.data
        val mimeType = intent.type
        var handled = false

        if (intent.action != null && intent.action == MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH) {
            val songs = SearchQueryHelper.getSongs(this, intent.extras!!)
            if (MusicPlayerRemote.shuffleMode == MusicService.SHUFFLE_MODE_SHUFFLE) {
                MusicPlayerRemote.openAndShuffleQueue(songs, true)
            } else {
                MusicPlayerRemote.openQueue(songs, 0, true)
            }
            handled = true
        }

        if (uri != null && uri.toString().isNotEmpty()) {
            MusicPlayerRemote.playFromUri(uri)
            handled = true
        } else if (MediaStore.Audio.Playlists.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "playlistId", "playlist").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                val songs = ArrayList(PlaylistSongsLoader.getPlaylistSongList(this, id))
                MusicPlayerRemote.openQueue(songs, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "albumId", "album").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                MusicPlayerRemote.openQueue(AlbumLoader.getAlbum(this, id).songs!!, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "artistId", "artist").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                MusicPlayerRemote.openQueue(ArtistLoader.getArtist(this, id).songs, position, true)
                handled = true
            }
        }
        if (handled) {
            setIntent(Intent())
        }
    }

    private fun parseIdFromIntent(intent: Intent, longKey: String, stringKey: String): Long {
        var id = intent.getLongExtra(longKey, -1)
        if (id < 0) {
            val idString = intent.getStringExtra(stringKey)
            if (idString != null) {
                try {
                    id = java.lang.Long.parseLong(idString)
                } catch (e: NumberFormatException) {
                    Log.e(TAG, e.message)
                }
            }
        }
        return id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            APP_INTRO_REQUEST -> {
                blockRequestPermissions = false
                if (!hasPermissions()) {
                    requestPermissions()
                }
            }
            REQUEST_CODE_THEME, APP_USER_INFO_REQUEST -> postRecreate()
            PURCHASE_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    //checkSetUpPro();
                }
            }
        }

    }

    override fun handleBackPress(): Boolean {
        return super.handleBackPress() || currentFragment.handleBackPress()
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        handlePlaybackIntent(intent)
    }

    override fun requestPermissions() {
        if (!blockRequestPermissions) {
            super.requestPermissions()
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == PreferenceUtil.GENERAL_THEME ||
                key == PreferenceUtil.ADAPTIVE_COLOR_APP ||
                key == PreferenceUtil.DOMINANT_COLOR ||
                key == PreferenceUtil.USER_NAME ||
                key == PreferenceUtil.TOGGLE_FULL_SCREEN ||
                key == PreferenceUtil.TOGGLE_VOLUME ||
                key == PreferenceUtil.ROUND_CORNERS ||
                key == PreferenceUtil.CAROUSEL_EFFECT ||
                key == PreferenceUtil.NOW_PLAYING_SCREEN_ID ||
                key == PreferenceUtil.TOGGLE_GENRE ||
                key == PreferenceUtil.BANNER_IMAGE_PATH ||
                key == PreferenceUtil.PROFILE_IMAGE_PATH ||
                key == PreferenceUtil.CIRCULAR_ALBUM_ART ||
                key == PreferenceUtil.KEEP_SCREEN_ON ||
                key == PreferenceUtil.TOGGLE_SEPARATE_LINE ||
                key == PreferenceUtil.ALBUM_GRID_STYLE ||
                key == PreferenceUtil.ARTIST_GRID_STYLE ||
                key == PreferenceUtil.TOGGLE_HOME_BANNER ||
                key == PreferenceUtil.TOGGLE_ADD_CONTROLS ||
                key == PreferenceUtil.ALBUM_COVER_STYLE ||
                key == PreferenceUtil.HOME_ARTIST_GRID_STYLE ||
                key == PreferenceUtil.ALBUM_COVER_TRANSFORM ||
                key == PreferenceUtil.TAB_TEXT_MODE ||
                key == PreferenceUtil.LIBRARY_CATEGORIES)
            postRecreate()

    }

    private fun showPromotionalOffer() {
        /*MaterialDialog(this).show {
            positiveButton(text = "Buy") { startActivity(Intent(this@MainActivity, PurchaseActivity::class.java)) }
            negativeButton(android.R.string.cancel)
            customView(R.layout.dialog_promotional_offer)
            onDismiss {
                PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                        .edit()
                        .putBoolean("shown", true)
                        .apply()
            }
        }*/
    }

    fun selectedFragment(itemId: Int) {
        when (itemId) {
            R.id.action_album,
            R.id.action_artist,
            R.id.action_playlist,
            R.id.action_genre,
            R.id.action_song -> setCurrentFragment(LibraryFragment.newInstance(itemId), false)
            R.id.action_home -> setCurrentFragment(BannerHomeFragment.newInstance(), false)
            R.id.action_folder -> setCurrentFragment(FoldersFragment.newInstance(this), false)
            else -> {
                setCurrentFragment(BannerHomeFragment.newInstance(), false)
            }
        }
    }

    companion object {
        const val APP_INTRO_REQUEST = 2323
        const val HOME = 0
        private const val TAG = "MainActivity"
        private const val APP_USER_INFO_REQUEST = 9003
        private const val REQUEST_CODE_THEME = 9002
        private const val PURCHASE_REQUEST = 101
    }
}
