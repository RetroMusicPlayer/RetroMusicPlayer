package code.name.monkey.retromusic.ui.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.util.Pair
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
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
import com.google.android.material.appbar.AppBarLayout
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album_content.*
import java.util.*

class AlbumDetailsActivity : AbsSlidingMusicPanelActivity(), AlbumDetailsContract.AlbumDetailsView {

    private lateinit var albumDetailsPresenter: AlbumDetailsPresenter
    private lateinit var simpleSongAdapter: SimpleSongAdapter
    private var disposable = CompositeDisposable()

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
        toggleBottomNavigationView(true)

        setLightNavigationBar(true)
        setNavigationbarColorAuto()


        ActivityCompat.postponeEnterTransition(this)

        val albumId = intent.getIntExtra(EXTRA_ALBUM_ID, -1)
        albumDetailsPresenter = AlbumDetailsPresenter(this, albumId)
        albumDetailsPresenter.subscribe()

        setupRecyclerView()
        setupToolbarMarginHeight()

        contentContainer.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            run {
                if (scrollY > oldScrollY) {
                    actionShuffleAll!!.setShowTitle(false)
                }
                if (scrollY < oldScrollY) {
                    actionShuffleAll!!.setShowTitle(true)
                }
            }
        }

        actionShuffleAll.setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(album!!.songs!!, true) }

        artistImage = findViewById(R.id.artistImage)
        artistImage.setOnClickListener {
            val artistPairs = arrayOf<Pair<*, *>>(Pair.create(image, resources.getString(R.string.transition_artist_image)))
            NavigationUtil.goToArtist(this, album!!.artistId, *artistPairs)
        }
    }

    private fun setupRecyclerView() {
        simpleSongAdapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song,false)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailsActivity)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = simpleSongAdapter
        }
    }

    private fun setupToolbarMarginHeight() {
        setSupportActionBar(toolbar)
        supportActionBar!!.title = null

        val primaryColor = ThemeStore.primaryColor(this)
        TintHelper.setTintAuto(contentContainer!!, primaryColor, true)

        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout!!.apply {
                setContentScrimColor(primaryColor)
                setStatusBarScrimColor(ColorUtil.darkenColor(primaryColor))
            }
        }

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)

        if (toolbar != null && !PreferenceUtil.getInstance().fullScreenMode) {
            val params = toolbar!!.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = RetroUtil.getStatusBarHeight()
            toolbar!!.layoutParams = params
        }

        appBarLayout?.apply {
            addOnOffsetChangedListener(object : AppBarStateChangeListener() {
                override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                    val color: Int = when (state) {
                        AppBarStateChangeListener.State.COLLAPSED -> {
                            setLightStatusbar(ColorUtil.isColorLight(ThemeStore.primaryColor(this@AlbumDetailsActivity)))
                            ThemeStore.primaryColor(this@AlbumDetailsActivity)
                        }
                        AppBarStateChangeListener.State.EXPANDED, AppBarStateChangeListener.State.IDLE -> {
                            setLightStatusbar(false)
                            Color.TRANSPARENT
                        }
                    }
                    ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(this@AlbumDetailsActivity, toolbar, color)
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        albumDetailsPresenter.unsubscribe()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }

    override fun loading() {

    }

    override fun showEmptyView() {

    }

    override fun completed() {
        ActivityCompat.startPostponedEnterTransition(this)
    }

    override fun showData(list: Album) {
        if (list.songs!!.isEmpty()) {
            finish()
            return
        }
        this.album = list

        albumTitle.text = list.title
        albumText.text = String.format("%s • %s • %s", list.artistName, MusicUtil.getYearString(list.year), MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(this, list.songs)))

        loadAlbumCover()
        loadMoreFrom(list)

        simpleSongAdapter.swapDataSet(list.songs)
    }

    private lateinit var artistImage: ImageView

    private fun loadMoreFrom(album: Album) {
        disposable.add(ArtistLoader.getArtist(this, album.artistId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    GlideApp.with(this@AlbumDetailsActivity)
                            .asBitmapPalette()
                            .load(RetroGlideExtension.getArtistModel(it))
                            .transition(RetroGlideExtension.getDefaultTransition())
                            .artistOptions(it)
                            .dontAnimate()
                            .into(object : RetroMusicColoredTarget(artistImage) {
                                override fun onColorReady(color: Int) {

                                }
                            })
                    return@map it.albums!!
                }

                .subscribe {

                    it.remove(album)
                    if (!it.isEmpty()) {
                        moreTitle.visibility = View.VISIBLE
                        moreRecyclerView.visibility = View.VISIBLE
                    } else {
                        return@subscribe
                    }
                    moreTitle.text = String.format("More from %s", album.artistName)

                    val albumAdapter = HorizontalAlbumAdapter(this, it, false, null)
                    moreRecyclerView.layoutManager = GridLayoutManager(this, 1, GridLayoutManager.HORIZONTAL, false)
                    moreRecyclerView.adapter = albumAdapter

                })
    }

    private fun loadAlbumCover() {
        GlideApp.with(this)
                .asBitmapPalette()
                .load(RetroGlideExtension.getSongModel(album!!.safeGetFirstSong()))
                .transition(RetroGlideExtension.getDefaultTransition())
                .songOptions(album!!.safeGetFirstSong())
                .dontAnimate()
                .into(object : RetroMusicColoredTarget(image as ImageView) {
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
        val songs = simpleSongAdapter.dataSet
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
        albumDetailsPresenter.subscribe()
    }

    companion object {

        const val EXTRA_ALBUM_ID = "extra_album_id"
        private const val TAG_EDITOR_REQUEST = 2001
    }
}