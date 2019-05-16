package code.name.monkey.retromusic.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumFullWidthAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.IconImageView


class HomeAdapter(private val activity: AppCompatActivity, private var homes: List<Home>, private val displayMetrics: DisplayMetrics) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return homes[position].homeSection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(activity).inflate(R.layout.section_recycler_view, parent, false)
        return when (viewType) {
            RECENT_ARTISTS, TOP_ARTISTS -> ArtistViewHolder(layout)
            GENRES -> GenreViewHolder(layout)
            PLAYLISTS -> PlaylistViewHolder(layout)
            else -> {
                AlbumViewHolder(LayoutInflater.from(activity).inflate(R.layout.metal_section_recycler_view, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val home = homes[position]
        when (getItemViewType(position)) {

            RECENT_ALBUMS, TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(home)
            }
            RECENT_ARTISTS, TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(home)
            }
            GENRES -> {
                val viewHolder = holder as GenreViewHolder
                viewHolder.bindView(home)
            }
            PLAYLISTS -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(home)
            }
        }
    }

    override fun getItemCount(): Int {
        return homes.size
    }

    fun swapData(finalList: List<Home>) {
        homes = finalList
        notifyDataSetChanged()
    }

    companion object {

        @IntDef(RECENT_ALBUMS, TOP_ALBUMS, RECENT_ARTISTS, TOP_ARTISTS, GENRES, PLAYLISTS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomeSection

        const val RECENT_ALBUMS = 0
        const val TOP_ALBUMS = 1
        const val RECENT_ARTISTS = 2
        const val TOP_ARTISTS = 3
        const val GENRES = 4
        const val PLAYLISTS = 5

    }

    private inner class AlbumViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                adapter = AlbumFullWidthAdapter(activity, home.arrayList as ArrayList<Album>, displayMetrics)
            }
            title.text = activity.getString(home.title)
            icon.setImageResource(home.icon)
        }
    }

    private inner class ArtistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                val artistAdapter = ArtistAdapter(activity, home.arrayList as ArrayList<Artist>, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
                adapter = artistAdapter
            }
            title.text = activity.getString(home.title)
            icon.setImageResource(home.icon)
        }
    }

    private inner class GenreViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                val genreAdapter = GenreAdapter(activity, home.arrayList as ArrayList<Genre>, R.layout.item_list)
                layoutManager = LinearLayoutManager(context)
                adapter = genreAdapter

            }
            title.text = activity.getString(home.title)
            icon.setImageResource(home.icon)
        }
    }

    private inner class PlaylistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(home: Home) {
            val songs = PlaylistSongsLoader.getPlaylistSongList(activity, home.arrayList[0] as Playlist).blockingFirst()
            recyclerView.apply {
                val songAdapter = SongAdapter(activity, songs, R.layout.item_album_card, false, null)
                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                adapter = songAdapter

            }
            title.text = activity.getString(home.title)
            icon.setImageResource(home.icon)
        }
    }

    private open inner class AbsHomeViewItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val title: TextView = itemView.findViewById(R.id.sectionTitle)
        val icon: IconImageView = itemView.findViewById(R.id.sectionIcon)
    }
}