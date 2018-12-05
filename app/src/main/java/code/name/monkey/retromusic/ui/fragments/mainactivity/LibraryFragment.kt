package code.name.monkey.retromusic.ui.fragments.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.common.ATHToolbarActivity
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.CreatePlaylistDialog
import code.name.monkey.retromusic.dialogs.SleepTimerDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.interfaces.MainActivityFragmentCallbacks
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.ui.activities.SettingsActivity
import code.name.monkey.retromusic.ui.fragments.base.AbsLibraryPagerRecyclerViewCustomGridSizeFragment
import code.name.monkey.retromusic.ui.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialcab.MaterialCab
import com.google.android.material.appbar.AppBarLayout

class LibraryFragment : AbsMainActivityFragment(), CabHolder, MainActivityFragmentCallbacks, AppBarLayout.OnOffsetChangedListener {
    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        mainActivity.setLightStatusbar(!ATHUtil.isWindowBackgroundDark(context))
    }


    lateinit var toolbar: Toolbar
    lateinit var appbar: AppBarLayout
    lateinit var title: TextView
    lateinit var contentContainer: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        toolbar = view.findViewById(R.id.toolbar)
        appbar = view.findViewById(R.id.app_bar)
        title = view.findViewById(R.id.title)
        contentContainer = view.findViewById(R.id.fragment_container)
        return view
    }

    private var cab: MaterialCab? = null


    val totalAppBarScrollingRange: Int
        get() = appbar.totalScrollRange

    private val currentFragment: Fragment?
        get() = if (fragmentManager == null) {
            SongsFragment.newInstance()
        } else fragmentManager!!.findFragmentByTag(LibraryFragment.TAG)


    fun setTitle(@StringRes name: Int) {
        title.text = getString(name)
    }

    fun addOnAppBarOffsetChangedListener(onOffsetChangedListener: AppBarLayout.OnOffsetChangedListener) {
        appbar.addOnOffsetChangedListener(onOffsetChangedListener)
    }

    fun removeOnAppBarOffsetChangedListener(onOffsetChangedListener: AppBarLayout.OnOffsetChangedListener) {
        appbar.removeOnOffsetChangedListener(onOffsetChangedListener)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatusbarColorAuto(view)
        setupToolbar()
        inflateFragment()

    }

    private fun inflateFragment() {
        if (arguments == null) {
            selectedFragment(SongsFragment.newInstance())
            return
        }
        when (arguments!!.getInt(CURRENT_TAB_ID)) {
            R.id.action_song -> selectedFragment(SongsFragment.newInstance())
            R.id.action_album -> selectedFragment(AlbumsFragment.newInstance())
            R.id.action_artist -> selectedFragment(ArtistsFragment.newInstance())
            R.id.action_playlist -> selectedFragment(PlaylistsFragment.newInstance())
            else -> selectedFragment(SongsFragment.newInstance())
        }
    }

    private fun setupToolbar() {
        title.setTextColor(ThemeStore.textColorPrimary(context!!))

        val primaryColor = ThemeStore.primaryColor(context!!)
        TintHelper.setTintAuto(contentContainer, primaryColor, true)

        toolbar.setBackgroundColor(primaryColor)
        appbar.setBackgroundColor(primaryColor)
        appbar.addOnOffsetChangedListener(this)
        mainActivity.title = null
        mainActivity.setSupportActionBar(toolbar)
        toolbar.navigationIcon = RetroUtil.getTintedDrawable(mainActivity, R.drawable.ic_menu_white_24dp, ThemeStore.textColorPrimary(mainActivity))
    }


    override fun handleBackPress(): Boolean {
        if (cab != null && cab!!.isActive) {
            cab!!.finish()
            return true
        }
        return false
    }

    private fun selectedFragment(fragment: Fragment) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction
                .replace(R.id.fragment_container, fragment, TAG)
                .commit()
    }

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        if (cab != null && cab!!.isActive) {
            cab!!.finish()
        }

        cab = MaterialCab(mainActivity, R.id.cab_stub)
                .setMenu(menuRes)
                .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
                .setBackgroundColor(
                        RetroColorUtil.shiftBackgroundColorForLightText(ThemeStore.primaryColor(activity!!)))
                .start(callback)
        return cab as MaterialCab
    }


    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.menu_main, menu)

        val currentFragment = currentFragment
        if (currentFragment is AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *> && currentFragment.isAdded) {
            val fragment = currentFragment as AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>?

            val gridSizeItem = menu!!.findItem(R.id.action_grid_size)
            if (RetroUtil.isLandscape()) {
                gridSizeItem.setTitle(R.string.action_grid_size_land)
            }
            setUpGridSizeMenu(fragment!!, gridSizeItem.subMenu)

            setUpSortOrderMenu(fragment, menu.findItem(R.id.action_sort_order).subMenu)

        } else {
            menu!!.add(0, R.id.action_new_playlist, 0, R.string.new_playlist_title)
            menu.removeItem(R.id.action_grid_size)
        }
        activity ?: return
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(getActivity(), toolbar, menu, ATHToolbarActivity.getToolbarBackgroundColor(toolbar))
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)
        val activity = activity ?: return
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(activity, toolbar)
    }


    private fun setUpSortOrderMenu(
            fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
            sortOrderMenu: SubMenu) {
        val currentSortOrder = fragment.getSortOrder()
        sortOrderMenu.clear()

        if (fragment is AlbumsFragment) {
            sortOrderMenu.add(0, R.id.action_album_sort_order_asc, 0, R.string.sort_order_a_z).isChecked = currentSortOrder == SortOrder.AlbumSortOrder.ALBUM_A_Z
            sortOrderMenu.add(0, R.id.action_album_sort_order_desc, 1, R.string.sort_order_z_a).isChecked = currentSortOrder == SortOrder.AlbumSortOrder.ALBUM_Z_A
            sortOrderMenu.add(0, R.id.action_album_sort_order_artist, 2, R.string.sort_order_artist).isChecked = currentSortOrder == SortOrder.AlbumSortOrder.ALBUM_ARTIST
            sortOrderMenu.add(0, R.id.action_album_sort_order_year, 3, R.string.sort_order_year).isChecked = currentSortOrder == SortOrder.AlbumSortOrder.ALBUM_YEAR
        } else if (fragment is ArtistsFragment) {
            sortOrderMenu.add(0, R.id.action_artist_sort_order_asc, 0, R.string.sort_order_a_z).isChecked = currentSortOrder == SortOrder.ArtistSortOrder.ARTIST_A_Z
            sortOrderMenu.add(0, R.id.action_artist_sort_order_desc, 1, R.string.sort_order_z_a).isChecked = currentSortOrder == SortOrder.ArtistSortOrder.ARTIST_Z_A
        } else if (fragment is SongsFragment) {
            sortOrderMenu.add(0, R.id.action_song_sort_order_asc, 0, R.string.sort_order_a_z).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_A_Z
            sortOrderMenu.add(0, R.id.action_song_sort_order_desc, 1, R.string.sort_order_z_a).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_Z_A
            sortOrderMenu.add(0, R.id.action_song_sort_order_artist, 2, R.string.sort_order_artist).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_ARTIST
            sortOrderMenu.add(0, R.id.action_song_sort_order_album, 3, R.string.sort_order_album).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_ALBUM
            sortOrderMenu.add(0, R.id.action_song_sort_order_year, 4, R.string.sort_order_year).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_YEAR
            sortOrderMenu.add(0, R.id.action_song_sort_order_date, 4, R.string.sort_order_date).isChecked = currentSortOrder == SortOrder.SongSortOrder.SONG_DATE

        }

        sortOrderMenu.setGroupCheckable(0, true, true)
    }

    private fun handleSortOrderMenuItem(
            fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>, item: MenuItem): Boolean {
        var sortOrder: String? = null
        if (fragment is AlbumsFragment) {
            when (item.itemId) {
                R.id.action_album_sort_order_asc -> sortOrder = SortOrder.AlbumSortOrder.ALBUM_A_Z
                R.id.action_album_sort_order_desc -> sortOrder = SortOrder.AlbumSortOrder.ALBUM_Z_A
                R.id.action_album_sort_order_artist -> sortOrder = SortOrder.AlbumSortOrder.ALBUM_ARTIST
                R.id.action_album_sort_order_year -> sortOrder = SortOrder.AlbumSortOrder.ALBUM_YEAR
            }
        } else if (fragment is ArtistsFragment) {
            when (item.itemId) {
                R.id.action_artist_sort_order_asc -> sortOrder = SortOrder.ArtistSortOrder.ARTIST_A_Z
                R.id.action_artist_sort_order_desc -> sortOrder = SortOrder.ArtistSortOrder.ARTIST_Z_A
            }
        } else if (fragment is SongsFragment) {
            when (item.itemId) {
                R.id.action_song_sort_order_asc -> sortOrder = SortOrder.SongSortOrder.SONG_A_Z
                R.id.action_song_sort_order_desc -> sortOrder = SortOrder.SongSortOrder.SONG_Z_A
                R.id.action_song_sort_order_artist -> sortOrder = SortOrder.SongSortOrder.SONG_ARTIST
                R.id.action_song_sort_order_album -> sortOrder = SortOrder.SongSortOrder.SONG_ALBUM
                R.id.action_song_sort_order_year -> sortOrder = SortOrder.SongSortOrder.SONG_YEAR
                R.id.action_song_sort_order_date -> sortOrder = SortOrder.SongSortOrder.SONG_DATE
            }
        }

        if (sortOrder != null) {
            item.isChecked = true
            fragment.setAndSaveSortOrder(sortOrder)
            return true
        }

        return false
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //if (pager == null) return false;
        val currentFragment = currentFragment
        if (currentFragment is AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>) {
            val fragment = currentFragment as AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>?
            if (handleGridSizeMenuItem(fragment!!, item)) {
                return true
            }
            if (handleSortOrderMenuItem(fragment, item)) {
                return true
            }
        }
        val id = item.itemId
        when (id) {
            R.id.action_new_playlist -> {
                CreatePlaylistDialog.create().show(childFragmentManager, "CREATE_PLAYLIST")
                return true
            }
            R.id.action_shuffle_all -> {
                MusicPlayerRemote.openAndShuffleQueue(SongLoader.getAllSongs(context!!).blockingFirst(), true)
                return true
            }
            R.id.action_equalizer -> {
                NavigationUtil.openEqualizer(activity!!)
                return true
            }
            R.id.action_sleep_timer -> {
                if (fragmentManager != null) {
                    SleepTimerDialog().show(fragmentManager!!, TAG)
                }
                return true
            }
            R.id.action_settings -> startActivity(Intent(context, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setUpGridSizeMenu(
            fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>,
            gridSizeMenu: SubMenu) {
        when (fragment.getGridSize()) {
            1 -> gridSizeMenu.findItem(R.id.action_grid_size_1).isChecked = true
            2 -> gridSizeMenu.findItem(R.id.action_grid_size_2).isChecked = true
            3 -> gridSizeMenu.findItem(R.id.action_grid_size_3).isChecked = true
            4 -> gridSizeMenu.findItem(R.id.action_grid_size_4).isChecked = true
            5 -> gridSizeMenu.findItem(R.id.action_grid_size_5).isChecked = true
            6 -> gridSizeMenu.findItem(R.id.action_grid_size_6).isChecked = true
            7 -> gridSizeMenu.findItem(R.id.action_grid_size_7).isChecked = true
            8 -> gridSizeMenu.findItem(R.id.action_grid_size_8).isChecked = true
        }
        val maxGridSize = fragment.maxGridSize
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).isVisible = false
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).isVisible = false
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).isVisible = false
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).isVisible = false
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).isVisible = false
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).isVisible = false
        }
    }


    private fun handleGridSizeMenuItem(
            fragment: AbsLibraryPagerRecyclerViewCustomGridSizeFragment<*, *>, item: MenuItem): Boolean {
        var gridSize = 0
        when (item.itemId) {
            R.id.action_grid_size_1 -> gridSize = 1
            R.id.action_grid_size_2 -> gridSize = 2
            R.id.action_grid_size_3 -> gridSize = 3
            R.id.action_grid_size_4 -> gridSize = 4
            R.id.action_grid_size_5 -> gridSize = 5
            R.id.action_grid_size_6 -> gridSize = 6
            R.id.action_grid_size_7 -> gridSize = 7
            R.id.action_grid_size_8 -> gridSize = 8
        }

        if (gridSize > 0) {
            item.isChecked = true
            fragment.setAndSaveGridSize(gridSize)
            return true
        }
        return false
    }

    companion object {

        const val TAG: String = "LibraryFragment"
        private const val CURRENT_TAB_ID = "current_tab_id"

        fun newInstance(tab: Int): Fragment {
            val args = Bundle()
            args.putInt(CURRENT_TAB_ID, tab)
            val fragment = LibraryFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return LibraryFragment()
        }
    }


}

