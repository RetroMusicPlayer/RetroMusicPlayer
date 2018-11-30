package code.name.monkey.retromusic.loaders

import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import code.name.monkey.retromusic.Constants.BASE_PROJECTION
import code.name.monkey.retromusic.Constants.BASE_SELECTION
import code.name.monkey.retromusic.helper.ShuffleHelper
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.providers.BlacklistStore
import code.name.monkey.retromusic.util.PreferenceUtil
import io.reactivex.Observable
import java.util.*

/**
 * Created by hemanths on 10/08/17.
 */


object SongLoader {

    fun getAllSongs(context: Context): Observable<ArrayList<Song>> {
        val cursor = makeSongCursor(context, null, null)
        return getSongs(cursor)
    }

    fun getSongs(context: Context, query: String): Observable<ArrayList<Song>> {
        val cursor = makeSongCursor(context, AudioColumns.TITLE + " LIKE ?", arrayOf("%$query%"))
        return getSongs(cursor)
    }

    fun getSongs(cursor: Cursor?): Observable<ArrayList<Song>> {
        return Observable.create { e ->
            val songs = ArrayList<Song>()
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    songs.add(getSongFromCursorImpl(cursor))
                } while (cursor.moveToNext())
            }

            cursor?.close()
            e.onNext(songs)
            e.onComplete()
        }
    }

    private fun getSongFromCursorImpl(cursor: Cursor): Song {
        val id = cursor.getInt(0)
        val title = cursor.getString(1)
        val trackNumber = cursor.getInt(2)
        val year = cursor.getInt(3)
        val duration = cursor.getLong(4)
        val data = cursor.getString(5)
        val dateModified = cursor.getLong(6)
        val albumId = cursor.getInt(7)
        val albumName = cursor.getString(8)
        val artistId = cursor.getInt(9)
        val artistName = cursor.getString(10)

        return Song(id, title, trackNumber, year, duration, data, dateModified, albumId, albumName,
                artistId, artistName)
    }

    @JvmOverloads
    fun makeSongCursor(context: Context, selection: String?, selectionValues: Array<String>?, sortOrder: String = PreferenceUtil.getInstance().songSortOrder): Cursor? {
        var selectionFinal = selection
        var selectionValuesFinal = selectionValues
        selectionFinal = if (selection != null && selection.trim { it <= ' ' } != "") {
            "$BASE_SELECTION AND $selectionFinal"
        } else {
            BASE_SELECTION
        }

        // Blacklist
        val paths = BlacklistStore.getInstance(context).paths
        if (!paths.isEmpty()) {
            selectionFinal = generateBlacklistSelection(selectionFinal, paths.size)
            selectionValuesFinal = addBlacklistSelectionValues(selectionValuesFinal, paths)
        }

        try {
            return context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    BASE_PROJECTION, selectionFinal, selectionValuesFinal, sortOrder)
        } catch (e: SecurityException) {
            return null
        }

    }

    private fun generateBlacklistSelection(selection: String?, pathCount: Int): String {
        val newSelection = StringBuilder(
                if (selection != null && selection.trim { it <= ' ' } != "") "$selection AND " else "")
        newSelection.append(AudioColumns.DATA + " NOT LIKE ?")
        for (i in 0 until pathCount - 1) {
            newSelection.append(" AND " + AudioColumns.DATA + " NOT LIKE ?")
        }
        return newSelection.toString()
    }

    private fun addBlacklistSelectionValues(selectionValues: Array<String>?,
                                            paths: ArrayList<String>): Array<String>? {
        var selectionValuesFinal = selectionValues
        if (selectionValuesFinal == null) {
            selectionValuesFinal = emptyArray()
        }
        val newSelectionValues = Array(selectionValuesFinal.size + paths.size) {
            "n = $it"
        }
        System.arraycopy(selectionValuesFinal, 0, newSelectionValues, 0, selectionValuesFinal.size)
        for (i in selectionValuesFinal.size until newSelectionValues.size) {
            newSelectionValues[i] = paths[i - selectionValuesFinal.size] + "%"
        }
        return newSelectionValues
    }

    fun getSong(cursor: Cursor?): Observable<Song> {
        return Observable.create { e ->
            val song: Song = if (cursor != null && cursor.moveToFirst()) {
                getSongFromCursorImpl(cursor)
            } else {
                Song.EMPTY_SONG
            }
            cursor?.close()
            e.onNext(song)
            e.onComplete()
        }
    }

    fun getSong(context: Context, queryId: Int): Observable<Song> {
        val cursor = makeSongCursor(context, AudioColumns._ID + "=?",
                arrayOf(queryId.toString()))
        return getSong(cursor)
    }

    fun suggestSongs(context: Context): Observable<ArrayList<Song>> {
        return SongLoader.getAllSongs(context)
                .flatMap {
                    val list = ArrayList<Song>()
                    ShuffleHelper.makeShuffleList(it, -1)
                    if (it.size > 9) {
                        list.addAll(it.subList(0, 9))
                    }
                    return@flatMap Observable.just(list)
                }
        /*.flatMap({ songs ->
            val list = ArrayList<Song>()
            ShuffleHelper.makeShuffleList(songs, -1)
            if (songs.size > 9) {
                list.addAll(songs.subList(0, 9))
            }
            Observable.just(list)
        } as Function<ArrayList<Song>, ObservableSource<ArrayList<Song>>>)*/
        /*.subscribe(songs -> {
                ArrayList<Song> list = new ArrayList<>();
                if (songs.isEmpty()) {
                    return;
                }
                ShuffleHelper.makeShuffleList(songs, -1);
                if (songs.size() > 10) {
                    list.addAll(songs.subList(0, 10));
                } else {
                    list.addAll(songs);
                }
               return;
            });*/
    }
}
