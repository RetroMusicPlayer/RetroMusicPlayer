package code.name.monkey.retromusic.ui.adapter.song

import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import java.util.*


open class PlaylistSongAdapter(activity: AppCompatActivity, dataSet: ArrayList<Song>, @LayoutRes itemLayoutRes: Int, usePalette: Boolean, cabHolder: CabHolder?) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, usePalette, cabHolder, false) {

    init {
        this.setMultiSelectMenuRes(R.menu.menu_cannot_delete_single_songs_playlist_songs_selection)
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == AbsOffsetSongAdapter.OFFSET_ITEM) {
            val textColor = ThemeStore.textColorSecondary(activity)
            if (holder.title != null) {
                holder.title!!.text = MusicUtil.getPlaylistInfoString(activity, dataSet)
                holder.title!!.setTextColor(textColor)
            }


            if (holder.text != null) {
                holder.text!!.visibility = View.GONE
            }
            if (holder.menu != null) {
                holder.menu!!.visibility = View.GONE
            }
            if (holder.image != null) {
                val padding = activity.resources.getDimensionPixelSize(R.dimen.default_item_margin) / 2
                holder.image!!.setPadding(padding, padding, padding, padding)
                holder.image!!.setColorFilter(textColor)
                holder.image!!.setImageResource(R.drawable.ic_timer_white_24dp)
            }
            if (holder.dragView != null) {
                holder.dragView!!.visibility = View.GONE
            }
            if (holder.separator != null) {
                holder.separator!!.visibility = View.GONE
            }
            if (holder.shortSeparator != null) {
                holder.shortSeparator!!.visibility = View.GONE
            }
        } else {
            super.onBindViewHolder(holder, position - 1)
        }
    }

    open inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {

        override var songMenuRes: Int
            get() = R.menu.menu_item_cannot_delete_single_songs_playlist_song
            set(value) {
                super.songMenuRes = value
            }

        override fun onSongMenuItemClick(item: MenuItem): Boolean {
            if (item.itemId == R.id.action_go_to_album) {
                val albumPairs = arrayOf<Pair<*, *>>(Pair.create(image, activity.getString(R.string.transition_album_art)))
                NavigationUtil.goToAlbum(activity, dataSet[adapterPosition - 1].albumId, *albumPairs)
                return true
            }
            return super.onSongMenuItemClick(item)
        }
    }

    companion object {

        val TAG = PlaylistSongAdapter::class.java.simpleName
    }
}