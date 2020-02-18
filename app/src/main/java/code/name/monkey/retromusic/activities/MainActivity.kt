package code.name.monkey.retromusic.activities

import android.app.ActivityOptions
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ATHUtil.resolveColor
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.R.drawable
import code.name.monkey.retromusic.R.id
import code.name.monkey.retromusic.R.layout
import code.name.monkey.retromusic.R.string
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog.Companion.create
import code.name.monkey.retromusic.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.fragments.mainactivity.AlbumsFragment
import code.name.monkey.retromusic.fragments.mainactivity.ArtistsFragment
import code.name.monkey.retromusic.fragments.mainactivity.GenresFragment
import code.name.monkey.retromusic.fragments.mainactivity.LibraryFragment
import code.name.monkey.retromusic.fragments.mainactivity.PlayingQueueFragment
import code.name.monkey.retromusic.fragments.mainactivity.PlaylistsFragment
import code.name.monkey.retromusic.fragments.mainactivity.SongsFragment
import code.name.monkey.retromusic.fragments.mainactivity.folders.FoldersFragment
import code.name.monkey.retromusic.fragments.mainactivity.home.BannerHomeFragment
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SearchQueryHelper
import code.name.monkey.retromusic.helper.SortOrder.AlbumSortOrder
import code.name.monkey.retromusic.helper.SortOrder.ArtistSortOrder
import code.name.monkey.retromusic.helper.SortOrder.SongSortOrder
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.AlbumLoader
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.service.MusicService
import code.name.monkey.retromusic.util.AppRater
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialcab.MaterialCab
import com.afollestad.materialcab.MaterialCab.Callback
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import kotlinx.android.synthetic.main.activity_main_content.appBarLayout
import kotlinx.android.synthetic.main.activity_main_content.toolbar
import kotlinx.android.synthetic.main.activity_main_content.toolbarContainer
import java.util.ArrayList

