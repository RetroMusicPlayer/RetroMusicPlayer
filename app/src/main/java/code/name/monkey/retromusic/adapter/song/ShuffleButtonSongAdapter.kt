package code.name.monkey.retromusic.adapter.song

import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SortOrder.SongSortOrder
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.textview.MaterialTextView

class ShuffleButtonSongAdapter(
    activity: AppCompatActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
    cabHolder: CabHolder?
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, cabHolder) {

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == OFFSET_ITEM) {
            val viewHolder = holder as ViewHolder
            viewHolder.playAction?.setOnClickListener {
                MusicPlayerRemote.openQueue(dataSet, 0, true)
            }
            viewHolder.shuffleAction?.setOnClickListener {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
            }
            val songCount = activity.resources.getQuantityString(R.plurals.songCount, dataSet.size, dataSet.size)
            viewHolder.songCount?.text = songCount
            viewHolder.sortAction?.setOnClickListener {
                showSortPopupMenu(it)
            }
        } else {
            super.onBindViewHolder(holder, position - 1)
        }
    }

    private fun showGridPopupMenu(view: View) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.menu_grid_options, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_grid_size_1 -> {
                    PreferenceUtil.getInstance(activity).setSongGridSize(1)
                }
                R.id.action_grid_size_2 -> {
                    PreferenceUtil.getInstance(activity).setSongGridSize(2)
                }
                R.id.action_grid_size_3 -> {
                    PreferenceUtil.getInstance(activity).setSongGridSize(3)
                }
                R.id.action_grid_size_4 -> {
                    PreferenceUtil.getInstance(activity).setSongGridSize(4)
                }

            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    private fun showSortPopupMenu(view: View) {
        val popupMenu = PopupMenu(activity, view)
        popupMenu.menuInflater.inflate(R.menu.menu_song_sort, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_song_sort_order_asc -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_A_Z
                }
                R.id.action_song_sort_order_desc -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_Z_A
                }
                R.id.action_song_sort_order_artist -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_ARTIST
                }
                R.id.action_song_sort_order_album -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_ALBUM
                }
                R.id.action_song_sort_order_date -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_DATE
                }
                R.id.action_song_sort_order_composer -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.COMPOSER
                }
                R.id.action_song_sort_order_year -> {
                    PreferenceUtil.getInstance(activity).songSortOrder = SongSortOrder.SONG_YEAR
                }
            }
            return@setOnMenuItemClickListener true
        }
        popupMenu.show()
    }

    inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {
        val playAction: AppCompatImageView? = itemView.findViewById(R.id.playAction)
        val shuffleAction: AppCompatImageView? = itemView.findViewById(R.id.shuffleAction)
        val sortAction: AppCompatImageView? = itemView.findViewById(R.id.sortAction)
        val songCount: MaterialTextView? = itemView.findViewById(R.id.songCount)

        override fun onClick(v: View?) {
            if (itemViewType == OFFSET_ITEM) {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                return
            }
            super.onClick(v)
        }
    }
}