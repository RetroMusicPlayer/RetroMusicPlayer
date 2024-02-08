package code.name.monkey.retromusic.helper.menu

import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistEntity
import code.name.monkey.retromusic.db.PlaylistWithSongs
import code.name.monkey.retromusic.dialogs.DeletePlaylistDialog
import org.koin.core.component.KoinComponent

object PlaylistMenuHelper: KoinComponent {
    fun handleMenuClick(
        activity: FragmentActivity,
        playlistWithSongs: List<PlaylistEntity>,
        item: MenuItem
    ): Boolean {
        return when(item.itemId) {
            R.id.action_delete_playlists -> {
                DeletePlaylistDialog.create(playlistWithSongs)
                    .show(activity.supportFragmentManager, "DELETE_PLAYLISTS")
                return true
            }
            else -> {
                false
            }
        }
    }

}