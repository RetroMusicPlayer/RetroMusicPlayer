package code.name.monkey.retromusic.adapter.song

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Song
import java.util.*


abstract class AbsOffsetSongAdapter : SongAdapter {

    constructor(activity: AppCompatActivity, dataSet: ArrayList<Song>, @LayoutRes itemLayoutRes: Int, usePalette: Boolean, cabHolder: CabHolder?) : super(activity, dataSet, itemLayoutRes, usePalette, cabHolder)

    constructor(activity: AppCompatActivity, dataSet: ArrayList<Song>, @LayoutRes itemLayoutRes: Int, usePalette: Boolean, cabHolder: CabHolder?, showSectionName: Boolean) : super(activity, dataSet, itemLayoutRes, usePalette, cabHolder, showSectionName) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongAdapter.ViewHolder {
        if (viewType == OFFSET_ITEM) {
            val view = LayoutInflater.from(activity).inflate(R.layout.item_list_quick_actions, parent, false)
            return createViewHolder(view)
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun getItemId(position: Int): Long {
        var positionFinal = position
        positionFinal--
        return if (positionFinal < 0) -2 else super.getItemId(positionFinal)
    }

    override fun getIdentifier(position: Int): Song? {
        var positionFinal = position
        positionFinal--
        return if (positionFinal < 0) null else super.getIdentifier(positionFinal)
    }

    override fun getItemCount(): Int {
        val superItemCount = super.getItemCount()
        return if (superItemCount == 0) 0 else superItemCount + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) OFFSET_ITEM else SONG
    }

    override fun getSectionName(position: Int): String {
        var positionF = position
        positionF--
        return if (positionF < 0) "" else super.getSectionName(positionF)
    }

    open inner class ViewHolder(itemView: View) : SongAdapter.ViewHolder(itemView) {

        override// could also return null, just to be safe return empty song
        val song: Song
            get() = if (itemViewType == OFFSET_ITEM) Song.emptySong else dataSet[adapterPosition - 1]

        override fun onClick(v: View?) {
            if (isInQuickSelectMode && itemViewType != OFFSET_ITEM) {
                toggleChecked(adapterPosition)
            } else {
                MusicPlayerRemote.openQueue(dataSet, adapterPosition - 1, true)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            if (itemViewType == OFFSET_ITEM) return false
            toggleChecked(adapterPosition)
            return true
        }
    }

    companion object {
        const val OFFSET_ITEM = 0
        const val SONG = 1
    }
}