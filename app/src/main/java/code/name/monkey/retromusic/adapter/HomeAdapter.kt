package code.name.monkey.retromusic.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumFullWidthAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Home
import code.name.monkey.retromusic.model.Playlist

import code.name.monkey.retromusic.util.PreferenceUtilKT

class HomeAdapter(
    private val activity: AppCompatActivity,
    private val displayMetrics: DisplayMetrics
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = listOf<Home>()

    override fun getItemViewType(position: Int): Int {
        return list[position].homeSection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(activity)
            .inflate(R.layout.section_recycler_view, parent, false)
        return when (viewType) {
            RECENT_ARTISTS, TOP_ARTISTS -> ArtistViewHolder(layout)
            PLAYLISTS -> PlaylistViewHolder(layout)
            else -> {
                AlbumViewHolder(
                    LayoutInflater.from(activity).inflate(
                        R.layout.metal_section_recycler_view,
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            RECENT_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(list[position].arrayList.toAlbums(), R.string.recent_albums)
            }
            TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(list[position].arrayList.toAlbums(), R.string.top_albums)
            }
            RECENT_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(list[position].arrayList.toArtists(), R.string.recent_artists)
            }
            TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(list[position].arrayList.toArtists(), R.string.top_artists)
            }
            PLAYLISTS -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(list[position].arrayList.toPlaylist(), R.string.favorites)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun swapData(sections: List<Home>) {
        list = sections
        notifyDataSetChanged()
    }

    companion object {

        @IntDef(RECENT_ALBUMS, TOP_ALBUMS, RECENT_ARTISTS, TOP_ARTISTS, PLAYLISTS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomeSection

        const val RECENT_ALBUMS = 3
        const val TOP_ALBUMS = 1
        const val RECENT_ARTISTS = 2
        const val TOP_ARTISTS = 0
        const val PLAYLISTS = 4
    }

    private inner class AlbumViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(list: ArrayList<Album>, titleRes: Int) {
            if (list.isNotEmpty()) {
                recyclerView.apply {
                    show()
                    adapter = AlbumFullWidthAdapter(activity, list, displayMetrics)
                }
                title.text = activity.getString(titleRes)
            }
        }
    }

    inner class ArtistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(list: ArrayList<Artist>, titleRes: Int) {
            if (list.isNotEmpty()) {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                val artistAdapter = ArtistAdapter(
                    activity,
                    list,
                    PreferenceUtilKT.homeGridStyle,
                    null
                )
                recyclerView.apply {
                    show()
                    layoutManager = manager
                    adapter = artistAdapter
                }
                title.text = activity.getString(titleRes)
            }
        }
    }

    private inner class PlaylistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(arrayList: ArrayList<Playlist>, titleRes: Int) {
            if (arrayList.isNotEmpty()) {
                val songs = PlaylistSongsLoader.getPlaylistSongList(activity, arrayList[0])
                if (songs.isNotEmpty()) {
                    recyclerView.apply {
                        show()
                        val songAdapter =
                            SongAdapter(activity, songs, R.layout.item_album_card, null)
                        layoutManager =
                            GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                        adapter = songAdapter
                    }
                    title.text = activity.getString(titleRes)
                }
            }
        }
    }

    open inner class AbsHomeViewItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val title: AppCompatTextView = itemView.findViewById(R.id.title)
    }
}

private fun <E> ArrayList<E>.toAlbums(): ArrayList<Album> {
    val arrayList = ArrayList<Album>()
    for (x in this) {
        arrayList.add(x as Album)
    }
    return arrayList
}

private fun <E> ArrayList<E>.toArtists(): ArrayList<Artist> {
    val arrayList = ArrayList<Artist>()
    for (x in this) {
        arrayList.add(x as Artist)
    }
    return arrayList
}

private fun <E> ArrayList<E>.toPlaylist(): ArrayList<Playlist> {
    val arrayList = ArrayList<Playlist>()
    for (x in this) {
        arrayList.add(x as Playlist)
    }
    return arrayList
}

