package code.name.monkey.retromusic.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumFullWidthAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Playlist
import code.name.monkey.retromusic.providers.interfaces.Repository
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.textview.MaterialTextView


class HomeAdapter(
        private val activity: AppCompatActivity,
        private val displayMetrics: DisplayMetrics,
        private val repository: Repository
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> TOP_ARTISTS
            1 -> TOP_ALBUMS
            2 -> RECENT_ARTISTS
            3 -> RECENT_ALBUMS
            4 -> PLAYLISTS
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(activity).inflate(R.layout.section_recycler_view, parent, false)
        return when (viewType) {
            RECENT_ARTISTS, TOP_ARTISTS -> ArtistViewHolder(layout)
            PLAYLISTS -> PlaylistViewHolder(layout)
            else -> {
                AlbumViewHolder(LayoutInflater.from(activity).inflate(R.layout.metal_section_recycler_view, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println(getItemViewType(position))
        when (getItemViewType(position)) {
            RECENT_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(repository.recentAlbums(), R.string.recent_albums, R.string.recent_added_albums)
            }
            TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder

                viewHolder.bindView(repository.topAlbums(), R.string.top_albums, R.string.most_played_albums)
            }
            RECENT_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(repository.recentArtists(), R.string.recent_artists, R.string.recent_added_artists)
            }
            TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder

                viewHolder.bindView(repository.recentArtists(), R.string.top_artists, R.string.most_played_artists)
            }
            PLAYLISTS -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(repository.favoritePlaylist, R.string.favorites, R.string.favorites_songs)
            }
        }
    }

    override fun getItemCount(): Int {
        return 5
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
        fun bindView(list: ArrayList<Album>, titleRes: Int, subtitleRes: Int) {
            if (list.isNotEmpty()) {
                recyclerView.apply {
                    show()
                    adapter = AlbumFullWidthAdapter(activity, list, displayMetrics)
                }
                titleContainer . show ()
                title.text = activity.getString(titleRes)
                text.text = activity.getString(subtitleRes)
            }
        }
    }

    inner class ArtistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(list: ArrayList<Artist>, titleRes: Int, subtitleRes: Int) {
            if (list.isNotEmpty()) {
                recyclerView.apply {
                    show()
                    layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                    val artistAdapter = ArtistAdapter(activity, list,
                            PreferenceUtil.getInstance(activity).getHomeGridStyle(activity), false, null)
                    adapter = artistAdapter
                }
                titleContainer.show()
                title.text = activity.getString(titleRes)
                text.text = activity.getString(subtitleRes)
            }
        }
    }

    private inner class PlaylistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(arrayList: ArrayList<Playlist>, titleRes: Int, subtitleRes: Int) {
            if (arrayList.isNotEmpty()) {
                val songs = PlaylistSongsLoader.getPlaylistSongList(activity, arrayList[0])
                if (songs.isNotEmpty()) {
                    recyclerView.apply {
                        show()
                        val songAdapter = SongAdapter(activity, songs, R.layout.item_album_card, false, null)
                        layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                        adapter = songAdapter

                    }
                    titleContainer.show()
                    title.text = activity.getString(titleRes)
                    text.text = activity.getString(subtitleRes)
                }
            }
        }
    }

    open inner class AbsHomeViewItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val titleContainer: View = itemView.findViewById(R.id.titleContainer)
        val title: MaterialTextView = itemView.findViewById(R.id.title)
        val text: MaterialTextView = itemView.findViewById(R.id.text)
    }
}