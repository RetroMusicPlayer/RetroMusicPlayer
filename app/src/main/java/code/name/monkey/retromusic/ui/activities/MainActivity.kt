package code.name.monkey.retromusic.ui.activities

import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.NavigationViewUtil
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SearchQueryHelper
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.ui.activities.bugreport.BugReportActivity
import code.name.monkey.retromusic.ui.fragments.mainactivity.LibraryFragment
import code.name.monkey.retromusic.ui.fragments.mainactivity.folders.FoldersFragment
import code.name.monkey.retromusic.ui.fragments.mainactivity.home.BannerHomeFragment
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main_drawer_layout.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)

        getBottomNavigationView()!!.setOnNavigationItemSelectedListener {
            PreferenceUtil.getInstance().lastPage = it.itemId
            selectedFragment(it.itemId)
            true
        }

        setUpDrawerLayout()

        if (savedInstanceState == null) {
            setMusicChooser(PreferenceUtil.getInstance().lastMusicChooser)
        } else {
            restoreCurrentFragment();
        }

        checkShowChangelog()

        if (!App.isProVersion && !PreferenceManager.getDefaultSharedPreferences(this).getBoolean("shown", false)) {
            showPromotionalOffer()
        }
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
                //expandPanel();
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

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, null).commit()
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
                val songs = ArrayList(PlaylistSongsLoader.getPlaylistSongList(this, id).blockingFirst())
                MusicPlayerRemote.openQueue(songs, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Albums.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "albumId", "album").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                MusicPlayerRemote.openQueue(AlbumLoader.getAlbum(this, id).blockingFirst().songs!!, position, true)
                handled = true
            }
        } else if (MediaStore.Audio.Artists.CONTENT_TYPE == mimeType) {
            val id = parseIdFromIntent(intent, "artistId", "artist").toInt()
            if (id >= 0) {
                val position = intent.getIntExtra("position", 0)
                MusicPlayerRemote.openQueue(ArtistLoader.getArtist(this, id).blockingFirst().songs, position, true)
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
                checkSetUpPro(); // good chance that pro version check was delayed on first start
            }
            REQUEST_CODE_THEME, APP_USER_INFO_REQUEST -> postRecreate()
            PURCHASE_REQUEST -> {
                if (resultCode == RESULT_OK) {
                    checkSetUpPro();
                }
            }
        }

    }

    override fun handleBackPress(): Boolean {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers()
            return true
        }
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
                key == PreferenceUtil.TAB_TEXT_MODE)
            postRecreate()
    }

    private fun showPromotionalOffer() {
        MaterialDialog.Builder(this)
                .positiveText("Buy")
                .onPositive { _, _ -> startActivity(Intent(this@MainActivity, ProVersionActivity::class.java)) }
                .negativeText(android.R.string.cancel)
                .customView(R.layout.dialog_promotional_offer, false)
                .dismissListener {
                    PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                            .edit()
                            .putBoolean("shown", true)
                            .apply()
                }
                .show()
    }

    private fun selectedFragment(itemId: Int) {
        when (itemId) {
            R.id.action_album,
            R.id.action_artist,
            R.id.action_playlist,
            R.id.action_song -> setCurrentFragment(LibraryFragment.newInstance(itemId))
        }
    }

    private fun setUpNavigationView() {
        val accentColor = ThemeStore.accentColor(this)
        NavigationViewUtil.setItemIconColors(navigationView, ATHUtil.resolveColor(this, R.attr.iconColor, ThemeStore.textColorSecondary(this)), accentColor)
        NavigationViewUtil.setItemTextColors(navigationView, ThemeStore.textColorPrimary(this), accentColor)

        checkSetUpPro()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_library -> Handler().postDelayed({ setMusicChooser(LIBRARY) }, 200)
                R.id.nav_home -> Handler().postDelayed({ setMusicChooser(HOME) }, 200)
                R.id.nav_folders -> Handler().postDelayed({ setMusicChooser(FOLDERS) }, 200)
                R.id.buy_pro -> Handler().postDelayed({ startActivityForResult(Intent(this@MainActivity, ProVersionActivity::class.java), PURCHASE_REQUEST) }, 200)
                R.id.nav_settings -> Handler().postDelayed({ NavigationUtil.goToSettings(this@MainActivity) }, 200)
                R.id.nav_equalizer -> Handler().postDelayed({ NavigationUtil.openEqualizer(this@MainActivity) }, 200)
                R.id.nav_share_app -> Handler().postDelayed({ shareApp() }, 200)
                R.id.nav_report_bug -> Handler().postDelayed({ prepareBugReport() }, 200)
            }
            true
        }
    }

    private fun prepareBugReport() {
        startActivity(Intent(this, BugReportActivity::class.java))
    }

    private fun shareApp() {
        val shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("songText/plain")
                .setText(String.format(getString(R.string.app_share), packageName))
                .intent
        if (shareIntent.resolveActivity(packageManager) != null) {
            startActivity(
                    Intent.createChooser(shareIntent, resources.getText(R.string.action_share)))
        }
    }


    private fun setMusicChooser(key: Int) {
        PreferenceUtil.getInstance().lastMusicChooser = key
        when (key) {
            LIBRARY -> {
                navigationView.setCheckedItem(R.id.nav_library)
                setCurrentFragment(LibraryFragment.newInstance())
            }
            FOLDERS -> {
                navigationView.setCheckedItem(R.id.nav_folders)
                setCurrentFragment(FoldersFragment.newInstance(this))
            }
            HOME -> {
                navigationView.setCheckedItem(R.id.nav_home)
                setCurrentFragment(BannerHomeFragment())
            }
        }
    }


    private fun checkSetUpPro() {
        if (App.isProVersion) {
            setUpPro()
        }
    }

    private fun setUpPro() {
        navigationView.menu.removeGroup(R.id.navigation_drawer_menu_category_buy_pro)
    }

    private fun setUpDrawerLayout() {
        setUpNavigationView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(navigationView)) {
                drawerLayout.closeDrawer(navigationView)
            } else {
                drawerLayout.openDrawer(navigationView)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val APP_INTRO_REQUEST = 2323
        const val LIBRARY = 1
        const val FOLDERS = 3
        const val HOME = 0
        private const val TAG = "MainActivity"
        private const val APP_USER_INFO_REQUEST = 9003
        private const val REQUEST_CODE_THEME = 9002
        private const val PURCHASE_REQUEST = 101
    }
}
