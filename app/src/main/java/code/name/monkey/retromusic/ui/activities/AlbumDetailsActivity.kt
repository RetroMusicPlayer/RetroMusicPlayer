package code.name.monkey.retromusic.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.util.Pair
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import butterknife.ButterKnife
import butterknife.OnClick
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder
import code.name.monkey.retromusic.loaders.ArtistLoader
import code.name.monkey.retromusic.misc.AppBarStateChangeListener
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.mvp.contract.AlbumDetailsContract
import code.name.monkey.retromusic.mvp.presenter.AlbumDetailsPresenter
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.ui.activities.tageditor.AbsTagEditorActivity
import code.name.monkey.retromusic.ui.activities.tageditor.AlbumTagEditorActivity
import code.name.monkey.retromusic.ui.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.views.CircularImageView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album_content.*
import java.util.*

class AlbumDetailsActivity : AbsSlidingMusicPanelActivity(), AlbumDetailsContract.AlbumDetailsView {

    private var albumDetailsPresenter: AlbumDetailsPresenter? = null

    private var adapter: SimpleSongAdapter? = null
    var album: Album? = null
        private set

    private val savedSortOrder: String
        get() = PreferenceUtil.getInstance().albumDetailSongSortOrder

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_album)
    }

    private fun setupWindowTransition() {
        val slide = Slide(Gravity.BOTTOM)
        slide.interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        window.enterTransition = slide
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        setupWindowTransition()
        super.onCreate(savedInstanceState)
        ButterKnife.bind(this)

        toggleBottomNavigationView(true)
        setLightNavigationBar(true)
        setNavigationbarColorAuto()

        ActivityCompat.postponeEnterTransition(this)

        val albumId = intent.getIntExtra(EXTRA_ALBUM_ID, -1)
        albumDetailsPresenter = AlbumDetailsPresenter(this, albumId)
        albumDetailsPresenter!!.subscribe()

        setupRecyclerView()
        setupToolbarMarginHeight()


        contentContainer.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            run {
                if (scrollY > oldScrollY) {
                    actionShuffleAll!!.setShowTitle(false)
                }
                if (scrollY < oldScrollY) {
                    actionShuffleAll!!.setShowTitle(true)
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailsActivity)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = adapter
        }
    }

    private fun setupToolbarMarginHeight() {
        val primaryColor = ThemeStore.primaryColor(this)
        TintHelper.setTintAuto(contentContainer!!, primaryColor, true)
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout!!.setContentScrimColor(primaryColor)
            collapsingToolbarLayout!!.setStatusBarScrimColor(ColorUtil.darkenColor(primaryColor))
        }

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = null


        if (toolbar != null && !PreferenceUtil.getInstance().fullScreenMode) {
            val params = toolbar!!.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = RetroUtil.getStatusBarHeight()
            toolbar!!.layoutParams = params
        }

        if (appBarLayout != null) {
            appBarLayout!!.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                    val color: Int
                    when (state) {
                        AppBarStateChangeListener.State.COLLAPSED -> {
                            setLightStatusbar(ColorUtil.isColorLight(ThemeStore.primaryColor(this@AlbumDetailsActivity)))
                            color = ThemeStore.primaryColor(this@AlbumDetailsActivity)
                        }
                        AppBarStateChangeListener.State.EXPANDED, AppBarStateChangeListener.State.IDLE -> {
                            setLightStatusbar(false)
                            color = Color.TRANSPARENT
                        }
                        else -> {
                            setLightStatusbar(false)
                            color = Color.TRANSPARENT
                        }
                    }
                    ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(this@AlbumDetailsActivity, toolbar, color)
                }
            })
        }
    }

    @OnClick(R.id.action_shuffle_all, R.id.artist_image)
    fun onViewClicked(view: View) {
        when (view.id) {
            R.id.artist_image -> {
                val artistPairs = arrayOf<Pair<*, *>>(Pair.create(image, resources.getString(R.string.transition_artist_image)))
                NavigationUtil.goToArtist(this, album!!.artistId, *artistPairs)
            }
            R.id.action_shuffle_all -> if (album!!.songs != null) {
                MusicPlayerRemote.openAndShuffleQueue(album!!.songs!!, true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        albumDetailsPresenter!!.unsubscribe()
    }

    override fun loading() {

    }

    override fun showEmptyView() {

    }

    override fun completed() {
        ActivityCompat.startPostponedEnterTransition(this)
    }

    override fun showData(album: Album) {
        if (album.songs!!.isEmpty()) {
            finish()
            return
        }
        this.album = album

        albumTitle.text = album.title
        albumText.text = String.format("%s • %s • %s", album.artistName, MusicUtil.getYearString(album.year), MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(this, album.songs)))

        loadAlbumCover()
        loadMoreFrom(album)
        adapter!!.swapDataSet(album.songs)
    }

    private fun loadMoreFrom(album: Album) {
        if (artistImage != null) {
            ArtistGlideRequest.Builder.from(Glide.with(this),
                    ArtistLoader.getArtist(this, album.artistId).blockingFirst())
                    .forceDownload(false)
                    .generatePalette(this).build()
                    .dontAnimate()
                    .into(object : RetroMusicColoredTarget(artistImage as CircularImageView) {
                        override fun onColorReady(color: Int) {
                            //setColors(color);
                        }
                    })
        }

        val albums = ArtistLoader.getArtist(this, album.artistId)
                .blockingFirst().albums
        albums!!.remove(album)
        if (!albums.isEmpty()) {
            moreTitle.visibility = View.VISIBLE
            moreRecyclerView!!.visibility = View.VISIBLE
        } else {
            return
        }
        moreTitle.text = String.format("More from %s", album.artistName)

        val albumAdapter = HorizontalAlbumAdapter(this, albums, false, null)
        moreRecyclerView!!.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
        moreRecyclerView!!.adapter = albumAdapter
    }

    private fun loadAlbumCover() {
        SongGlideRequest.Builder.from(Glide.with(this), album!!.safeGetFirstSong())
                .checkIgnoreMediaStore(this)
                .generatePalette(this).build()
                .dontAnimate()
                .into(object : RetroMusicColoredTarget(image) {
                    override fun onColorReady(color: Int) {
                        setColors(color)
                    }
                })
    }

    private fun setColors(color: Int) {
        val themeColor = if (PreferenceUtil.getInstance().adaptiveColor) color else ThemeStore.accentColor(this)
        songTitle.setTextColor(themeColor)
        moreTitle.setTextColor(themeColor)
        actionShuffleAll.setColor(themeColor)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_album_detail, menu)
        val sortOrder = menu.findItem(R.id.action_sort_order)
        setUpSortOrderMenu(sortOrder.subMenu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        var sortOrder: String? = null
        val songs = adapter!!.dataSet
        when (item.itemId) {
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(songs).show(supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_delete_from_device -> {
                DeleteSongsDialog.create(songs).show(supportFragmentManager, "DELETE_SONGS")
                return true
            }
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
            R.id.action_tag_editor -> {
                val intent = Intent(this, AlbumTagEditorActivity::class.java)
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, album!!.id)
                startActivityForResult(intent, TAG_EDITOR_REQUEST)
                return true
            }
            R.id.action_go_to_artist -> {
                NavigationUtil.goToArtist(this, album!!.artistId)
                return true
            }
            /*Sort*/
            R.id.action_sort_order_title -> sortOrder = AlbumSongSortOrder.SONG_A_Z
            R.id.action_sort_order_title_desc -> sortOrder = AlbumSongSortOrder.SONG_Z_A
            R.id.action_sort_order_track_list -> sortOrder = AlbumSongSortOrder.SONG_TRACK_LIST
            R.id.action_sort_order_artist_song_duration -> sortOrder = AlbumSongSortOrder.SONG_DURATION
        }
        if (sortOrder != null) {
            item.isChecked = true
            setSaveSortOrder(sortOrder)
        }
        return true
    }

    private fun setUpSortOrderMenu(sortOrder: SubMenu) {
        when (savedSortOrder) {
            AlbumSongSortOrder.SONG_A_Z -> sortOrder.findItem(R.id.action_sort_order_title).isChecked = true
            AlbumSongSortOrder.SONG_Z_A -> sortOrder.findItem(R.id.action_sort_order_title_desc).isChecked = true
            AlbumSongSortOrder.SONG_TRACK_LIST -> sortOrder.findItem(R.id.action_sort_order_track_list).isChecked = true
            AlbumSongSortOrder.SONG_DURATION -> sortOrder.findItem(R.id.action_sort_order_artist_song_duration).isChecked = true
        }
    }

    private fun setSaveSortOrder(sortOrder: String?) {
        PreferenceUtil.getInstance().albumDetailSongSortOrder = sortOrder
        reload()
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        reload()
    }

    private fun reload() {
        albumDetailsPresenter!!.subscribe()
    }

    companion object {

        const val EXTRA_ALBUM_ID = "extra_album_id"
        private const val TAG_EDITOR_REQUEST = 2001
    }
}