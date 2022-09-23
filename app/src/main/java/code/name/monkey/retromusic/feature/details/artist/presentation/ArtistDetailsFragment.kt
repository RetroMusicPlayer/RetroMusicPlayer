package code.name.monkey.retromusic.feature.details.artist.presentation

import android.graphics.Color
import android.os.Bundle
import android.text.Spanned
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.core.text.parseAsHtml
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.EXTRA_ALBUM_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.FragmentArtistDetailsBinding
import code.name.monkey.retromusic.extensions.applyColor
import code.name.monkey.retromusic.extensions.applyOutlineColor
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.extensions.surfaceColor
import code.name.monkey.retromusic.feature.details.artist.presentation.adapter.ArtistDetailsAlbumAdapter
import code.name.monkey.retromusic.feature.details.artist.presentation.adapter.ArtistDetailsSongAdapter
import code.name.monkey.retromusic.feature.details.artist.presentation.entity.ArtistDetailsUi
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.interfaces.IAlbumClickListener
import code.name.monkey.retromusic.network.model.LastFmArtist
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.transition.MaterialContainerTransform
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class ArtistDetailsFragment : AbsMainActivityFragment(R.layout.fragment_artist_details), IAlbumClickListener {

    private var _binding: FragmentArtistDetailsBinding? = null
    private val binding
        get() = _binding!!

    private val arguments by navArgs<ArtistDetailsFragmentArgs>()
    private val artistId: String
        get() = arguments.extraArtistId

    private val detailsViewModel: ArtistDetailsViewModel by viewModel {
        parametersOf(arguments.extraArtistId, null)
    }

    private lateinit var songAdapter: ArtistDetailsSongAdapter
    private lateinit var albumAdapter: ArtistDetailsAlbumAdapter
    private var lang: String? = null
    private var biography: Spanned? = null

    private val savedSongSortOrder: String
        get() = PreferenceUtil.artistDetailSongSortOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(surfaceColor())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArtistDetailsBinding.bind(view)
        mainActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.title = null
        binding.artistCoverContainer.transitionName = artistId
        postponeEnterTransition()
        lifecycleScope.launchWhenStarted {
            detailsViewModel.artistDetails.collect {
                view.doOnPreDraw {
                    startPostponedEnterTransition()
                }
                showArtist(it)
            }
        }
        setupRecyclerView()
        lifecycleScope.launchWhenStarted {
            detailsViewModel.albums.collect {
                albumAdapter.swapDataSet(it)
            }
        }
        lifecycleScope.launchWhenStarted {
            detailsViewModel.songs.collect {
                Log.d("Dbg", "Songs count: ${it.size}")
                songAdapter.swapDataSet(it)
            }
        }

        binding.fragmentArtistContent.playAction.apply {
            setOnClickListener {
//                MusicPlayerRemote.openQueue(artist.sortedSongs, 0, true)
            }
        }
        binding.fragmentArtistContent.shuffleAction.apply {
            setOnClickListener {
//                MusicPlayerRemote.openAndShuffleQueue(artist.songs, true)
            }
        }

        binding.fragmentArtistContent.biographyText.setOnClickListener {
            if (binding.fragmentArtistContent.biographyText.maxLines == 4) {
                binding.fragmentArtistContent.biographyText.maxLines = Integer.MAX_VALUE
            } else {
                binding.fragmentArtistContent.biographyText.maxLines = 4
            }
        }
        setupSongSortButton()
        binding.appBarLayout?.statusBarForeground =
            MaterialShapeDrawable.createWithElevationOverlay(requireContext())
    }

    private fun setupRecyclerView() {
        albumAdapter = ArtistDetailsAlbumAdapter(requireActivity(), ArrayList(), this)
        binding.fragmentArtistContent.albumRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(this.context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
        songAdapter = ArtistDetailsSongAdapter(requireActivity(), ArrayList(), R.layout.item_song)
        binding.fragmentArtistContent.recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }
    }

    private fun showArtist(artistDetails: ArtistDetailsUi) {
        loadArtistImage(artistDetails)
        if (PreferenceUtil.isAllowedToDownloadMetadata(requireContext())) {
            loadBiography(artistDetails.title)
        }
        binding.artistTitle.text = artistDetails.title
        binding.text.text = String.format(
            "%s • %s",
            getArtistInfoString(artistDetails),
            MusicUtil.getReadableDurationString(artistDetails.totalDuration)
        )
        val songText = resources.getQuantityString(
            R.plurals.albumSongs,
            artistDetails.songCount,
            artistDetails.songCount
        )
        val albumText = resources.getQuantityString(
            R.plurals.albums,
            artistDetails.songCount,
            artistDetails.songCount
        )
        binding.fragmentArtistContent.songTitle.text = songText
        binding.fragmentArtistContent.albumTitle.text = albumText

        binding.fragmentArtistContent.biographyText.text = artistDetails.biography
//        songAdapter.swapDataSet(artistDetails.sortedSongs)
//        albumAdapter.swapDataSet(artistDetails.albums)
    }

    private fun getArtistInfoString(artistDetails: ArtistDetailsUi): String {
        val albumCount = artistDetails.albumCount
        val songCount = artistDetails.songCount
        val albumString = if (albumCount == 1) {
            getString(R.string.album)
        } else {
            getString(R.string.albums)
        }
        val songString = if (songCount == 1) {
            getString(R.string.song)
        } else {
            getString(R.string.songs)
        }
        return "$albumCount $albumString • $songCount $songString"
    }

    private fun loadBiography(
        name: String,
        lang: String? = Locale.getDefault().language,
    ) {
        biography = null
        this.lang = lang
//        detailsViewModel.getArtistInfo(name, lang, null)
//            .observe(viewLifecycleOwner) { result ->
//                when (result) {
//                    is Result.Loading -> logD("Loading")
//                    is Result.Error -> logE("Error")
//                    is Result.Success -> artistInfo(result.data)
//                }
//            }
    }

    private fun artistInfo(lastFmArtist: LastFmArtist?) {
        if (lastFmArtist != null && lastFmArtist.artist != null && lastFmArtist.artist.bio != null) {
            val bioContent = lastFmArtist.artist.bio.content
            if (bioContent != null && bioContent.trim { it <= ' ' }.isNotEmpty()) {
                binding.fragmentArtistContent.run {
                    biographyText.isVisible = true
                    biographyTitle.isVisible = true
                    biography = bioContent.parseAsHtml()
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
        }

        // If the "lang" parameter is set and no biography is given, retry with default language
//        if (biography == null && lang != null) {
//            loadBiography(artist.name, null)
//        }
    }

    private fun loadArtistImage(artistDetails: ArtistDetailsUi) {
//        val target = object : SingleColorTarget(binding.image) {
//            override fun onColorReady(color: Int) {
//                setColors(color)
//            }
//        }
//        GlideApp
//            .with(requireContext())
//            .load(artistDetails.coverArtUrl)
//            .dontAnimate()
//            .into(target)
        GlideApp
            .with(requireContext())
            .load(artistDetails.coverArtUrl)
            .dontAnimate()
            .into(binding.image)
    }

    private fun setColors(color: Int) {
        if (_binding != null) {
            binding.fragmentArtistContent.shuffleAction.applyColor(color)
            binding.fragmentArtistContent.playAction.applyOutlineColor(color)
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

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
//        val songs = artist.songs
        when (item.itemId) {
            android.R.id.home -> findNavController().navigateUp()
            R.id.action_play_next -> {
//                MusicPlayerRemote.playNext(songs)
                return true
            }
            R.id.action_add_to_current_playing -> {
//                MusicPlayerRemote.enqueue(songs)
                return true
            }
            R.id.action_add_to_playlist -> {
//                lifecycleScope.launch(Dispatchers.IO) {
//                    val playlists = get<RealRepository>().fetchPlaylists()
//                    withContext(Dispatchers.Main) {
//                        AddToPlaylistDialog.create(playlists, songs)
//                            .show(childFragmentManager, "ADD_PLAYLIST")
//                    }
//                }
                return true
            }
        }
        return true
    }

    private fun setupSongSortButton() {
        binding.fragmentArtistContent.songSortOrder.setOnClickListener {
            PopupMenu(requireContext(), binding.fragmentArtistContent.songSortOrder).apply {
                inflate(R.menu.menu_artist_song_sort_order)
                setUpSortOrderMenu(menu)
                setOnMenuItemClickListener { item ->
                    val sortOrder = when (item.itemId) {
                        R.id.action_sort_order_title -> SortOrder.ArtistSongSortOrder.SONG_A_Z
                        R.id.action_sort_order_title_desc -> SortOrder.ArtistSongSortOrder.SONG_Z_A
                        R.id.action_sort_order_album -> SortOrder.ArtistSongSortOrder.SONG_ALBUM
                        R.id.action_sort_order_year -> SortOrder.ArtistSongSortOrder.SONG_YEAR
                        R.id.action_sort_order_song_duration -> SortOrder.ArtistSongSortOrder.SONG_DURATION
                        else -> {
                            throw IllegalArgumentException("invalid ${item.title}")
                        }
                    }
                    item.isChecked = true
                    setSaveSortOrder(sortOrder)
                    return@setOnMenuItemClickListener true
                }
                show()
            }
        }
    }

    private fun setSaveSortOrder(sortOrder: String) {
        PreferenceUtil.artistDetailSongSortOrder = sortOrder
//        songAdapter.swapDataSet(artist.sortedSongs)
    }

    private fun setUpSortOrderMenu(sortOrder: Menu) {
        when (savedSongSortOrder) {
            SortOrder.ArtistSongSortOrder.SONG_A_Z -> sortOrder.findItem(R.id.action_sort_order_title).isChecked =
                true
            SortOrder.ArtistSongSortOrder.SONG_Z_A -> sortOrder.findItem(R.id.action_sort_order_title_desc).isChecked =
                true
            SortOrder.ArtistSongSortOrder.SONG_ALBUM ->
                sortOrder.findItem(R.id.action_sort_order_album).isChecked = true
            SortOrder.ArtistSongSortOrder.SONG_YEAR ->
                sortOrder.findItem(R.id.action_sort_order_year).isChecked = true
            SortOrder.ArtistSongSortOrder.SONG_DURATION ->
                sortOrder.findItem(R.id.action_sort_order_song_duration).isChecked = true
            else -> {
                throw IllegalArgumentException("invalid $savedSongSortOrder")
            }
        }
    }

    private val selectImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                result.data?.data?.let {
//                    lifecycleScope.launch {
//                        CustomArtistImageUtil.getInstance(requireContext())
//                            .setCustomArtistImage(artist, it)
//                    }
//                }
//            }
        }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_artist_detail, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}