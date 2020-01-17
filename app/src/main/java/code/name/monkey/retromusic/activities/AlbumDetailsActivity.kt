package code.name.monkey.retromusic.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.App
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.activities.tageditor.AbsTagEditorActivity
import code.name.monkey.retromusic.activities.tageditor.AlbumTagEditorActivity
import code.name.monkey.retromusic.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.adapter.song.SimpleSongAdapter
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.dialogs.DeleteSongsDialog
import code.name.monkey.retromusic.extensions.ripAlpha
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.presenter.AlbumDetailsPresenter
import code.name.monkey.retromusic.mvp.presenter.AlbumDetailsView
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import com.afollestad.materialcab.MaterialCab
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_album.albumCoverContainer
import kotlinx.android.synthetic.main.activity_album.albumText
import kotlinx.android.synthetic.main.activity_album.albumTitle
import kotlinx.android.synthetic.main.activity_album.container
import kotlinx.android.synthetic.main.activity_album.image
import kotlinx.android.synthetic.main.activity_album.toolbar
import kotlinx.android.synthetic.main.activity_album_content.moreRecyclerView
import kotlinx.android.synthetic.main.activity_album_content.moreTitle
import kotlinx.android.synthetic.main.activity_album_content.playAction
import kotlinx.android.synthetic.main.activity_album_content.recyclerView
import kotlinx.android.synthetic.main.activity_album_content.shuffleAction
import kotlinx.android.synthetic.main.activity_album_content.songTitle
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper
import java.util.ArrayList
import javax.inject.Inject
import android.util.Pair as UtilPair

