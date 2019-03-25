package code.name.monkey.retromusic.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.transition.Slide
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.misc.AppBarStateChangeListener
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.mvp.contract.ArtistDetailContract
import code.name.monkey.retromusic.mvp.presenter.ArtistDetailsPresenter
import code.name.monkey.retromusic.rest.LastFMRestClient
import code.name.monkey.retromusic.rest.model.LastFmArtist
import code.name.monkey.retromusic.ui.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.ui.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.ui.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.ui.adapter.song.SimpleSongAdapter
import code.name.monkey.retromusic.util.*
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_artist_content.*
import kotlinx.android.synthetic.main.activity_artist_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ArtistDetailActivity : AbsSlidingMusicPanelActivity(), ArtistDetailContract.ArtistsDetailsView {

    private var biography: Spanned? = null
    private var artist: Artist? = null
    private var lastFMRestClient: LastFMRestClient? = null
    private var artistDetailsPresenter: ArtistDetailsPresenter? = null
    private lateinit var songAdapter: SimpleSongAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private var forceDownload: Boolean = false

    private fun setupWindowTransitions() {
        val slide = Slide(Gravity.BOTTOM)
        slide.interpolator = AnimationUtils.loadInterpolator(this, android.R.interpolator.linear_out_slow_in)
        window.enterTransition = slide
    }


    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_artist_details)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setDrawUnderStatusBar()
        setupWindowTransitions()
        super.onCreate(savedInstanceState)


        toggleBottomNavigationView(true)
        setNavigationbarColorAuto()
        setLightNavigationBar(true)

        ActivityCompat.postponeEnterTransition(this)

        lastFMRestClient = LastFMRestClient(this)

        setUpViews()

        artistDetailsPresenter = ArtistDetailsPresenter(this, intent.extras!!)
        artistDetailsPresenter!!.subscribe()

        contentContainer.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            run {
                if (scrollY > oldScrollY) {
                    actionShuffleAll.shrink(true)
                }
                if (scrollY < oldScrollY) {
                    actionShuffleAll.extend(true)
                }
            }
        }

        biographyText.setOnClickListener {
            if (biographyText.maxLines == 4) {
                biographyText.maxLines = Integer.MAX_VALUE
            } else {
                biographyText.maxLines = 4
            }
        }
        actionShuffleAll.setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(getArtist().songs, true) }
    }

    private fun setUpViews() {
        setupRecyclerView()
        setupToolbarMarginHeight()
        setupContainerHeight()
    }

    private fun setupContainerHeight() {
        if (imageContainer != null) {
            val params = imageContainer!!.layoutParams
            params.width = DensityUtil.getScreenHeight(this) / 2
            imageContainer!!.layoutParams = params
        }
    }

    private fun setupToolbarMarginHeight() {
        val primaryColor = ThemeStore.primaryColor(this)
        TintHelper.setTintAuto(contentContainer!!, primaryColor, true)
        if (collapsingToolbar != null) {
            collapsingToolbar!!.setContentScrimColor(primaryColor)
            collapsingToolbar!!.setStatusBarScrimColor(ColorUtil.darkenColor(primaryColor))
        }

        toolbar!!.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = null


        if (toolbar != null && !PreferenceUtil.getInstance().fullScreenMode) {
            val params = toolbar!!.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin = RetroUtil.getStatusBarHeight()
            toolbar!!.layoutParams = params
        }

        appBarLayout?.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout, state: AppBarStateChangeListener.State) {
                val color: Int = when (state) {
                    AppBarStateChangeListener.State.COLLAPSED -> {
                        setLightStatusbar(ColorUtil.isColorLight(ThemeStore.primaryColor(appBarLayout.context)))
                        ThemeStore.primaryColor(appBarLayout.context)
                    }
                    AppBarStateChangeListener.State.EXPANDED, AppBarStateChangeListener.State.IDLE -> {
                        setLightStatusbar(false)
                        Color.TRANSPARENT
                    }

                }
                ToolbarContentTintHelper.setToolbarContentColorBasedOnToolbarColor(appBarLayout.context, toolbar, color)
            }
        })
    }

    private fun setupRecyclerView() {
        albumAdapter = HorizontalAlbumAdapter(this, ArrayList(), false, null)
        albumRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(this.context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
        songAdapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song, false)
        recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SELECT_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                CustomArtistImageUtil.getInstance(this).setCustomArtistImage(artist!!, data!!.data!!)
            }
            else -> if (resultCode == Activity.RESULT_OK) {
                reload()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        artistDetailsPresenter!!.unsubscribe()
    }

    override fun loading() {}

    override fun showEmptyView() {

    }

    override fun completed() {
        ActivityCompat.startPostponedEnterTransition(this)
    }

    override fun showData(list: Artist) {
        setArtist(list)
    }

    private fun getArtist(): Artist {
        if (artist == null) {
            artist = Artist()
        }
        return this.artist!!
    }

    private fun setArtist(artist: Artist) {
        if (artist.songCount <= 0) {
            finish()
        }
        this.artist = artist
        loadArtistImage()

        if (RetroUtil.isAllowedToDownloadMetadata(this)) {
            loadBiography()
        }
        artistTitle.text = artist.name
        text.text = String.format("%s • %s", MusicUtil.getArtistInfoString(this, artist), MusicUtil
                .getReadableDurationString(MusicUtil.getTotalDuration(this, artist.songs)))
        //val songs = artist.songs.sortedWith(compareBy { it.title }) as ArrayList<Song>
        songAdapter.swapDataSet(artist.songs)

        //val albums = artist.albums?.sortedWith(compareBy { it.artistName }) as ArrayList<Album>
        albumAdapter.swapDataSet(artist.albums!!)
    }

    private fun loadBiography(lang: String? = Locale.getDefault().language) {
        biography = null

        lastFMRestClient!!.apiService
                .getArtistInfo(getArtist().name, lang, null)
                .enqueue(object : Callback<LastFmArtist> {
                    override fun onResponse(call: Call<LastFmArtist>,
                                            response: Response<LastFmArtist>) {
                        val lastFmArtist = response.body()
                        if (lastFmArtist != null && lastFmArtist.artist != null) {
                            val bioContent = lastFmArtist.artist.bio.content
                            if (bioContent != null && !bioContent.trim { it <= ' ' }.isEmpty()) {
                                //TransitionManager.beginDelayedTransition(titleContainer);
                                biographyText.visibility = View.VISIBLE
                                biographyTitle.visibility = View.VISIBLE
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    biography = Html.fromHtml(bioContent, Html.FROM_HTML_MODE_LEGACY)
                                } else {
                                    biography = Html.fromHtml(bioContent)
                                }
                                biographyText!!.text = biography
                            }
                        }

                        // If the "lang" parameter is set and no biography is given, retry with default language
                        if (biography == null && lang != null) {
                            loadBiography(null)
                        }
                    }

                    override fun onFailure(call: Call<LastFmArtist>, t: Throwable) {
                        t.printStackTrace()
                        biography = null
                    }
                })
    }


    private fun loadArtistImage() {
        GlideApp.with(this)
                .asBitmapPalette()
                .load(RetroGlideExtension.getArtistModel(artist!!, forceDownload))
                .transition(RetroGlideExtension.getDefaultTransition())
                .artistOptions(artist)
                .dontAnimate()
                .into(object : RetroMusicColoredTarget(artistImage) {
                    override fun onColorReady(color: Int) {
                        setColors(color)
                    }
                })
        forceDownload = false;
    }

    private fun setColors(color: Int) {

        val textColor = if (PreferenceUtil.getInstance().adaptiveColor) color else ThemeStore.accentColor(this)

        albumTitle.setTextColor(textColor)
        songTitle.setTextColor(textColor)
        biographyTitle.setTextColor(textColor)

        actionShuffleAll.backgroundTintList = ColorStateList.valueOf(textColor)
        ColorStateList.valueOf(MaterialValueHelper.getPrimaryTextColor(this, ColorUtil.isColorLight(textColor))).apply {
            actionShuffleAll.setTextColor(this)
            actionShuffleAll.iconTint = this
        }

        findViewById<View>(R.id.root).setBackgroundColor(ThemeStore.primaryColor(this))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        val songs = getArtist().songs
        when (item.itemId) {
            android.R.id.home -> {
                super.onBackPressed()
                return true
            }
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
            R.id.action_set_artist_image -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(Intent.createChooser(intent, getString(R.string.pick_from_local_storage)), REQUEST_CODE_SELECT_IMAGE)
                return true
            }
            R.id.action_reset_artist_image -> {
                Toast.makeText(this@ArtistDetailActivity, resources.getString(R.string.updating),
                        Toast.LENGTH_SHORT).show()
                CustomArtistImageUtil.getInstance(this@ArtistDetailActivity).resetCustomArtistImage(artist!!)
                forceDownload = true
                return true
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_artist_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        reload()
    }

    private fun reload() {
        artistDetailsPresenter!!.unsubscribe()
        artistDetailsPresenter!!.subscribe()
    }

    companion object {

        const val EXTRA_ARTIST_ID = "extra_artist_id"
        const val REQUEST_CODE_SELECT_IMAGE = 9003
    }
}
