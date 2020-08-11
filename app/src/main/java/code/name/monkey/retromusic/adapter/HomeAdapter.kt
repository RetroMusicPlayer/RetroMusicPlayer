package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.EXTRA_ALBUM_ID
import code.name.monkey.retromusic.EXTRA_ARTIST_ID
import code.name.monkey.retromusic.PeekingLinearLayoutManager
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.show
import code.name.monkey.retromusic.fragments.albums.AlbumClickListener
import code.name.monkey.retromusic.fragments.artists.ArtistClickListener
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.loaders.PlaylistSongsLoader
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class HomeAdapter(
    private val activity: AppCompatActivity
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
            TOP_ALBUMS, RECENT_ALBUMS -> {
                AlbumViewHolder(
                    LayoutInflater.from(activity)
                        .inflate(R.layout.metal_section_recycler_view, parent, false)
                )
            }
            GENRES -> GenreViewHolder(layout)
            FAVOURITES -> PlaylistViewHolder(layout)
            else -> {
                SuggestionsViewHolder(
                    LayoutInflater.from(activity).inflate(
                        R.layout.item_suggestions,
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
                    R.string.recent_albums,
                    "Most recently added albums"
                )
            }
            TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Album>,
                    R.string.top_albums,
                    "Most played albums"
                )
            }
            RECENT_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Artist>,
                    R.string.recent_artists,
                    "Most recently added artists"
                )
            }
            TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Artist>,
                    R.string.top_artists,
                    "Most played artists"
                )
            }
            SUGGESTIONS -> {
                val viewHolder = holder as SuggestionsViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Song>
                )
            }
            FAVOURITES -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(
                    list[position].arrayList as List<Playlist>,
                    R.string.favorites
                )
            }
            GENRES -> {
                val viewHolder = holder as GenreViewHolder
                viewHolder.bind(list[position].arrayList as List<Genre>, R.string.genres)
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

        @IntDef(
            RECENT_ALBUMS,
            TOP_ALBUMS,
            RECENT_ARTISTS,
            TOP_ARTISTS,
            SUGGESTIONS,
            FAVOURITES,
            GENRES
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomeSection

        const val RECENT_ALBUMS = 3
        const val TOP_ALBUMS = 1
        const val RECENT_ARTISTS = 2
        const val TOP_ARTISTS = 0
        const val SUGGESTIONS = 5
        const val FAVOURITES = 4
        const val GENRES = 6
    }

    private inner class AlbumViewHolder(view: View) : AbsHomeViewItem(view), AlbumClickListener {
        fun bindView(list: List<Album>, titleRes: Int, message: String) {
            if (list.isNotEmpty()) {
                val albumAdapter = AlbumAdapter(activity, list, R.layout.pager_item, null, this)
                recyclerView.apply {
                    show()
                    adapter = albumAdapter
                    layoutManager = PeekingLinearLayoutManager(activity, HORIZONTAL, false)
                }
                title.text = activity.getString(titleRes)
            }
        }

        override fun onAlbumClick(albumId: Int, view: View) {
            activity.findNavController(R.id.fragment_container).navigate(
                R.id.albumDetailsFragment,
                bundleOf(EXTRA_ALBUM_ID to albumId),
                null,
                FragmentNavigatorExtras(
                    view to activity.getString(R.string.transition_album_art)
                )
            )
        }
    }

    private inner class ArtistViewHolder(view: View) : AbsHomeViewItem(view), ArtistClickListener {
        fun bindView(list: List<Artist>, titleRes: Int, message: String) {
            if (list.isNotEmpty()) {
                val manager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
                val artistAdapter = ArtistAdapter(
                    activity,
                    list,
                    PreferenceUtil.homeGridStyle,
                    null,
                    this
                )
                recyclerView.apply {
                    show()
                    layoutManager = manager
                    adapter = artistAdapter
                }
                title.text = activity.getString(titleRes)
            }
        }

        override fun onArtist(artistId: Int) {
            activity.findNavController(R.id.fragment_container).navigate(
                R.id.artistDetailsFragment,
                bundleOf(EXTRA_ARTIST_ID to artistId)
            )
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
            itemView.findViewById<TextView>(R.id.message).setTextColor(color)
            itemView.findViewById<MaterialCardView>(R.id.card6).apply {
                setCardBackgroundColor(ColorUtil.withAlpha(color, 0.2f))
            }
            if (arrayList.size > 9)
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
            text.text = "You're all time favorites"
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

    private inner class GenreViewHolder(itemView: View) : AbsHomeViewItem(itemView) {
        fun bind(genres: List<Genre>, titleRes: Int) {
            title.text = activity.getString(titleRes)
            text.text = "Genres for you"
            recyclerView.apply {
                show()
                layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false)
                val genreAdapter = GenreAdapter(activity, genres, R.layout.item_grid_genre)
                adapter = genreAdapter
            }
        }
    }

    open inner class AbsHomeViewItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val title: AppCompatTextView = itemView.findViewById(R.id.title)
        val text: AppCompatTextView = itemView.findViewById(R.id.text)
    }
}