class MainActivity : AbsSlidingMusicPanelActivity(), CabHolder, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var currentFragment: MainActivityFragmentCallbacks

    private var blockRequestPermissions: Boolean = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null && action == Intent.ACTION_SCREEN_OFF) {
                if (PreferenceUtil.getInstance(this@MainActivity).lockScreen && MusicPlayerRemote.isPlaying) {
                    val activity = Intent(context, LockScreenActivity::class.java)
                    activity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    activity.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                    ActivityCompat.startActivity(context, activity, null)
                }
            }
        }
    }

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_main_content)
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        getBottomNavigationView().selectedItemId = PreferenceUtil.getInstance(this).lastPage
        getBottomNavigationView().setOnNavigationItemSelectedListener {
            PreferenceUtil.getInstance(this).lastPage = it.itemId
            selectedFragment(it.itemId)
            true
        }

        if (savedInstanceState == null) {
            setMusicChooser(PreferenceUtil.getInstance(this).lastMusicChooser)
        } else {
            restoreCurrentFragment()
        }

        checkShowChangelog()
        AppRater.appLaunched(this)
        setupToolbar()
        setBottomBarVisibility(View.VISIBLE)
    }

    private fun setupToolbar() {
        toolbar.setBackgroundColor(Color.TRANSPARENT)
        toolbarContainer.backgroundTintList =
            ColorStateList.valueOf(resolveColor(this, R.attr.colorSurface))
        setSupportActionBar(toolbar)
        toolbar.setOnClickListener {
            val options = ActivityOptions
                .makeSceneTransitionAnimation(
                    this, toolbarContainer,
                    getString(R.string.transition_toolbar)
                )
            NavigationUtil.goToSearch(this, options)
        }
    }

    private fun checkShowChangelog() {
        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            val currentVersion = pInfo.versionCode
            if (currentVersion != PreferenceUtil.getInstance(this).lastChangelogVersion) {
                startActivityForResult(
                    Intent(this, WhatsNewActivity::class.java),
                    APP_INTRO_REQUEST
                )
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        val screenOnOff = IntentFilter()
        screenOnOff.addAction(Intent.ACTION_SCREEN_OFF)
        registerReceiver(broadcastReceiver, screenOnOff)

        PreferenceUtil.getInstance(this).registerOnSharedPreferenceChangedListener(this)

        if (intent.hasExtra("expand")) {
            if (intent.getBooleanExtra("expand", false)) {
                expandPanel()
                intent.putExtra("expand", false)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
        PreferenceUtil.getInstance(this).unregisterOnSharedPreferenceChangedListener(this)
    }

    private fun setCurrentFragment(fragment: Fragment, tag: String) {
        if (tag != supportFragmentManager.findFragmentById(R.id.fragment_container)?.tag) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag).commit()
            currentFragment = fragment as MainActivityFragmentCallbacks
        }
    }

    private fun restoreCurrentFragment() {
        currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as MainActivityFragmentCallbacks
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
        if (key == PreferenceUtil.GENERAL_THEME || key == PreferenceUtil.BLACK_THEME ||
            key == PreferenceUtil.ADAPTIVE_COLOR_APP || key == PreferenceUtil.DOMINANT_COLOR ||
            key == PreferenceUtil.USER_NAME || key == PreferenceUtil.TOGGLE_FULL_SCREEN ||
            key == PreferenceUtil.TOGGLE_VOLUME || key == PreferenceUtil.ROUND_CORNERS ||
            key == PreferenceUtil.CAROUSEL_EFFECT || key == PreferenceUtil.NOW_PLAYING_SCREEN_ID ||
            key == PreferenceUtil.TOGGLE_GENRE || key == PreferenceUtil.BANNER_IMAGE_PATH ||
            key == PreferenceUtil.PROFILE_IMAGE_PATH || key == PreferenceUtil.CIRCULAR_ALBUM_ART ||
            key == PreferenceUtil.KEEP_SCREEN_ON || key == PreferenceUtil.TOGGLE_SEPARATE_LINE ||
            key == PreferenceUtil.TOGGLE_HOME_BANNER || key == PreferenceUtil.TOGGLE_ADD_CONTROLS ||
            key == PreferenceUtil.ALBUM_COVER_STYLE || key == PreferenceUtil.HOME_ARTIST_GRID_STYLE ||
            key == PreferenceUtil.ALBUM_COVER_TRANSFORM || key == PreferenceUtil.DESATURATED_COLOR ||
            key == PreferenceUtil.TAB_TEXT_MODE || key == PreferenceUtil.LIBRARY_CATEGORIES
        )
            postRecreate()
    }

    private fun selectedFragment(itemId: Int) {
        when (itemId) {
            R.id.action_album -> setCurrentFragment(AlbumsFragment.newInstance(), itemId.toString())
            R.id.action_artist -> setCurrentFragment(ArtistsFragment.newInstance(), itemId.toString())
            R.id.action_playlist -> setCurrentFragment(PlaylistsFragment.newInstance(), itemId.toString())
            R.id.action_genre -> setCurrentFragment(GenresFragment.newInstance(), itemId.toString())
            R.id.action_playing_queue -> setCurrentFragment(PlayingQueueFragment.newInstance(), itemId.toString())
            R.id.action_song -> setCurrentFragment(SongsFragment.newInstance(), itemId.toString())
            R.id.action_home -> setCurrentFragment(BannerHomeFragment.newInstance(), BannerHomeFragment.TAG)
            else -> {
                setCurrentFragment(BannerHomeFragment.newInstance(), BannerHomeFragment.TAG)
            }
        }
    }

    fun setMusicChooser(key: Int) {
        PreferenceUtil.getInstance(this).lastMusicChooser = key
        when (key) {
            FOLDER -> setCurrentFragment(FoldersFragment.newInstance(this), FoldersFragment.TAG)
            else -> selectedFragment(PreferenceUtil.getInstance(this).lastPage)
        }
    }

    companion object {
        const val APP_INTRO_REQUEST = 2323
        const val HOME = 0
        const val FOLDER = 1
        const val LIBRARY = 2
        private const val TAG = "MainActivity"
        private const val APP_USER_INFO_REQUEST = 9003
        private const val REQUEST_CODE_THEME = 9002
        private const val PURCHASE_REQUEST = 101
    }

    private lateinit var cab: MaterialCab

    fun getTotalAppBarScrollingRange(): Int {
        return appBarLayout.totalScrollRange
    }

    fun addOnAppBarOffsetChangedListener(
        onOffsetChangedListener: OnOffsetChangedListener
    ) {
        appBarLayout.addOnOffsetChangedListener(onOffsetChangedListener)
    }

    fun removeOnAppBarOffsetChangedListener(
        onOffsetChangedListener: OnOffsetChangedListener
    ) {
        appBarLayout.removeOnOffsetChangedListener(onOffsetChangedListener)
    }

    override fun openCab(menuRes: Int, callback: Callback): MaterialCab {
        if (cab != null && cab.isActive()) {
            cab.finish()
        }

        cab = MaterialCab(this, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
            .setBackgroundColor(
                RetroColorUtil.shiftBackgroundColorForLightText(
                    ATHUtil.resolveColor(this, R.attr.colorSurface)
                )
            )
            .start(callback)
        return cab
    }

    private fun getCurrentFragment(): Fragment? {
        return if (supportFragmentManager == null) {
            SongsFragment.newInstance()
        } else supportFragmentManager.findFragmentByTag(LibraryFragment.TAG)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        val currentFragment: Fragment? = getCurrentFragment()
        if (currentFragment is AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>
            && currentFragment.isAdded()
        ) {

            if (currentFragment is SongsFragment) {
                menu!!.removeItem(id.action_grid_size)
                menu.removeItem(id.action_layout_type)
            } else {
                val gridSizeItem = menu!!.findItem(id.action_grid_size)
                if (RetroUtil.isLandscape()) {
                    gridSizeItem.setTitle(string.action_grid_size_land)
                }
                setUpGridSizeMenu(currentFragment, gridSizeItem.subMenu)
                val layoutItem = menu.findItem(id.action_layout_type)
                setupLayoutMenu(currentFragment, layoutItem.subMenu)
            }
            setUpSortOrderMenu(currentFragment, menu.findItem(id.action_sort_order).subMenu)
        } else if (currentFragment is GenresFragment || currentFragment is PlayingQueueFragment) {
            menu!!.removeItem(id.action_new_playlist)
            menu.removeItem(id.action_layout_type)
            menu.removeItem(id.action_grid_size)
            menu.removeItem(id.action_sort_order)
        } else {
            menu!!.add(0, id.action_new_playlist, 0, string.new_playlist_title)
                .setIcon(drawable.ic_playlist_add_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            menu.removeItem(id.action_grid_size)
            menu.removeItem(id.action_layout_type)
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun setUpGridSizeMenu(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
        gridSizeMenu: SubMenu
    ) {
        when (fragment.getGridSize()) {
            1 -> gridSizeMenu.findItem(id.action_grid_size_1).isChecked = true
            2 -> gridSizeMenu.findItem(id.action_grid_size_2).isChecked = true
            3 -> gridSizeMenu.findItem(id.action_grid_size_3).isChecked = true
            4 -> gridSizeMenu.findItem(id.action_grid_size_4).isChecked = true
            5 -> gridSizeMenu.findItem(id.action_grid_size_5).isChecked = true
            6 -> gridSizeMenu.findItem(id.action_grid_size_6).isChecked = true
            7 -> gridSizeMenu.findItem(id.action_grid_size_7).isChecked = true
            8 -> gridSizeMenu.findItem(id.action_grid_size_8).isChecked = true
        }
        val maxGridSize = fragment.maxGridSize
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(id.action_grid_size_8).isVisible = false
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(id.action_grid_size_7).isVisible = false
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(id.action_grid_size_6).isVisible = false
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(id.action_grid_size_5).isVisible = false
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(id.action_grid_size_4).isVisible = false
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(id.action_grid_size_3).isVisible = false
        }
    }

    private fun setUpSortOrderMenu(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
        sortOrderMenu: SubMenu
    ) {
        val currentSortOrder = fragment.getSortOrder()
        sortOrderMenu.clear()
        when (fragment) {
            is AlbumsFragment -> {
                sortOrderMenu.add(0, id.action_album_sort_order_asc, 0, string.sort_order_a_z).isChecked =
                    currentSortOrder == AlbumSortOrder.ALBUM_A_Z
                sortOrderMenu.add(0, id.action_album_sort_order_desc, 1, string.sort_order_z_a).isChecked =
                    currentSortOrder == AlbumSortOrder.ALBUM_Z_A
                sortOrderMenu.add(0, id.action_album_sort_order_artist, 2, string.sort_order_artist).isChecked =
                    currentSortOrder == AlbumSortOrder.ALBUM_ARTIST
                sortOrderMenu.add(0, id.action_album_sort_order_year, 3, string.sort_order_year).isChecked =
                    currentSortOrder == AlbumSortOrder.ALBUM_YEAR
            }
            is ArtistsFragment -> {
                sortOrderMenu.add(0, id.action_artist_sort_order_asc, 0, string.sort_order_a_z).isChecked =
                    currentSortOrder == ArtistSortOrder.ARTIST_A_Z
                sortOrderMenu.add(0, id.action_artist_sort_order_desc, 1, string.sort_order_z_a).isChecked =
                    currentSortOrder == ArtistSortOrder.ARTIST_Z_A
            }
            is SongsFragment -> {
                sortOrderMenu.add(0, id.action_song_sort_order_asc, 0, string.sort_order_a_z).isChecked =
                    currentSortOrder == SongSortOrder.SONG_A_Z
                sortOrderMenu.add(0, id.action_song_sort_order_desc, 1, string.sort_order_z_a).isChecked =
                    currentSortOrder == SongSortOrder.SONG_Z_A
                sortOrderMenu.add(0, id.action_song_sort_order_artist, 2, string.sort_order_artist).isChecked =
                    currentSortOrder == SongSortOrder.SONG_ARTIST
                sortOrderMenu.add(0, id.action_song_sort_order_album, 3, string.sort_order_album).isChecked =
                    currentSortOrder == SongSortOrder.SONG_ALBUM
                sortOrderMenu.add(0, id.action_song_sort_order_year, 4, string.sort_order_year).isChecked =
                    currentSortOrder == SongSortOrder.SONG_YEAR
                sortOrderMenu.add(0, id.action_song_sort_order_date, 5, string.sort_order_date).isChecked =
                    currentSortOrder == SongSortOrder.SONG_DATE
                sortOrderMenu.add(0, id.action_song_sort_order_composer, 6, string.sort_order_composer).isChecked =
                    currentSortOrder == SongSortOrder.COMPOSER
            }
        }
        sortOrderMenu.setGroupCheckable(0, true, true)
    }

    private fun setupLayoutMenu(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
        subMenu: SubMenu
    ) {
        when (fragment.itemLayoutRes()) {
            layout.item_card -> subMenu.findItem(id.action_layout_card).isChecked = true
            layout.item_grid -> subMenu.findItem(id.action_layout_normal).isChecked = true
            layout.item_card_color -> subMenu.findItem(id.action_layout_colored_card).isChecked = true
            layout.item_grid_circle -> subMenu.findItem(id.action_layout_circular).isChecked = true
            layout.image -> subMenu.findItem(id.action_layout_image).isChecked = true
            layout.item_image_gradient -> subMenu.findItem(id.action_layout_gradient_image).isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if (pager == null) return false;
        val currentFragment = getCurrentFragment()
        if (currentFragment is AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>) {
            if (handleGridSizeMenuItem(currentFragment, item)) {
                return true
            }
            if (handleLayoutResType(currentFragment, item)) {
                return true
            }
            if (handleSortOrderMenuItem(currentFragment, item)) {
                return true
            }
        }
        when (item.itemId) {
            R.id.action_search -> {
                val options = ActivityOptions
                    .makeSceneTransitionAnimation(
                        this, toolbarContainer,
                        getString(string.transition_toolbar)
                    )
                NavigationUtil.goToSearch(this, options)
            }
            R.id.action_new_playlist -> {
                create().show(supportFragmentManager, "CREATE_PLAYLIST")
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun handleGridSizeMenuItem(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
        item: MenuItem
    ): Boolean {
        var gridSize = 0
        when (item.itemId) {
            id.action_grid_size_1 -> gridSize = 1
            id.action_grid_size_2 -> gridSize = 2
            id.action_grid_size_3 -> gridSize = 3
            id.action_grid_size_4 -> gridSize = 4
            id.action_grid_size_5 -> gridSize = 5
            id.action_grid_size_6 -> gridSize = 6
            id.action_grid_size_7 -> gridSize = 7
            id.action_grid_size_8 -> gridSize = 8
        }
        if (gridSize > 0) {
            item.isChecked = true
            fragment.setAndSaveGridSize(gridSize)
            return true
        }
        return false
    }

    private fun handleLayoutResType(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
        item: MenuItem
    ): Boolean {
        var layoutRes = -1
        when (item.itemId) {
            id.action_layout_normal -> layoutRes = layout.item_grid
            id.action_layout_card -> layoutRes = layout.item_card
            id.action_layout_colored_card -> layoutRes = layout.item_card_color
            id.action_layout_circular -> layoutRes = layout.item_grid_circle
            id.action_layout_image -> layoutRes = layout.image
            id.action_layout_gradient_image -> layoutRes = layout.item_image_gradient
        }
        if (layoutRes != -1) {
            item.isChecked = true
            fragment.setAndSaveLayoutRes(layoutRes)
            return true
        }
        return false
    }

    private fun handleSortOrderMenuItem(
        fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>, item: MenuItem
    ): Boolean {
        var sortOrder: String? = null
        when (fragment) {
            is SongsFragment -> {
                when (item.itemId) {
                    id.action_song_sort_order_asc -> sortOrder = SongSortOrder.SONG_A_Z
                    id.action_song_sort_order_desc -> sortOrder = SongSortOrder.SONG_Z_A
                    id.action_song_sort_order_artist -> sortOrder = SongSortOrder.SONG_ARTIST
                    id.action_song_sort_order_album -> sortOrder = SongSortOrder.SONG_ALBUM
                    id.action_song_sort_order_year -> sortOrder = SongSortOrder.SONG_YEAR
                    id.action_song_sort_order_date -> sortOrder = SongSortOrder.SONG_DATE
                    id.action_song_sort_order_composer -> sortOrder = SongSortOrder.COMPOSER
                }
            }
            is AlbumsFragment -> {
                when (item.itemId) {
                    id.action_album_sort_order_asc -> sortOrder = AlbumSortOrder.ALBUM_A_Z
                    id.action_album_sort_order_desc -> sortOrder = AlbumSortOrder.ALBUM_Z_A
                    id.action_album_sort_order_artist -> sortOrder = AlbumSortOrder.ALBUM_ARTIST
                    id.action_album_sort_order_year -> sortOrder = AlbumSortOrder.ALBUM_YEAR
                }
            }
            is ArtistsFragment -> {
                when (item.itemId) {
                    id.action_artist_sort_order_asc -> sortOrder = ArtistSortOrder.ARTIST_A_Z
                    id.action_artist_sort_order_desc -> sortOrder = ArtistSortOrder.ARTIST_Z_A
                }
            }
        }
        if (sortOrder != null) {
            item.isChecked = true
            fragment.setAndSaveSortOrder(sortOrder)
            return true
        }
        return false
    }
}
