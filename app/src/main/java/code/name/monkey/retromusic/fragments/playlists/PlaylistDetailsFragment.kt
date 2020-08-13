package code.name.monkey.retromusic.fragments.playlists

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.song.OrderablePlaylistSongAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.dipToPix
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import code.name.monkey.retromusic.helper.menu.PlaylistMenuHelper
import code.name.monkey.retromusic.model.AbsCustomPlaylist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PlaylistsUtil
import com.h6ah4i.android.widget.advrecyclerview.animator.RefactoredDefaultItemAnimator
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils
import kotlinx.android.synthetic.main.fragment_playlist_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistDetailsFragment : AbsMainActivityFragment(R.layout.fragment_playlist_detail) {
    private val arguments by navArgs<PlaylistDetailsFragmentArgs>()
    private val viewModel: PlaylistDetailsViewModel by viewModel {
        parametersOf(arguments.extraPlaylist)
    }

    private lateinit var playlist: Playlist
    private lateinit var adapter: SongAdapter

    private var wrappedAdapter: RecyclerView.Adapter<*>? = null
    private var recyclerViewDragDropManager: RecyclerViewDragDropManager? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.addMusicServiceEventListener(viewModel)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.hideBottomBarVisibility(false)

        playlist = arguments.extraPlaylist

        setUpRecyclerView()

        viewModel.getSongs().observe(viewLifecycleOwner, Observer {
            songs(it)
        })

        viewModel.getPlaylist().observe(viewLifecycleOwner, Observer {
            playlist = it
            toolbar.title = it.name
        })
    }

    private fun setUpRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        if (playlist is AbsCustomPlaylist) {
            adapter = SongAdapter(requireActivity(), ArrayList(), R.layout.item_list, null)
            recyclerView.adapter = adapter
        } else {
            recyclerViewDragDropManager = RecyclerViewDragDropManager()
            val animator = RefactoredDefaultItemAnimator()
            adapter = OrderablePlaylistSongAdapter(requireActivity(),
                ArrayList(),
                R.layout.item_list,
                null,
                object : OrderablePlaylistSongAdapter.OnMoveItemListener {
                    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
                        if (PlaylistsUtil.moveItem(
                                requireContext(),
                                playlist.id,
                                fromPosition,
                                toPosition
                            )
                        ) {
                            val song = adapter.dataSet.removeAt(fromPosition)
                            adapter.dataSet.add(toPosition, song)
                            adapter.notifyItemMoved(fromPosition, toPosition)
                        }
                    }
                })
            wrappedAdapter = recyclerViewDragDropManager!!.createWrappedAdapter(adapter)

            recyclerView.adapter = wrappedAdapter
            recyclerView.itemAnimator = animator

            recyclerViewDragDropManager?.attachRecyclerView(recyclerView)
        }
        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                super.onChanged()
                checkIsEmpty()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val menuRes = if (playlist is AbsCustomPlaylist)
            R.menu.menu_smart_playlist_detail
        else R.menu.menu_playlist_detail
        inflater.inflate(menuRes, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return PlaylistMenuHelper.handleMenuClick(requireActivity(), playlist, item)
    }

    private fun checkForPadding() {
        val height = dipToPix(52f)
        recyclerView.setPadding(0, 0, 0, height.toInt())
    }

    private fun checkIsEmpty() {
        checkForPadding()
        emptyEmoji.text = getEmojiByUnicode(0x1F631)
        empty.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        emptyText.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    private fun getEmojiByUnicode(unicode: Int): String {
        return String(Character.toChars(unicode))
    }

    public override fun onPause() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager!!.cancelDrag()
        }
        super.onPause()
    }

    override fun onDestroy() {
        if (recyclerViewDragDropManager != null) {
            recyclerViewDragDropManager!!.release()
            recyclerViewDragDropManager = null
        }

        if (recyclerView != null) {
            recyclerView!!.itemAnimator = null
            recyclerView!!.adapter = null
        }

        if (wrappedAdapter != null) {
            WrapperAdapterUtils.releaseAll(wrappedAdapter)
            wrappedAdapter = null
        }
        super.onDestroy()
    }

    private fun showEmptyView() {
        empty.visibility = View.VISIBLE
        emptyText.visibility = View.VISIBLE
    }

    fun songs(songs: List<Song>) {
        if (songs.isNotEmpty()) {
            adapter.swapDataSet(songs)
        } else {
            showEmptyView()
        }
    }

}