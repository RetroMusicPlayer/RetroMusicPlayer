package io.github.muntashirakon.music.fragments.albums

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import com.bumptech.glide.Glide
import io.github.muntashirakon.music.EXTRA_ALBUM_ID
import io.github.muntashirakon.music.EXTRA_ARTIST_ID
import io.github.muntashirakon.music.R
import io.github.muntashirakon.music.activities.tageditor.AbsTagEditorActivity
import io.github.muntashirakon.music.activities.tageditor.AlbumTagEditorActivity
import io.github.muntashirakon.music.adapter.album.HorizontalAlbumAdapter
import io.github.muntashirakon.music.adapter.song.SimpleSongAdapter
import io.github.muntashirakon.music.dialogs.AddToPlaylistDialog
import io.github.muntashirakon.music.dialogs.DeleteSongsDialog
import io.github.muntashirakon.music.extensions.applyColor
import io.github.muntashirakon.music.extensions.show
import io.github.muntashirakon.music.fragments.base.AbsMainActivityFragment
import io.github.muntashirakon.music.glide.AlbumGlideRequest
import io.github.muntashirakon.music.glide.ArtistGlideRequest
import io.github.muntashirakon.music.glide.RetroMusicColoredTarget
import io.github.muntashirakon.music.helper.MusicPlayerRemote
import io.github.muntashirakon.music.helper.SortOrder
import io.github.muntashirakon.music.model.Album
import io.github.muntashirakon.music.model.Artist
import io.github.muntashirakon.music.network.model.LastFmAlbum
import io.github.muntashirakon.music.util.MusicUtil
import io.github.muntashirakon.music.util.PreferenceUtil
import io.github.muntashirakon.music.util.RetroUtil
import io.github.muntashirakon.music.util.color.MediaNotificationProcessor
import kotlinx.android.synthetic.main.fragment_album_content.*
import kotlinx.android.synthetic.main.fragment_album_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class AlbumDetailsFragment : AbsMainActivityFragment(R.layout.fragment_album_details),
    AlbumClickListener {

    private val arguments by navArgs<AlbumDetailsFragmentArgs>()
    private val detailsViewModel by viewModel<AlbumDetailsViewModel> {
        parametersOf(arguments.extraAlbumId)
    }

    private lateinit var simpleSongAdapter: SimpleSongAdapter
    private lateinit var album: Album

    private val savedSortOrder: String
        get() = PreferenceUtil.albumDetailSongSortOrder

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.hideBottomBarVisibility(false)
        mainActivity.addMusicServiceEventListener(detailsViewModel)
        mainActivity.setSupportActionBar(toolbar)

        toolbar.title = null

        postponeEnterTransition()
        detailsViewModel.getAlbum().observe(viewLifecycleOwner, Observer {
            showAlbum(it)
            startPostponedEnterTransition()
        })
        detailsViewModel.getArtist().observe(viewLifecycleOwner, Observer {
            loadArtistImage(it)
        })
        detailsViewModel.getMoreAlbums().observe(viewLifecycleOwner, Observer {
            moreAlbums(it)
        })
        detailsViewModel.getAlbumInfo().observe(viewLifecycleOwner, Observer {
            aboutAlbum(it)
        })
        setupRecyclerView()
        artistImage.setOnClickListener {
            requireActivity().findNavController(R.id.fragment_container)
                .navigate(
                    R.id.artistDetailsFragment,
                    bundleOf(EXTRA_ARTIST_ID to album.artistId)
                )
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

    override fun onDestroy() {
        super.onDestroy()
        playerActivity?.removeMusicServiceEventListener(detailsViewModel)
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
            HorizontalAlbumAdapter(requireActivity() as AppCompatActivity, albums, null, this)
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
        shuffleAction.applyColor(color.backgroundColor)
        playAction.applyColor(color.backgroundColor)
    }

    override fun onAlbumClick(albumId: Int, view: View) {
        findNavController().navigate(
            R.id.albumDetailsFragment,
            bundleOf(EXTRA_ALBUM_ID to albumId)
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_album_detail, menu)
        val sortOrder = menu.findItem(R.id.action_sort_order)
        setUpSortOrderMenu(sortOrder.subMenu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            toolbar,
            menu,
            getToolbarBackgroundColor(toolbar)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        var sortOrder: String? = null
        val songs = simpleSongAdapter.dataSet
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(songs).show(childFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_delete_from_device -> {
                DeleteSongsDialog.create(songs).show(childFragmentManager, "DELETE_SONGS")
                return true
            }
            R.id.action_tag_editor -> {
                val intent = Intent(requireContext(), AlbumTagEditorActivity::class.java)
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, album.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    requireActivity(),
                    albumCoverContainer,
                    "${getString(R.string.transition_album_art)}_${album.id}"
                )
                startActivityForResult(
                    intent,
                    TAG_EDITOR_REQUEST, options.toBundle()
                )
                return true
            }
            /*Sort*/
            R.id.action_sort_order_title -> sortOrder = SortOrder.AlbumSongSortOrder.SONG_A_Z
            R.id.action_sort_order_title_desc -> sortOrder = SortOrder.AlbumSongSortOrder.SONG_Z_A
            R.id.action_sort_order_track_list -> sortOrder =
                SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST
            R.id.action_sort_order_artist_song_duration ->
                sortOrder = SortOrder.AlbumSongSortOrder.SONG_DURATION
        }
        if (sortOrder != null) {
            item.isChecked = true
            setSaveSortOrder(sortOrder)
        }
        return true
    }

    private fun setUpSortOrderMenu(sortOrder: SubMenu) {
        when (savedSortOrder) {
            SortOrder.AlbumSongSortOrder.SONG_A_Z -> sortOrder.findItem(R.id.action_sort_order_title)
                .isChecked = true
            SortOrder.AlbumSongSortOrder.SONG_Z_A -> sortOrder.findItem(R.id.action_sort_order_title_desc)
                .isChecked = true
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST -> sortOrder.findItem(R.id.action_sort_order_track_list)
                .isChecked = true
            SortOrder.AlbumSongSortOrder.SONG_DURATION -> sortOrder.findItem(R.id.action_sort_order_artist_song_duration)
                .isChecked = true
        }
    }

    private fun setSaveSortOrder(sortOrder: String) {
        PreferenceUtil.albumDetailSongSortOrder = sortOrder
        when (sortOrder) {
            SortOrder.AlbumSongSortOrder.SONG_TRACK_LIST -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.trackNumber.compareTo(
                    o2.trackNumber
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_A_Z -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.title.compareTo(
                    o2.title
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_Z_A -> album.songs?.sortWith(Comparator { o1, o2 ->
                o2.title.compareTo(
                    o1.title
                )
            })
            SortOrder.AlbumSongSortOrder.SONG_DURATION -> album.songs?.sortWith(Comparator { o1, o2 ->
                o1.duration.compareTo(
                    o2.duration
                )
            })
        }
        album.songs?.let { simpleSongAdapter.swapDataSet(it) }
    }

    companion object {
        const val TAG_EDITOR_REQUEST = 9002
    }
}