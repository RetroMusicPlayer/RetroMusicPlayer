package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.retromusic.*
import code.name.monkey.retromusic.adapter.album.AlbumAdapter
import code.name.monkey.retromusic.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.adapter.song.SongAdapter
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.fragments.albums.AlbumClickListener
import code.name.monkey.retromusic.fragments.artists.ArtistClickListener
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView

class HomeAdapter(
    private val activity: AppCompatActivity
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ArtistClickListener, AlbumClickListener {

    private var list = listOf<Home>()

    override fun getItemViewType(position: Int): Int {
        return list[position].homeSection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(activity)
            .inflate(R.layout.section_recycler_view, parent, false)
        return when (viewType) {
            RECENT_ARTISTS, TOP_ARTISTS -> ArtistViewHolder(layout)
            GENRES -> GenreViewHolder(layout)
            FAVOURITES -> PlaylistViewHolder(layout)
            TOP_ALBUMS, RECENT_ALBUMS -> AlbumViewHolder(layout)
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
        val home = list[position]
        when (getItemViewType(position)) {
            RECENT_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(home.arrayList as List<Album>, R.string.recent_albums)
                viewHolder.clickableArea.setOnClickListener {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.detailListFragment,
                        bundleOf("type" to RECENT_ALBUMS)
                    )
                }
            }
            TOP_ALBUMS -> {
                val viewHolder = holder as AlbumViewHolder
                viewHolder.bindView(home.arrayList as List<Album>, R.string.top_albums)
                viewHolder.clickableArea.setOnClickListener {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.detailListFragment,
                        bundleOf("type" to TOP_ALBUMS)
                    )
                }
            }
            RECENT_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(home.arrayList, R.string.recent_artists)
                viewHolder.clickableArea.setOnClickListener {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.detailListFragment,
                        bundleOf("type" to RECENT_ARTISTS)
                    )
                }
            }
            TOP_ARTISTS -> {
                val viewHolder = holder as ArtistViewHolder
                viewHolder.bindView(home.arrayList, R.string.top_artists)
                viewHolder.clickableArea.setOnClickListener {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.detailListFragment,
                        bundleOf("type" to TOP_ARTISTS)
                    )
                }
            }
            SUGGESTIONS -> {
                val viewHolder = holder as SuggestionsViewHolder
                viewHolder.bindView(home.arrayList)
            }
            FAVOURITES -> {
                val viewHolder = holder as PlaylistViewHolder
                viewHolder.bindView(home.arrayList, R.string.favorites)
                viewHolder.clickableArea.setOnClickListener {
                    activity.findNavController(R.id.fragment_container).navigate(
                        R.id.detailListFragment,
                        bundleOf("type" to FAVOURITES)
                    )
                }
            }
            GENRES -> {
                val viewHolder = holder as GenreViewHolder
                viewHolder.bind(home.arrayList, R.string.genres)
            }
            PLAYLISTS -> {

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

    private inner class AlbumViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(albums: List<Album>, titleRes: Int) {
            title.text = activity.getString(titleRes)
            recyclerView.apply {
                adapter = albumAdapter(albums)
                layoutManager = gridLayoutManager()
            }
        }
    }

    private inner class ArtistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(artists: List<Any>, titleRes: Int) {
            recyclerView.apply {
                layoutManager = linearLayoutManager()
                adapter = artistsAdapter(artists as List<Artist>)
            }
            title.text = activity.getString(titleRes)
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

        fun bindView(songs: List<Any>) {
            songs as List<Song>
            val color = ThemeStore.accentColor(activity)
            itemView.findViewById<TextView>(R.id.message).setTextColor(color)
            itemView.findViewById<MaterialCardView>(R.id.card6).apply {
                setCardBackgroundColor(ColorUtil.withAlpha(color, 0.12f))
            }
            images.forEachIndexed { index, id ->
                itemView.findViewById<View>(id).setOnClickListener {
                    MusicPlayerRemote.playNext(songs[index])
                }
                SongGlideRequest.Builder.from(Glide.with(activity), songs[index])
                    .asBitmap()
                    .build()
                    .into(itemView.findViewById(id))

            }
        }
    }

    private inner class PlaylistViewHolder(view: View) : AbsHomeViewItem(view) {
        fun bindView(songs: List<Any>, titleRes: Int) {
            recyclerView.apply {
                val songAdapter = SongAdapter(
                    activity,
                    songs as MutableList<Song>,
                    R.layout.item_album_card, null
                )
                layoutManager = linearLayoutManager()
                adapter = songAdapter
            }
            title.text = activity.getString(titleRes)
        }
    }

    private inner class GenreViewHolder(itemView: View) : AbsHomeViewItem(itemView) {
        fun bind(genres: List<Any>, titleRes: Int) {
            arrow.hide()
            title.text = activity.getString(titleRes)
            recyclerView.apply {
                layoutManager = GridLayoutManager(activity, 3, GridLayoutManager.HORIZONTAL, false)
                val genreAdapter =
                    GenreAdapter(activity, genres as List<Genre>, R.layout.item_grid_genre)
                adapter = genreAdapter
            }
        }
    }

    open class AbsHomeViewItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recyclerView: RecyclerView = itemView.findViewById(R.id.recyclerView)
        val title: AppCompatTextView = itemView.findViewById(R.id.title)
        val arrow: ImageView = itemView.findViewById(R.id.arrow)
        val clickableArea: ViewGroup = itemView.findViewById(R.id.clickable_area)
    }

    fun artistsAdapter(artists: List<Artist>) =
        ArtistAdapter(activity, artists, PreferenceUtil.homeGridStyle, null, this)

    fun albumAdapter(albums: List<Album>) =
        AlbumAdapter(activity, albums, R.layout.item_image, null, this)

    fun gridLayoutManager() = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
    fun linearLayoutManager() = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

    override fun onArtist(artistId: Int, imageView: ImageView) {
        activity.findNavController(R.id.fragment_container).navigate(
            R.id.artistDetailsFragment,
            bundleOf(EXTRA_ARTIST_ID to artistId),
            null,
            FragmentNavigatorExtras(
                imageView to activity.getString(R.string.transition_album_art)
            )
        )
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
