/*
 * Copyright (c) 2020 Hemanth Savarla.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 */
package code.name.monkey.retromusic.feature.details.album.presentation

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.EXTRA_ALBUM_ID
import code.name.monkey.retromusic.EXTRA_ARTIST_ID
import code.name.monkey.retromusic.EXTRA_ARTIST_NAME
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.activities.tageditor.AbsTagEditorActivity
import code.name.monkey.retromusic.activities.tageditor.AlbumTagEditorActivity
import code.name.monkey.retromusic.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.databinding.FragmentAlbumDetailsBinding
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.SingleColorTarget
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder.Companion.SONG_A_Z
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder.Companion.SONG_DURATION
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder.Companion.SONG_TRACK_LIST
import code.name.monkey.retromusic.helper.SortOrder.AlbumSongSortOrder.Companion.SONG_Z_A
import code.name.monkey.retromusic.interfaces.IAlbumClickListener
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.network.model.LastFmAlbum
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.Collator

class AlbumDetailsFragment : AbsMainActivityFragment(R.layout.fragment_album_details), IAlbumClickListener {

    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!

    private val arguments by navArgs<AlbumDetailsFragmentArgs>()
    private val detailsViewModel by viewModel<AlbumDetailsViewModel> {
        parametersOf(arguments.extraAlbumId)
    }

    private lateinit var simpleSongAdapter: AlbumDetailsSongAdapter
    private lateinit var albumDetails: AlbumDetailsUi
    private var albumArtistExists = false

    private val savedSortOrder: String
        get() = PreferenceUtil.albumDetailSongSortOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(surfaceColor())
            setPathMotion(MaterialArcMotion())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAlbumDetailsBinding.bind(view)
        mainActivity.setSupportActionBar(binding.toolbar)

        binding.toolbar.title = " "
        binding.albumCoverContainer.transitionName = arguments.extraAlbumId.toString()
        postponeEnterTransition()
        lifecycleScope.launchWhenStarted {
            detailsViewModel.albumDetails.collect { album ->
                view.doOnPreDraw {
                    startPostponedEnterTransition()
                }
                albumArtistExists = !album.artist.title.isNullOrEmpty()
                showAlbum(album)
                binding.artistImage.transitionName = if (albumArtistExists) {
                    album.artist.title
                } else {
                    album.artist.id
                }
            }
        }


        setupRecyclerView()
        binding.artistImage.setOnClickListener { artistView ->
            if (albumArtistExists) {
                findActivityNavController(R.id.fragment_container)
                    .navigate(
                        R.id.albumArtistDetailsFragment,
                        bundleOf(EXTRA_ARTIST_NAME to albumDetails.artist.title),
                        null,
                        FragmentNavigatorExtras(artistView to albumDetails.artist.title)
                    )
            } else {
                findActivityNavController(R.id.fragment_container)
                    .navigate(
                        R.id.artistDetailsFragment,
                        bundleOf(EXTRA_ARTIST_ID to albumDetails.artist),
                        null,
                        FragmentNavigatorExtras(artistView to albumDetails.artist.id)
                    )
            }

        }
        binding.fragmentAlbumContent.playAction.setOnClickListener {
// TODO:           fix play
            //  MusicPlayerRemote.openQueue(album.songs, 0, true)
        }
        binding.fragmentAlbumContent.shuffleAction.setOnClickListener {
            // TODO:           fix play
//            MusicPlayerRemote.openAndShuffleQueue(
//                album.songs,
//                true
//            )
        }

        binding.fragmentAlbumContent.aboutAlbumText.setOnClickListener {
            if (binding.fragmentAlbumContent.aboutAlbumText.maxLines == 4) {
                binding.fragmentAlbumContent.aboutAlbumText.maxLines = Integer.MAX_VALUE
            } else {
                binding.fragmentAlbumContent.aboutAlbumText.maxLines = 4
            }
        }

