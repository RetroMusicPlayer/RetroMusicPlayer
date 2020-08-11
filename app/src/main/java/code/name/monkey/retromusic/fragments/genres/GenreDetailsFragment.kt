package code.name.monkey.retromusic.fragments.genres

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.dipToPix
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.menu.GenreMenuHelper
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*

class GenreDetailsFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail) {
    private val arguments by navArgs<GenreDetailsFragmentArgs>()
    private val detailsViewModel: GenreDetailsViewModel by viewModel {
        parametersOf(arguments.extraGenre)
    }

    private lateinit var genre: Genre
    private lateinit var songAdapter: SongAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.addMusicServiceEventListener(detailsViewModel)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.setBottomBarVisibility(View.GONE)

        setupRecyclerView()
        detailsViewModel.getSongs().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            songs(it)
        })
        detailsViewModel.getGenre().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            genre = it
            toolbar?.title = it.name
        })
    }

    private fun setupRecyclerView() {
        songAdapter = SongAdapter(requireActivity(), ArrayList(), R.layout.item_list, null)
        recyclerView.apply {
            itemAnimator = DefaultItemAnimator()
            layoutManager = LinearLayoutManager(requireContext())
            adapter = songAdapter
        }
        songAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    fun songs(songs: List<Song>) {
        songAdapter.swapDataSet(songs)
    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    private fun checkIsEmpty() {
        checkForPadding()
        emptyEmoji.text = getEmojiByUnicode(0x1F631)
        empty?.visibility = if (songAdapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun checkForPadding() {
        val height = dipToPix(52f).toInt()
        recyclerView.setPadding(0, 0, 0, height)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_genre_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return GenreMenuHelper.handleMenuClick(requireActivity(), genre, item)
    }
}