class AlbumDetailsActivity : AbsSlidingMusicPanelActivity(), AlbumDetailsView, CabHolder {
    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            if (it.isActive) it.finish()
        }
        cab = MaterialCab(this, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close_white_24dp)
            .setBackgroundColor(
                RetroColorUtil.shiftBackgroundColorForLightText(
                    ATHUtil.resolveColor(
                        this,
                        R.attr.colorSurface
                    )
                )
            )
            .start(callback)
        return cab as MaterialCab
    }

    private lateinit var simpleSongAdapter: SimpleSongAdapter
    private lateinit var album: Album
    private lateinit var artistImage: ImageView
    private var cab: MaterialCab? = null
    private val savedSortOrder: String
        get() = PreferenceUtil.getInstance(this).albumDetailSongSortOrder

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_album)
    }

    @Inject
    lateinit var albumDetailsPresenter: AlbumDetailsPresenter

    private fun windowEnterTransition() {
        val slide = Slide()
        slide.excludeTarget(R.id.appBarLayout, true)
        slide.excludeTarget(R.id.status_bar, true)
        slide.excludeTarget(android.R.id.statusBarBackground, true)
        slide.excludeTarget(android.R.id.navigationBarBackground, true)

        window.enterTransition = slide
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        super.onCreate(savedInstanceState)
        toggleBottomNavigationView(true)
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)
        window.sharedElementsUseOverlay = true

        App.musicComponent.inject(this)
        albumDetailsPresenter.attachView(this)

        if (intent.extras!!.containsKey(EXTRA_ALBUM_ID)) {
            intent.extras?.getInt(EXTRA_ALBUM_ID)?.let {
                albumDetailsPresenter.loadAlbum(it)
                albumCoverContainer?.transitionName = "${getString(R.string.transition_album_art)}_$it"
            }
        } else {
            finish()
        }

        windowEnterTransition()
        ActivityCompat.postponeEnterTransition(this)


        artistImage = findViewById(R.id.artistImage)

        setupRecyclerView()

        artistImage.setOnClickListener {
            val artistPairs = ActivityOptions.makeSceneTransitionAnimation(
                this,
                UtilPair.create(
                    artistImage,
                    "${getString(R.string.transition_artist_image)}_${album.artistId}"
                )
            )
            NavigationUtil.goToArtistOptions(this, album.artistId, artistPairs)
        }
        playAction.apply {
            setOnClickListener { MusicPlayerRemote.openQueue(album.songs!!, 0, true) }
        }
        shuffleAction.apply {
            setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(album.songs!!, true) }
        }
    }

    private fun setupRecyclerView() {
        simpleSongAdapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song, this)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@AlbumDetailsActivity)
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = simpleSongAdapter
        }

        OverScrollDecoratorHelper.setUpOverScroll(container)
    }

    override fun onDestroy() {
        super.onDestroy()
        albumDetailsPresenter.detachView()
    }

    override fun complete() {
        ActivityCompat.startPostponedEnterTransition(this)
    }

    override fun album(album: Album) {
        complete()
        if (album.songs!!.isEmpty()) {
            finish()
            return
        }
        this.album = album

        albumTitle.text = album.title
        if (MusicUtil.getYearString(album.year) == "-") {
            albumText.text = String.format(
                "%s • %s",
                album.artistName,
                MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(album.songs))
            )
        } else {
            albumText.text = String.format(
                "%s • %s • %s",
                album.artistName,
                MusicUtil.getYearString(album.year),
                MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(album.songs))
            )
        }
        loadAlbumCover()
        simpleSongAdapter.swapDataSet(album.songs)
        albumDetailsPresenter.loadMore(album.artistId)
    }

    override fun moreAlbums(albums: ArrayList<Album>) {
        moreTitle.show()
        moreRecyclerView.show()
        moreTitle.text = String.format(getString(R.string.label_more_from), album.artistName)

        val albumAdapter = HorizontalAlbumAdapter(this, albums, false, null)
        moreRecyclerView.layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.HORIZONTAL,
            false
        )
        moreRecyclerView.adapter = albumAdapter
    }

    override fun loadArtistImage(artist: Artist) {
        ArtistGlideRequest.Builder.from(Glide.with(this), artist).generatePalette(this).build()
            .dontAnimate().dontTransform().into(object : RetroMusicColoredTarget(artistImage) {
                override fun onColorReady(color: Int) {
                }
            })
    }

    private fun loadAlbumCover() {
        SongGlideRequest.Builder.from(Glide.with(this), album.safeGetFirstSong())
            .checkIgnoreMediaStore(this)
            .ignoreMediaStore(PreferenceUtil.getInstance(this).ignoreMediaStoreArtwork())
            .generatePalette(this)
            .build().dontAnimate().dontTransform()
            .into(object : RetroMusicColoredTarget(image) {
                override fun onColorReady(color: Int) {
                    setColors(color)
                }
            })
    }

    private fun setColors(color: Int) {
        val themeColor = if (PreferenceUtil.getInstance(this).adaptiveColor) color.ripAlpha()
        else ThemeStore.accentColor(this)

        songTitle.setTextColor(themeColor)
        moreTitle.setTextColor(themeColor)

        val buttonColor = if (PreferenceUtil.getInstance(this).adaptiveColor)
            color.ripAlpha()
        else
            ATHUtil.resolveColor(this, R.attr.colorSurface)

        MaterialUtil.setTint(button = shuffleAction, color = buttonColor)
        MaterialUtil.setTint(button = playAction, color = buttonColor)

        val toolbarColor = ATHUtil.resolveColor(this, R.attr.colorSurface)
        //status_bar.setBackgroundColor(toolbarColor)
        toolbar.setBackgroundColor(toolbarColor)
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_album_detail, menu)
        val sortOrder = menu.findItem(R.id.action_sort_order)
        setUpSortOrderMenu(sortOrder.subMenu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            this,
            toolbar,
            menu,
            getToolbarBackgroundColor(toolbar)
        )
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
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, album.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    this,
                    albumCoverContainer,
                    "${getString(R.string.transition_album_art)}_${album.id}"
                )
                startActivityForResult(intent, TAG_EDITOR_REQUEST, options.toBundle())
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
            AlbumSongSortOrder.SONG_TRACK_LIST -> sortOrder.findItem(R.id.action_sort_order_track_list).isChecked =
                true
            AlbumSongSortOrder.SONG_DURATION -> sortOrder.findItem(R.id.action_sort_order_artist_song_duration)
                .isChecked = true
        }
    }

    private fun setSaveSortOrder(sortOrder: String?) {
        PreferenceUtil.getInstance(this).albumDetailSongSortOrder = sortOrder
        reload()
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        reload()
    }

    private fun reload() {
        if (intent.extras!!.containsKey(EXTRA_ALBUM_ID)) {
            intent.extras?.getInt(EXTRA_ALBUM_ID)?.let { albumDetailsPresenter.loadAlbum(it) }
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        if (cab != null && cab!!.isActive) {
            cab?.finish()
        } else {
            super.onBackPressed()
        }
    }

    companion object {

        const val EXTRA_ALBUM_ID = "extra_album_id"
        private const val TAG_EDITOR_REQUEST = 2001
    }
}