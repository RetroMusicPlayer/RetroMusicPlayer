package code.name.monkey.retromusic.fragments.artists

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spanned
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.doOnPreDraw
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import code.name.monkey.retromusic.EXTRA_ALBUM_ID
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.HorizontalAlbumAdapter
import code.name.monkey.retromusic.adapter.song.SimpleSongAdapter
import code.name.monkey.retromusic.databinding.FragmentArtistDetailsBinding
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.extensions.*
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.SingleColorTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.IAlbumClickListener
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.network.Result
import code.name.monkey.retromusic.network.model.LastFmArtist
import code.name.monkey.retromusic.repository.RealRepository
import code.name.monkey.retromusic.util.CustomArtistImageUtil
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroColorUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.afollestad.materialcab.MaterialCab
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import java.util.*
import kotlin.collections.ArrayList

abstract class AbsArtistDetailsFragment : AbsMainActivityFragment(R.layout.fragment_artist_details),
    IAlbumClickListener, ICabHolder {
    private var _binding: FragmentArtistDetailsBinding? = null
    private val binding get() = _binding!!

    abstract val detailsViewModel: ArtistDetailsViewModel
    abstract val artistId: Long?
    abstract val artistName: String?
    private lateinit var artist: Artist
    private lateinit var songAdapter: SimpleSongAdapter
    private lateinit var albumAdapter: HorizontalAlbumAdapter
    private var forceDownload: Boolean = false
    private var lang: String? = null
    private var biography: Spanned? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment_container
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().resolveColor(R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentArtistDetailsBinding.bind(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.addMusicServiceEventListener(detailsViewModel)
        mainActivity.setSupportActionBar(binding.toolbar)
        binding.toolbar.title = null
        ViewCompat.setTransitionName(
            binding.artistCoverContainer,
            (artistId ?: artistName).toString()
        )
        postponeEnterTransition()
        detailsViewModel.getArtist().observe(viewLifecycleOwner, {
            requireView().doOnPreDraw {
                startPostponedEnterTransition()
            }
            showArtist(it)
        })
        setupRecyclerView()

        binding.fragmentArtistContent.playAction.apply {
            setOnClickListener { MusicPlayerRemote.openQueue(artist.songs, 0, true) }
        }
        binding.fragmentArtistContent.shuffleAction.apply {
            setOnClickListener { MusicPlayerRemote.openAndShuffleQueue(artist.songs, true) }
        }

        binding.fragmentArtistContent.biographyText.setOnClickListener {
            if (binding.fragmentArtistContent.biographyText.maxLines == 4) {
                binding.fragmentArtistContent.biographyText.maxLines = Integer.MAX_VALUE
            } else {
                binding.fragmentArtistContent.biographyText.maxLines = 4
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (!handleBackPress()) {
                remove()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setupRecyclerView() {
        albumAdapter = HorizontalAlbumAdapter(requireActivity(), ArrayList(), this, this)
        binding.fragmentArtistContent.albumRecyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = GridLayoutManager(this.context, 1, GridLayoutManager.HORIZONTAL, false)
            adapter = albumAdapter
        }
        songAdapter = SimpleSongAdapter(requireActivity(), ArrayList(), R.layout.item_song, this)
        binding.fragmentArtistContent.recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(this.context)
            adapter = songAdapter
        }
    }

    private fun showArtist(artist: Artist) {
        this.artist = artist
        loadArtistImage(artist)
        if (RetroUtil.isAllowedToDownloadMetadata(requireContext())) {
            loadBiography(artist.name)
        }
        binding.artistTitle.text = artist.name
        binding.text.text = String.format(
            "%s • %s",
            MusicUtil.getArtistInfoString(requireContext(), artist),
            MusicUtil.getReadableDurationString(MusicUtil.getTotalDuration(artist.songs))
        )
        val songText = resources.getQuantityString(
            R.plurals.albumSongs,
            artist.songCount,
            artist.songCount
        )
        val albumText = resources.getQuantityString(
            R.plurals.albums,
            artist.songCount,
            artist.songCount
        )
        binding.fragmentArtistContent.songTitle.text = songText
        binding.fragmentArtistContent.albumTitle.text = albumText
        songAdapter.swapDataSet(artist.songs.sortedBy { it.trackNumber })
        albumAdapter.swapDataSet(artist.albums)
    }

    private fun loadBiography(
        name: String,
        lang: String? = Locale.getDefault().language
    ) {
        biography = null
        this.lang = lang
        detailsViewModel.getArtistInfo(name, lang, null)
            .observe(viewLifecycleOwner, { result ->
                when (result) {
                    is Result.Loading -> println("Loading")
                    is Result.Error -> println("Error")
                    is Result.Success -> artistInfo(result.data)
                }
            })
    }

    private fun artistInfo(lastFmArtist: LastFmArtist?) {
        if (lastFmArtist != null && lastFmArtist.artist != null && lastFmArtist.artist.bio != null) {
            val bioContent = lastFmArtist.artist.bio.content
            if (bioContent != null && bioContent.trim { it <= ' ' }.isNotEmpty()) {
                binding.fragmentArtistContent.biographyText.visibility = View.VISIBLE
                binding.fragmentArtistContent.biographyTitle.visibility = View.VISIBLE
                biography = HtmlCompat.fromHtml(bioContent, HtmlCompat.FROM_HTML_MODE_LEGACY)
                binding.fragmentArtistContent.biographyText.text = biography
                if (lastFmArtist.artist.stats.listeners.isNotEmpty()) {
                    binding.fragmentArtistContent.listeners.show()
                    binding.fragmentArtistContent.listenersLabel.show()
                    binding.fragmentArtistContent.scrobbles.show()
                    binding.fragmentArtistContent.scrobblesLabel.show()
                    binding.fragmentArtistContent.listeners.text =
                        RetroUtil.formatValue(lastFmArtist.artist.stats.listeners.toFloat())
                    binding.fragmentArtistContent.scrobbles.text =
                        RetroUtil.formatValue(lastFmArtist.artist.stats.playcount.toFloat())
                }
            }
        }

        // If the "lang" parameter is set and no biography is given, retry with default language
        if (biography == null && lang != null) {
            loadBiography(artist.name, null)
        }
    }

    private fun loadArtistImage(artist: Artist) {
        GlideApp.with(requireContext()).asBitmapPalette().artistImageOptions(artist)
            .load(RetroGlideExtension.getArtistModel(artist))
            .dontAnimate()
            .into(object : SingleColorTarget(binding.image) {
                override fun onColorReady(color: Int) {
                    setColors(color)
                }
            })
    }

    private fun setColors(color: Int) {
        if (_binding != null) {
            binding.fragmentArtistContent.shuffleAction.applyColor(color)
            binding.fragmentArtistContent.playAction.applyOutlineColor(color)
        }
    }

    override fun onAlbumClick(albumId: Long, view: View) {
        findNavController().navigate(
            R.id.albumDetailsFragment,
            bundleOf(EXTRA_ALBUM_ID to albumId),
            null,
            FragmentNavigatorExtras(
                view to albumId.toString()
            )
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return handleSortOrderMenuItem(item)
    }

    private fun handleSortOrderMenuItem(item: MenuItem): Boolean {
        val songs = artist.songs
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
                lifecycleScope.launch(Dispatchers.IO) {
                    val playlists = get<RealRepository>().fetchPlaylists()
                    withContext(Dispatchers.Main) {
                        AddToPlaylistDialog.create(playlists, songs)
                            .show(childFragmentManager, "ADD_PLAYLIST")
                    }
                }
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
                showToast(resources.getString(R.string.updating))
                CustomArtistImageUtil.getInstance(requireContext()).resetCustomArtistImage(artist)
                forceDownload = true
                return true
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SELECT_IMAGE -> if (resultCode == Activity.RESULT_OK) {
                data?.data?.let {
                    CustomArtistImageUtil.getInstance(requireContext())
                        .setCustomArtistImage(artist, it)
                }
            }
            else -> if (resultCode == Activity.RESULT_OK) {
                println("OK")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_artist_detail, menu)
    }


    private fun handleBackPress(): Boolean {
        cab?.let {
            if (it.isActive) {
                it.finish()
                return true
            }
        }
        return false
    }

    private var cab: MaterialCab? = null

    override fun openCab(menuRes: Int, callback: MaterialCab.Callback): MaterialCab {
        cab?.let {
            if (it.isActive) {
                it.finish()
            }
        }
        cab = MaterialCab(mainActivity, R.id.cab_stub)
            .setMenu(menuRes)
            .setCloseDrawableRes(R.drawable.ic_close)
            .setBackgroundColor(RetroColorUtil.shiftBackgroundColorForLightText(surfaceColor()))
            .start(callback)
        return cab as MaterialCab
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val REQUEST_CODE_SELECT_IMAGE = 9002
    }
}