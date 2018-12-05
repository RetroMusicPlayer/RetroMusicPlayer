package code.name.monkey.retromusic.helper

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import code.name.monkey.retromusic.loaders.SongLoader
import code.name.monkey.retromusic.model.Song
import java.util.*


object SearchQueryHelper {
    private val TITLE_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.TITLE + ") = ?"
    private val ALBUM_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ALBUM + ") = ?"
    private val ARTIST_SELECTION = "lower(" + MediaStore.Audio.AudioColumns.ARTIST + ") = ?"
    private val AND = " AND "
    var songs = ArrayList<Song>()

    fun getSongs(context: Context, extras: Bundle): ArrayList<Song> {
        val query = extras.getString(SearchManager.QUERY, null)
        val artistName = extras.getString(MediaStore.EXTRA_MEDIA_ARTIST, null)
        val albumName = extras.getString(MediaStore.EXTRA_MEDIA_ALBUM, null)
        val titleName = extras.getString(MediaStore.EXTRA_MEDIA_TITLE, null)

        var songs = ArrayList<Song>()
        if (artistName != null && albumName != null && titleName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ARTIST_SELECTION + AND + ALBUM_SELECTION + AND + TITLE_SELECTION, arrayOf(artistName.toLowerCase(), albumName.toLowerCase(), titleName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        if (artistName != null && titleName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ARTIST_SELECTION + AND + TITLE_SELECTION, arrayOf(artistName.toLowerCase(), titleName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        if (albumName != null && titleName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ALBUM_SELECTION + AND + TITLE_SELECTION, arrayOf(albumName.toLowerCase(), titleName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        if (artistName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ARTIST_SELECTION, arrayOf(artistName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        if (albumName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ALBUM_SELECTION, arrayOf(albumName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        if (titleName != null) {
            songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, TITLE_SELECTION, arrayOf(titleName.toLowerCase()))).blockingFirst()
        }
        if (!songs.isEmpty()) {
            return songs
        }
        songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ARTIST_SELECTION, arrayOf(query.toLowerCase()))).blockingFirst()

        if (!songs.isEmpty()) {
            return songs
        }
        songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, ALBUM_SELECTION, arrayOf(query.toLowerCase()))).blockingFirst()
        if (!songs.isEmpty()) {
            return songs
        }
        songs = SongLoader.getSongs(SongLoader.makeSongCursor(context, TITLE_SELECTION, arrayOf(query.toLowerCase()))).blockingFirst()
        return if (!songs.isEmpty()) {
            songs
        } else ArrayList()
    }


}