        binding.appBarLayout?.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())
    }

    private fun setupRecyclerView() {
        simpleSongAdapter = AlbumDetailsSongAdapter(
            requireActivity() as AppCompatActivity,
            ArrayList(),
            R.layout.item_song
        )
        binding.fragmentAlbumContent.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = false
            adapter = simpleSongAdapter
        }
    }

    private fun showAlbum(albumDetails: AlbumDetailsUi) {
        if (albumDetails.songs.isEmpty()) {
            findNavController().navigateUp()
            return
        }
        this.albumDetails = albumDetails

        binding.albumTitle.text = albumDetails.title
        val songText = resources.getQuantityString(
            R.plurals.albumSongs,
            albumDetails.songs.size,
            albumDetails.songs.size
        )
        binding.fragmentAlbumContent.songTitle.text = songText
        if (MusicUtil.getYearString(albumDetails.year) == "-") {
            binding.albumText.text = String.format(
                "%s",
                albumDetails.artist.title
            )
        } else {
            binding.albumText.text = String.format(
                "%s • %s",
                albumDetails.artist.title,
                MusicUtil.getYearString(albumDetails.year)
            )
        }
        loadAlbumCover(albumDetails)
        simpleSongAdapter.swapDataSet(albumDetails.songs)
        loadArtistImage(albumDetails.artist)

//        detailsViewModel.getAlbumInfo(album).observe(viewLifecycleOwner) { result ->
//            when (result) {
//                is Result.Loading -> {
//                    logD("Loading")
//                }
//                is Result.Error -> {
//                    logE("Error")
//                }
//                is Result.Success -> {
//                    aboutAlbum(result.data)
//                }
//            }
//        }
    }

    private fun moreAlbums(albums: List<Album>) {
        binding.fragmentAlbumContent.moreTitle.show()
        binding.fragmentAlbumContent.moreRecyclerView.show()
        binding.fragmentAlbumContent.moreTitle.text =
            String.format(getString(R.string.label_more_from), albumDetails.artist)

        val albumAdapter =
            HorizontalAlbumAdapter(requireActivity() as AppCompatActivity, albums, this)
        binding.fragmentAlbumContent.moreRecyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            1,
            GridLayoutManager.HORIZONTAL,
            false
        )
        binding.fragmentAlbumContent.moreRecyclerView.adapter = albumAdapter
    }

    private fun aboutAlbum(lastFmAlbum: LastFmAlbum) {
        if (lastFmAlbum.album != null) {
            if (lastFmAlbum.album.wiki != null) {
                binding.fragmentAlbumContent.aboutAlbumText.show()
                binding.fragmentAlbumContent.aboutAlbumTitle.show()
                binding.fragmentAlbumContent.aboutAlbumTitle.text =
                    String.format(getString(R.string.about_album_label), lastFmAlbum.album.name)
                binding.fragmentAlbumContent.aboutAlbumText.text =
                    lastFmAlbum.album.wiki.content.parseAsHtml()
            }
            if (lastFmAlbum.album.listeners.isNotEmpty()) {
                binding.fragmentAlbumContent.listeners.show()
                binding.fragmentAlbumContent.listenersLabel.show()
                binding.fragmentAlbumContent.scrobbles.show()
                binding.fragmentAlbumContent.scrobblesLabel.show()

                binding.fragmentAlbumContent.listeners.text =
                    RetroUtil.formatValue(lastFmAlbum.album.listeners.toFloat())
                binding.fragmentAlbumContent.scrobbles.text =
                    RetroUtil.formatValue(lastFmAlbum.album.playcount.toFloat())
            }
        }
    }

    private fun loadArtistImage(artist: AlbumDetailsUi.ArtistUi) {
//        detailsViewModel.getMoreAlbums(artist).observe(viewLifecycleOwner) {
//            moreAlbums(it)
//        }
        GlideApp.with(requireContext())
            //.forceDownload(PreferenceUtil.isAllowedToDownloadMetadata())
            .load(artist.coverArtUrl)
            .dontAnimate()
            .dontTransform()
            .into(binding.artistImage)
    }

    private fun loadAlbumCover(album: AlbumDetailsUi) {
        GlideApp
            .with(requireContext())
            .asBitmapPalette()
            .load(album.coverArtUrl)
            .into(object : SingleColorTarget(binding.image) {
                override fun onColorReady(color: Int) {
                    setColors(color)
                }
            })
    }

    private fun setColors(color: Int) {
        _binding?.fragmentAlbumContent?.apply {
            shuffleAction.applyColor(color)
            playAction.applyOutlineColor(color)
        }
    }

    override fun onAlbumClick(albumId: String, view: View) {
        findNavController().navigate(
            R.id.albumDetailsFragment,
            bundleOf(EXTRA_ALBUM_ID to albumId),
            null,
            FragmentNavigatorExtras(view to albumId)
        )
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_album_detail, menu)
        val sortOrder = menu.findItem(R.id.action_sort_order)
