package code.name.monkey.retromusic.helper

import android.content.Context

import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.ArrayList

import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.AbsCustomPlaylist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.model.Song
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

class M3UWriter : M3UConstants {
    companion object {
        val TAG: String = M3UWriter::class.java.simpleName

        fun write(context: Context,
                  dir: File, playlist: Playlist): Observable<File> {
            if (!dir.exists())

                dir.mkdirs()
            val file = File(dir, playlist.name + ("." + M3UConstants.EXTENSION))

            return if (playlist is AbsCustomPlaylist) {
                Observable.create { e -> playlist.getSongs(context).subscribe { songs -> saveSongsToFile(file, e, songs) } }
            } else
                Observable.create { e -> PlaylistSongsLoader.getPlaylistSongList(context, playlist.id).subscribe { songs -> saveSongsToFile(file, e, songs) } }
        }

        @Throws(IOException::class)
        private fun saveSongsToFile(file: File, e: ObservableEmitter<File>, songs: ArrayList<Song>) {
            if (songs.size > 0) {
                val bw = BufferedWriter(FileWriter(file))
                bw.write(M3UConstants.HEADER)
                for (song in songs) {
                    bw.newLine()
                    bw.write(M3UConstants.ENTRY + song.duration + M3UConstants.DURATION_SEPARATOR + song.artistName + " - " + song.title)
                    bw.newLine()
                    bw.write(song.data)
                }

                bw.close()
            }
            e.onNext(file)
            e.onComplete()
        }
    }
}