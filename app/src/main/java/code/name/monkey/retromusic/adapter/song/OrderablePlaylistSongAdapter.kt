package code.name.monkey.retromusic.adapter.song

import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.db.toSongsEntity
import code.name.monkey.retromusic.dialogs.RemoveSongFromPlaylistDialog
import code.name.monkey.retromusic.fragments.LibraryViewModel
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.ViewUtil
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderablePlaylistSongAdapter(
    private val playlistId: Long,
    activity: FragmentActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
) : SongAdapter(activity, dataSet, itemLayoutRes),
    DraggableItemAdapter<OrderablePlaylistSongAdapter.ViewHolder> {

    val libraryViewModel: LibraryViewModel by activity.viewModel()

    private var filtered = false
    private var filter: CharSequence? = null
    private var fullDataSet: MutableList<Song>

    init {
        this.setHasStableIds(true)
        this.setMultiSelectMenuRes(R.menu.menu_playlists_songs_selection)
        fullDataSet = dataSet.toMutableList()
    }

    override fun swapDataSet(dataSet: List<Song>) {
        super.swapDataSet(dataSet)
        fullDataSet = dataSet.toMutableList()
        onFilter(filter)
    }

    override fun getItemId(position: Int): Long {
        // requires static value, it means need to keep the same value
        // even if the item position has been changed.
        return dataSet[position].id
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: List<Song>) {
        when (menuItem.itemId) {
            R.id.action_remove_from_playlist -> RemoveSongFromPlaylistDialog.create(
                selection.toSongsEntity(
                    playlistId
                )
            )
                .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")

            else -> super.onMultipleItemAction(menuItem, selection)
        }
    }

    inner class ViewHolder(itemView: View) : SongAdapter.ViewHolder(itemView) {

        override var songMenuRes: Int
            get() = R.menu.menu_item_playlist_song
            set(value) {
                super.songMenuRes = value
            }

        override fun onSongMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_remove_from_playlist -> {
                    RemoveSongFromPlaylistDialog.create(song.toSongEntity(playlistId))
                        .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")
                    return true
                }
            }
            return super.onSongMenuItemClick(item)
        }

        override fun onClick(v: View?) {
            if (isInQuickSelectMode || !filtered) {
                super.onClick(v)
            } else {
                val position = fullDataSet.indexOf(dataSet.get(layoutPosition))
                MusicPlayerRemote.openQueueKeepShuffleMode(fullDataSet, position, true)
            }
        }

        init {
            dragView?.isVisible = true
        }
    }

    override fun onCheckCanStartDrag(holder: ViewHolder, position: Int, x: Int, y: Int): Boolean {
        if (isInQuickSelectMode || filtered) {
            return false
        }
        return ViewUtil.hitTest(holder.imageText!!, x, y) || ViewUtil.hitTest(
            holder.dragView!!,
            x,
            y
        )
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        dataSet.add(toPosition, dataSet.removeAt(fromPosition))
    }

    override fun onGetItemDraggableRange(holder: ViewHolder, position: Int): ItemDraggableRange? {
        return null
    }

    override fun onCheckCanDrop(draggingPosition: Int, dropPosition: Int): Boolean {
        return true
    }

    override fun onItemDragStarted(position: Int) {
        notifyDataSetChanged()
    }

    override fun onItemDragFinished(fromPosition: Int, toPosition: Int, result: Boolean) {
        notifyDataSetChanged()
    }

    fun saveSongs(playlistEntity: PlaylistEntity) {
        onFilter(null)
        activity.lifecycleScope.launch(Dispatchers.IO) {
            libraryViewModel.insertSongs(dataSet.toSongsEntity(playlistEntity))
        }
    }


    fun onFilter(text: CharSequence?) {
        filter = text
        if (text.isNullOrEmpty()) {
            filtered = false
            dataSet = fullDataSet
        } else {
            filtered = true
            dataSet = fullDataSet.filter { song -> song.title.contains(text, ignoreCase = true) }
                .toMutableList()
        }
        notifyDataSetChanged()
    }

    fun hasSongs(): Boolean {
        return itemCount > 0 || (filtered && fullDataSet.size > 0)
    }
}
