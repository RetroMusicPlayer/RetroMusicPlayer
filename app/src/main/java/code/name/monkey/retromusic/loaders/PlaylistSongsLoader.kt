package code.name.monkey.retromusic.loaders

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.Constants.BASE_SELECTION
import code.name.monkey.retromusic.model.AbsCustomPlaylist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.PlaylistSong
import code.name.monkey.retromusic.model.Song
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import java.util.*

/**
 * Created by hemanths on 16/08/17.
 */

object PlaylistSongsLoader {

    @NonNull
    fun getPlaylistSongList(@NonNull context: Context, playlist: Playlist): Observable<ArrayList<Song>> {
        return (playlist as? AbsCustomPlaylist)?.getSongs(context)
                ?: getPlaylistSongList(context, playlist.id)
    }

    @NonNull
    fun getPlaylistSongList(@NonNull context: Context, playlistId: Int): Observable<ArrayList<Song>> {
        return Observable.create { e ->
            val songs = ArrayList<Song>()
            val cursor = makePlaylistSongCursor(context, playlistId)

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    songs.add(getPlaylistSongFromCursorImpl(cursor, playlistId))
                } while (cursor.moveToNext())
            }
            cursor?.close()
            e.onNext(songs)
            e.onComplete()
        }
    }

    @NonNull
    private fun getPlaylistSongFromCursorImpl(@NonNull cursor: Cursor, playlistId: Int): PlaylistSong {
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val trackNumber = cursor.getInt(2)
        val year = cursor.getInt(3)
        val duration = cursor.getLong(4)
        val data = cursor.getString(5)
        val dateModified = cursor.getInt(6)
        val albumId = cursor.getInt(7)
        val albumName = cursor.getString(8)
        val artistId = cursor.getInt(9)
        val artistName = cursor.getString(10)
        val idInPlaylist = cursor.getInt(11)

        return PlaylistSong(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName, artistId, artistName, playlistId, idInPlaylist)
    }

    private fun makePlaylistSongCursor(@NonNull context: Context, playlistId: Int): Cursor? {
        try {
            return context.contentResolver.query(
                    MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId.toLong()),
                    arrayOf(MediaStore.Audio.Playlists.Members.AUDIO_ID, // 0
                            AudioColumns.TITLE, // 1
                            AudioColumns.TRACK, // 2
                            AudioColumns.YEAR, // 3
                            AudioColumns.DURATION, // 4
                            AudioColumns.DATA, // 5
                            AudioColumns.DATE_MODIFIED, // 6
                            AudioColumns.ALBUM_ID, // 7
                            AudioColumns.ALBUM, // 8
                            AudioColumns.ARTIST_ID, // 9
                            AudioColumns.ARTIST, // 10
                            MediaStore.Audio.Playlists.Members._ID)// 11
                    , BASE_SELECTION, null,
                    MediaStore.Audio.Playlists.Members.DEFAULT_SORT_ORDER)
        } catch (e: SecurityException) {
            return null
        }

    }
}
