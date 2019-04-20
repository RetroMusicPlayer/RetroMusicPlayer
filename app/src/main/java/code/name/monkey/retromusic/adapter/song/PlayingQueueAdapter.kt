package code.name.monkey.retromusic.adapter.song

import android.graphics.Color
import android.graphics.PorterDuff
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.ViewUtil
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemViewHolder
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange
import com.h6ah4i.android.widget.advrecyclerview.draggable.annotation.DraggableItemStateFlags
import java.util.*


class PlayingQueueAdapter : SongAdapter, DraggableItemAdapter<PlayingQueueAdapter.ViewHolder> {

    private var current: Int = 0
    private var color = -1

    constructor(activity: AppCompatActivity, dataSet: ArrayList<Song>, current: Int,
                @LayoutRes itemLayoutRes: Int) : super(activity, dataSet, itemLayoutRes, false, null) {
        this.current = current
    }

    constructor(activity: AppCompatActivity,
                dataSet: ArrayList<Song>, current: Int,
                @LayoutRes itemLayoutRes: Int,
                @ColorInt color: Int) : super(activity, dataSet, itemLayoutRes, false, null) {
        this.current = current
        this.color = color
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (holder.imageText != null) {
            holder.imageText!!.text = (position - current).toString()
        }
        if (holder.time != null) {
            holder.time!!.text = MusicUtil.getReadableDurationString(dataSet[position].duration)
        }
        if (holder.itemViewType == HISTORY || holder.itemViewType == CURRENT) {
            setAlpha(holder, 0.5f)
        }
        if (usePalette) {
            setColor(holder, Color.WHITE)
        }
    }

    private fun setColor(holder: SongAdapter.ViewHolder, white: Int) {

        if (holder.title != null) {
            holder.title!!.setTextColor(white)
            if (color != -1) {
                holder.title!!.setTextColor(color)
            }
        }
        if (holder.text != null) {
            holder.text!!.setTextColor(white)
        }
        if (holder.time != null) {
            holder.time!!.setTextColor(white)
        }
        if (holder.imageText != null) {
            holder.imageText!!.setTextColor(white)
        }
        if (holder.menu != null) {
            (holder.menu as ImageView).setColorFilter(white, PorterDuff.Mode.SRC_IN)
        }
    }

    override fun usePalette(usePalette: Boolean) {
        super.usePalette(usePalette)
        this.usePalette = usePalette
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (position < current) {
            return HISTORY
        } else if (position > current) {
            return UP_NEXT
        }
        return CURRENT
    }

    override fun loadAlbumCover(song: Song, holder: SongAdapter.ViewHolder) {
        // We don't want to load it in this adapter
    }

    fun swapDataSet(dataSet: ArrayList<Song>, position: Int) {
        this.dataSet = dataSet
        current = position
        notifyDataSetChanged()
    }

    fun setCurrent(current: Int) {
        this.current = current
        notifyDataSetChanged()
    }

    private fun setAlpha(holder: SongAdapter.ViewHolder, alpha: Float) {
        if (holder.image != null) {
            holder.image!!.alpha = alpha
        }
        if (holder.title != null) {
            holder.title!!.alpha = alpha
        }
        if (holder.text != null) {
            holder.text!!.alpha = alpha
        }
        if (holder.imageText != null) {
            holder.imageText!!.alpha = alpha
        }
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer!!.alpha = alpha
        }
    }

    override fun onCheckCanStartDrag(holder: ViewHolder, position: Int, x: Int, y: Int): Boolean {
        return ViewUtil.hitTest(holder.imageText!!, x, y) || ViewUtil.hitTest(holder.dragView!!, x, y)
    }

    override fun onGetItemDraggableRange(holder: ViewHolder, position: Int): ItemDraggableRange? {
        return null
    }

    override fun onMoveItem(fromPosition: Int, toPosition: Int) {
        MusicPlayerRemote.moveSong(fromPosition, toPosition)
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

    inner class ViewHolder(itemView: View) : SongAdapter.ViewHolder(itemView), DraggableItemViewHolder {

        @DraggableItemStateFlags
        private var mDragStateFlags: Int = 0

        override var songMenuRes: Int
            get() = R.menu.menu_item_playing_queue_song
            set(value: Int) {
                super.songMenuRes = value
            }

        init {
            if (imageText != null) {
                imageText!!.visibility = View.VISIBLE
            }
            if (dragView != null) {
                dragView!!.visibility = View.VISIBLE
            }
        }

        override fun onSongMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_remove_from_playing_queue -> {
                    MusicPlayerRemote.removeFromQueue(adapterPosition)
                    return true
                }
            }
            return super.onSongMenuItemClick(item)
        }

        @DraggableItemStateFlags
        override fun getDragStateFlags(): Int {
            return mDragStateFlags
        }

        override fun setDragStateFlags(@DraggableItemStateFlags flags: Int) {
            mDragStateFlags = flags
        }
    }

    companion object {

        private const val HISTORY = 0
        private const val CURRENT = 1
        private const val UP_NEXT = 2
    }
}
