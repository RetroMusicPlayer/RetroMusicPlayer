package code.name.monkey.retromusic.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumFullWidthAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide

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
            SUGGESTIONS -> {
                SuggestionsViewHolder(
                    LayoutInflater.from(activity).inflate(
                        R.layout.item_suggestions,
                        parent,
                        false
                    )
                )
            }
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
                viewHolder.bindView(
                    list[position].arrayList as List<Album>,
                    R.string.recent_albums
                )
            }
            TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Album>,
                    R.string.top_albums
                )
            }
            RECENT_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Artist>,
                    R.string.recent_artists
                )
            }
            TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(list[position].arrayList as List<Artist>, R.string.top_artists)
            }
            PLAYLISTS -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Playlist>,
                    R.string.favorites
                )
            }
            SUGGESTIONS -> {
                val viewHolder = holder as SuggestionsViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Song>
                )
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

        @IntDef(RECENT_ALBUMS, TOP_ALBUMS, RECENT_ARTISTS, TOP_ARTISTS, PLAYLISTS, SUGGESTIONS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomeSection

        const val RECENT_ALBUMS = 3
        const val TOP_ALBUMS = 1
        const val RECENT_ARTISTS = 2
        const val TOP_ARTISTS = 0
        const val SUGGESTIONS = 4
        const val PLAYLISTS = 5
    }

    private inner class AlbumViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(list: List<Album>, titleRes: Int) {
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
        fun bindView(list: List<Artist>, titleRes: Int) {
            if (list.isNotEmpty()) {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                val artistAdapter = ArtistAdapter(
                    activity,
                    list,
                    PreferenceUtil.homeGridStyle,
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

    private inner class SuggestionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val images = listOf(
            R.id.image1,
            R.id.image2,
            R.id.image3,
            R.id.image4,
            R.id.image5,
            R.id.image6,
            R.id.image7,
            R.id.image8
        )

        fun bindView(arrayList: List<Song>) {
            val color = ThemeStore.accentColor(activity)
            itemView.findViewById<TextView>(R.id.text).setTextColor(color)

            images.forEachIndexed { index, i ->
                itemView.findViewById<View>(i).setOnClickListener {
                    MusicPlayerRemote.playNext(arrayList[index])
                }
                SongGlideRequest.Builder.from(Glide.with(activity), arrayList[index])
                    .asBitmap()
                    .build()
                    .into(itemView.findViewById(i))

            }
        }
    }

    private inner class PlaylistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(arrayList: List<Playlist>, titleRes: Int) {
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
