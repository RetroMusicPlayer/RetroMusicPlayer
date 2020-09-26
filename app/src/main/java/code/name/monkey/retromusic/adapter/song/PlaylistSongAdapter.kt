package code.name.monkey.retromusic.adapter.song

import android.view.MenuItem
import android.view.View
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.toSongEntity
import code.name.monkey.retromusic.dialogs.RemoveSongFromPlaylistDialog
import code.name.monkey.retromusic.interfaces.ICabHolder
import code.name.monkey.retromusic.model.Song

open class PlaylistSongAdapter(
    private val playlist: PlaylistEntity,
    activity: FragmentActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
    ICabHolder: ICabHolder?
) : SongAdapter(activity, dataSet, itemLayoutRes, ICabHolder) {

    init {
        this.setMultiSelectMenuRes(R.menu.menu_cannot_delete_single_songs_playlist_songs_selection)
    }

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    open inner class ViewHolder(itemView: View) : SongAdapter.ViewHolder(itemView) {

        override var songMenuRes: Int
            get() = R.menu.menu_item_playlist_song
            set(value) {
                super.songMenuRes = value
            }

        override fun onSongMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_remove_from_playlist -> {
                    RemoveSongFromPlaylistDialog.create(song.toSongEntity(playlist.playListId))
                        .show(activity.supportFragmentManager, "REMOVE_FROM_PLAYLIST")
                    return true
                }
            }
            return super.onSongMenuItemClick(item)
        }
    }
}