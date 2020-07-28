package code.name.monkey.retromusic.activities.artists

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Spanned
import android.transition.Slide
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.MaterialUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.base.AbsSlidingMusicPanelActivity
import code.name.monkey.retromusic.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.adapter.song.SimpleSongAdapter
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.extensions.extraNotNull
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.network.model.LastFmArtist
import code.name.monkey.retromusic.util.CustomArtistImageUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import com.afollestad.materialcab.MaterialCab
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_artist_content.*
import kotlinx.android.synthetic.main.activity_artist_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList

class ArtistDetailActivity : AbsSlidingMusicPanelActivity(), CabHolder {
    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            if (it.isActive) it.finish()
        }
        cab = MaterialCab(this, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
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

    private var cab: MaterialCab? = null
    private var biography: Spanned? = null
    private lateinit var artist: Artist
    private lateinit var songAdapter: SimpleSongAdapter
    private lateinit var albumAdapter: HorizontalAlbumAdapter
    private var forceDownload: Boolean = false
    private val detailsViewModel: ArtistDetailsViewModel by viewModel {
        parametersOf(extraNotNull<Int>(EXTRA_ARTIST_ID).value)
    }

    override fun createContentView(): View {
        return wrapSlidingMusicPanel(R.layout.activity_artist_details)
    }

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
        setStatusbarColorAuto()
        setNavigationbarColorAuto()
        setTaskDescriptionColorAuto()
        setLightNavigationBar(true)
        setBottomBarVisibility(View.GONE)
        window.sharedElementsUseOverlay = true
        windowEnterTransition()
        toolbar.setBackgroundColor(surfaceColor())

        addMusicServiceEventListener(detailsViewModel)

        ActivityCompat.postponeEnterTransition(this)
        detailsViewModel.getArtist().observe(this, androidx.lifecycle.Observer {
            ActivityCompat.startPostponedEnterTransition(this@ArtistDetailActivity)
            artist(it)
        })
        detailsViewModel.getArtistInfo().observe(this, androidx.lifecycle.Observer {
            artistInfo(it)
        })
        setupRecyclerView()
        playAction.apply {
            setOnClickListener { MusicPlayerRemote.openQueue(artist.songs, 0, true) }
        }
        shuffleAction.apply {
            setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(artist.songs, true) }
        }

        biographyText.setOnClickListener {
            if (biographyText.maxLines == 4) {
                biographyText.maxLines = Integer.MAX_VALUE
            } else {
                biographyText.maxLines = 4
            }
        }
    }

    private fun setupRecyclerView() {
        albumAdapter = HorizontalAlbumAdapter(this, ArrayList(), null)
        albumRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(this.context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
        songAdapter = SimpleSongAdapter(this, ArrayList(), R.layout.item_song, this)
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
                data?.data?.let {
                    CustomArtistImageUtil.getInstance(this).setCustomArtistImage(artist, it)
                }
            }
            else -> if (resultCode == Activity.RESULT_OK) {
                //reload()
            }
        }
    }

    fun artist(artist: Artist) {
        if (artist.songs.isEmpty()) {
            finish()
        }
        this.artist = artist
        loadArtistImage(artist)
        if (RetroUtil.isAllowedToDownloadMetadata(this)) {
            loadBiography(artist.name)
        }
        artistTitle.text = artist.name
        text.text = String.format(
            "%s • %s",
            MusicUtil.getArtistInfoString(this, artist),
            MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(artist.songs))
        )
        val songText =
            resources.getQuantityString(
                R.plurals.albumSongs,
                artist.songCount,
                artist.songCount
            )
        val albumText =
            resources.getQuantityString(
                R.plurals.albums,
                artist.songCount,
                artist.songCount
            )
        songTitle.text = songText
        albumTitle.text = albumText
        songAdapter.swapDataSet(artist.songs)
        artist.albums?.let { albumAdapter.swapDataSet(it) }
    }

    private fun loadBiography(
        name: String,
        lang: String? = Locale.getDefault().language
    ) {
        biography = null
        this.lang = lang
        detailsViewModel.loadBiography(name, lang, null)
    }

    private fun artistInfo(lastFmArtist: LastFmArtist?) {
        if (lastFmArtist != null && lastFmArtist.artist != null) {
            val bioContent = lastFmArtist.artist.bio.content
            if (bioContent != null && bioContent.trim { it <= ' ' }.isNotEmpty()) {
                biographyText.visibility = View.VISIBLE
                biographyTitle.visibility = View.VISIBLE
                biography = HtmlCompat.fromHtml(bioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
                biographyText.text = biography
                if (lastFmArtist.artist.stats.listeners.isNotEmpty()) {
                    listeners.show()
                    listenersLabel.show()
                    scrobbles.show()
                    scrobblesLabel.show()
                    listeners.text =
                        RetroUtil.formatValue(lastFmArtist.artist.stats.listeners.toFloat())
                    scrobbles.text =
                        RetroUtil.formatValue(lastFmArtist.artist.stats.playcount.toFloat())
                }
            }
        }

        // If the "lang" parameter is set and no biography is given, retry with default language
        if (biography == null && lang != null) {
            loadBiography(artist.name, null)
        }
    }

    private var lang: String? = null

    private fun loadArtistImage(artist: Artist) {
        ArtistGlideRequest.Builder.from(Glide.with(this), artist)
            .generatePalette(this).build()
            .dontAnimate().into(object : RetroMusicColoredTarget(image) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(colors)
                }
            })
    }

    private fun setColors(color: MediaNotificationProcessor) {
        MaterialUtil.tintColor(
            button = shuffleAction,
            textColor = color.primaryTextColor,
            backgroundColor = color.backgroundColor
        )
        MaterialUtil.tintColor(
            button = playAction,
            textColor = color.primaryTextColor,
            backgroundColor = color.backgroundColor
        )
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        val songs = artist.songs
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
                startActivityForResult(
                    Intent.createChooser(intent, getString(R.string.pick_from_local_storage)),
                    REQUEST_CODE_SELECT_IMAGE
                )
                return true
            }
            R.id.action_reset_artist_image -> {
                Toast.makeText(
                    this@ArtistDetailActivity,
                    resources.getString(R.string.updating),
                    Toast.LENGTH_SHORT
                )
                    .show()
                CustomArtistImageUtil.getInstance(this@ArtistDetailActivity)
                    .resetCustomArtistImage(artist)
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

    override fun onBackPressed() {
        if (cab != null && cab!!.isActive) {
            cab?.finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        removeMusicServiceEventListener(detailsViewModel)
    }

    companion object {
        const val EXTRA_ARTIST_ID = "extra_artist_id"
        const val REQUEST_CODE_SELECT_IMAGE = 9003
    }
}
