package io.github.muntashirakon.music.activities.albums

import android.app.ActivityOptions
import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.util.MaterialUtil
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.adapter.album.HorizontalAlbumAdapter
import io.github.muntashirakon.music.adapter.song.SimpleSongAdapter
import io.github.muntashirakon.music.extensions.extraNotNull
import io.github.muntashirakon.music.extensions.show
import io.github.muntashirakon.music.fragments.base.AbsMusicServiceFragment
import io.github.muntashirakon.music.glide.AlbumGlideRequest
import io.github.muntashirakon.music.glide.ArtistGlideRequest
import io.github.muntashirakon.music.glide.RetroMusicColoredTarget
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.network.model.LastFmAlbum
import io.github.muntashirakon.music.util.MusicUtil
import io.github.muntashirakon.music.util.NavigationUtil
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.RetroUtil
import io.github.muntashirakon.music.util.color.MediaNotificationProcessor
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_album.*
import kotlinx.android.synthetic.main.activity_album_content.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class AlbumDetailsFragment : AbsMusicServiceFragment(R.layout.fragment_album_details) {
    private lateinit var simpleSongAdapter: SimpleSongAdapter
    private lateinit var album: Album
    private val savedSortOrder: String
        get() = PreferenceUtil.albumDetailSongSortOrder
    private val detailsViewModel by viewModel<AlbumDetailsViewModel> {
        parametersOf(extraNotNull<Int>(AlbumDetailsActivity.EXTRA_ALBUM_ID).value)
    }

    private fun setSharedElementTransitionOnEnter() {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.change_bounds)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setSharedElementTransitionOnEnter()
        postponeEnterTransition()
        playerActivity?.addMusicServiceEventListener(detailsViewModel)

        detailsViewModel.getAlbum().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            startPostponedEnterTransition()
            showAlbum(it)
        })
        detailsViewModel.getArtist().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            loadArtistImage(it)
        })
        detailsViewModel.getMoreAlbums().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            moreAlbums(it)
        })
        detailsViewModel.getAlbumInfo().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            aboutAlbum(it)
        })
        setupRecyclerView()
        artistImage.setOnClickListener {
            val artistPairs = ActivityOptions.makeSceneTransitionAnimation(
                requireActivity(),
                Pair.create(
                    artistImage,
                    getString(R.string.transition_artist_image)
                )
            )
            NavigationUtil.goToArtistOptions(requireActivity(), album.artistId, artistPairs)
        }
        playAction.setOnClickListener { MusicPlayerRemote.openQueue(album.songs!!, 0, true) }

        shuffleAction.setOnClickListener {
            MusicPlayerRemote.openAndShuffleQueue(
                album.songs!!,
                true
            )
        }

        aboutAlbumText.setOnClickListener {
            if (aboutAlbumText.maxLines == 4) {
                aboutAlbumText.maxLines = Integer.MAX_VALUE
            } else {
                aboutAlbumText.maxLines = 4
            }
        }
        image.apply {
            transitionName = getString(R.string.transition_album_art)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        playerActivity?.removeMusicServiceEventListener(detailsViewModel)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        simpleSongAdapter = SimpleSongAdapter(
            requireActivity() as AppCompatActivity,
            ArrayList(),
            R.layout.item_song,
            null
        )
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = simpleSongAdapter
        }
    }

    private fun showAlbum(album: Album) {
        if (album.songs!!.isEmpty()) {
            return
        }
        this.album = album

        albumTitle.text = album.title
        val songText =
            resources.getQuantityString(
                R.plurals.albumSongs,
                album.songCount,
                album.songCount
            )
        songTitle.text = songText

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
        detailsViewModel.loadArtist(album.artistId)
        detailsViewModel.loadAlbumInfo(album)
    }

    private fun moreAlbums(albums: List<Album>) {
        moreTitle.show()
        moreRecyclerView.show()
        moreTitle.text = String.format(getString(R.string.label_more_from), album.artistName)

        val albumAdapter =
            HorizontalAlbumAdapter(requireActivity() as AppCompatActivity, albums, null)
        moreRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.HORIZONTAL,
            false
        )
        moreRecyclerView.adapter = albumAdapter
    }

    private fun aboutAlbum(lastFmAlbum: LastFmAlbum) {
        if (lastFmAlbum.album != null) {
            if (lastFmAlbum.album.wiki != null) {
                aboutAlbumText.show()
                aboutAlbumTitle.show()
                aboutAlbumTitle.text =
                    String.format(getString(R.string.about_album_label), lastFmAlbum.album.name)
                aboutAlbumText.text = lastFmAlbum.album.wiki.content
            }
            if (lastFmAlbum.album.listeners.isNotEmpty()) {
                listeners.show()
                listenersLabel.show()
                scrobbles.show()
                scrobblesLabel.show()

                listeners.text = RetroUtil.formatValue(lastFmAlbum.album.listeners.toFloat())
                scrobbles.text = RetroUtil.formatValue(lastFmAlbum.album.playcount.toFloat())
            }
        }
    }

    private fun loadArtistImage(artist: Artist) {
        ArtistGlideRequest.Builder.from(Glide.with(requireContext()), artist)
            .generatePalette(requireContext())
            .build()
            .dontAnimate()
            .dontTransform()
            .into(object : RetroMusicColoredTarget(artistImage) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                }
            })
    }

    private fun loadAlbumCover() {
        AlbumGlideRequest.Builder.from(Glide.with(requireContext()), album.safeGetFirstSong())
            .checkIgnoreMediaStore(requireContext())
            .ignoreMediaStore(PreferenceUtil.isIgnoreMediaStoreArtwork)
            .generatePalette(requireContext())
            .build()
            .dontAnimate()
            .dontTransform()
            .into(object : RetroMusicColoredTarget(image) {
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
    }
}