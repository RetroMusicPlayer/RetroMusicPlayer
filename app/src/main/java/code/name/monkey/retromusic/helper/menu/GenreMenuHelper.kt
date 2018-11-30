package code.name.monkey.retromusic.helper.menu

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem

import java.util.ArrayList

import code.name.monkey.retromusic.loaders.GenreLoader
import code.name.monkey.retromusic.model.Genre
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.AddToPlaylistDialog
import code.name.monkey.retromusic.helper.MusicPlayerRemote

/**
 * @author Hemanth S (h4h13).
 */

object GenreMenuHelper {
    fun handleMenuClick(activity: AppCompatActivity,
                        genre: Genre,
                        item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_play -> {
                MusicPlayerRemote.openQueue(getGenreSongs(activity, genre), 0, true)
                return true
            }
            R.id.action_play_next -> {
                MusicPlayerRemote.playNext(getGenreSongs(activity, genre))
                return true
            }
            R.id.action_add_to_playlist -> {
                AddToPlaylistDialog.create(getGenreSongs(activity, genre))
                        .show(activity.supportFragmentManager, "ADD_PLAYLIST")
                return true
            }
            R.id.action_add_to_current_playing -> {
                MusicPlayerRemote.enqueue(getGenreSongs(activity, genre))
                return true
            }
        }
        return false
    }

    private fun getGenreSongs(activity: Activity,
                              genre: Genre): ArrayList<Song> {
        val songs: ArrayList<Song>
        songs = GenreLoader.getSongs(activity, genre.id).blockingFirst()
        return songs
    }
}
