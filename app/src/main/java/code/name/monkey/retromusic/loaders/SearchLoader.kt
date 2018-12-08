package code.name.monkey.retromusic.loaders

import android.content.Context
import android.text.TextUtils
import code.name.monkey.retromusic.R
import io.reactivex.Observable
import java.util.*

object SearchLoader {

    fun searchAll(context: Context, query: String?): Observable<ArrayList<Any>> {
        val results = ArrayList<Any>()
        return Observable.create { e ->
            if (!TextUtils.isEmpty(query)) {
                SongLoader.getSongs(context, query!!)
                        .subscribe { songs ->
                            if (!songs.isEmpty()) {
                                results.add(context.resources.getString(R.string.songs))
                                results.addAll(songs)
                            }
                        }

                ArtistLoader.getArtists(context, query)
                        .subscribe { artists ->
                            if (!artists.isEmpty()) {
                                results.add(context.resources.getString(R.string.artists))
                                results.addAll(artists)
                            }
                        }
                AlbumLoader.getAlbums(context, query)
                        .subscribe { albums ->
                            if (!albums.isEmpty()) {
                                results.add(context.resources.getString(R.string.albums))
                                results.addAll(albums)
                            }
                        }
            }
            e.onNext(results)
            e.onComplete()
        }
    }
}