//        setUpSortOrderMenu(sortOrder.subMenu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            binding.toolbar,
            menu,
            getToolbarBackgroundColor(binding.toolbar)
        )
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        var sortOrder: String? = null
        val songs = simpleSongAdapter.dataSet
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.action_play_next -> {
//                TODO: fix play
//                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
//                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
                lifecycleScope.launch(Dispatchers.IO) {
//                    TODO: fix playlists
//                    val playlists = get<RealRepository>().fetchPlaylists()
//                    withContext(Dispatchers.Main) {
//                        AddToPlaylistDialog.create(playlists, songs)
//                            .show(childFragmentManager, "ADD_PLAYLIST")
//                    }
                }
                return true
            }
            R.id.action_delete_from_device -> {
//                TODO: fix delete
//                DeleteSongsDialog.create(songs).show(childFragmentManager, "DELETE_SONGS")
                return true
            }
            R.id.action_tag_editor -> {
                val intent = Intent(requireContext(), AlbumTagEditorActivity::class.java)
                intent.putExtra(AbsTagEditorActivity.EXTRA_ID, albumDetails.id)
                val options = ActivityOptions.makeSceneTransitionAnimation(
                    requireActivity(),
                    binding.albumCoverContainer,
                    "${getString(R.string.transition_album_art)}_${albumDetails.id}"
                )
                startActivity(
                    intent, options.toBundle()
                )
                return true
            }
            R.id.action_sort_order_title -> sortOrder = SONG_A_Z
            R.id.action_sort_order_title_desc -> sortOrder = SONG_Z_A
            R.id.action_sort_order_track_list -> sortOrder = SONG_TRACK_LIST
            R.id.action_sort_order_artist_song_duration -> sortOrder = SONG_DURATION
        }
        if (sortOrder != null) {
            item.isChecked = true
            setSaveSortOrder(sortOrder)
        }
        return true
    }

    private fun setUpSortOrderMenu(sortOrder: SubMenu) {
        when (savedSortOrder) {
            SONG_A_Z -> sortOrder.findItem(R.id.action_sort_order_title).isChecked = true
            SONG_Z_A -> sortOrder.findItem(R.id.action_sort_order_title_desc).isChecked = true
            SONG_TRACK_LIST ->
                sortOrder.findItem(R.id.action_sort_order_track_list).isChecked = true
            SONG_DURATION ->
                sortOrder.findItem(R.id.action_sort_order_artist_song_duration).isChecked = true
        }
    }

    private fun setSaveSortOrder(sortOrder: String) {
        PreferenceUtil.albumDetailSongSortOrder = sortOrder
        val songs = when (sortOrder) {
//            SONG_TRACK_LIST -> album.songs.sortedWith { o1, o2 ->
//                o1.trackNumber.compareTo(
//                    o2.trackNumber
//                )
//            }
            SONG_A_Z -> {
                val collator = Collator.getInstance()
                albumDetails.songs.sortedWith { o1, o2 -> collator.compare(o1.title, o2.title) }
            }
            SONG_Z_A -> {
                val collator = Collator.getInstance()
                albumDetails.songs.sortedWith { o1, o2 -> collator.compare(o2.title, o1.title) }
            }
//            SONG_DURATION -> album.songs.sortedWith { o1, o2 ->
//                o1.duration.compareTo(
//                    o2.duration
//                )
//            }
            else -> throw IllegalArgumentException("invalid $sortOrder")
        }
        albumDetails = albumDetails.copy(songs = songs)
        simpleSongAdapter.swapDataSet(albumDetails.songs)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
