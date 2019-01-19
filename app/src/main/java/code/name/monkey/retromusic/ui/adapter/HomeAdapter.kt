package code.name.monkey.retromusic.ui.adapter

import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.appthemehelper.util.TintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.*
import code.name.monkey.retromusic.ui.adapter.album.AlbumFullWidthAdapter
import code.name.monkey.retromusic.ui.adapter.artist.ArtistAdapter
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.views.MetalRecyclerViewPager
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeAdapter(private val activity: AppCompatActivity, private val homes: ArrayList<Home>, private val displayMetrics: DisplayMetrics) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return homes[position].homeSection
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(activity).inflate(R.layout.section_recycler_view, parent, false)
        return when (viewType) {
            SUGGESTIONS -> SuggestionViewHolder(LayoutInflater.from(activity).inflate(R.layout.section_item_collage, parent, false))
            RECENT_ARTISTS, TOP_ARTISTS -> ArtistViewHolder(layout)
            GENRES -> GenreViewHolder(layout)
            else -> {
                AlbumViewHolder(LayoutInflater.from(activity).inflate(R.layout.metal_section_recycler_view, parent, false))
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val home = homes[position]
        when (getItemViewType(position)) {
            SUGGESTIONS -> {
                val viewHolder = holder as SuggestionViewHolder
                viewHolder.bindView(home)
            }
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
        }
    }

    override fun getItemCount(): Int {
        return homes.size
    }

    companion object {

        @IntDef(SUGGESTIONS, RECENT_ALBUMS, TOP_ALBUMS, RECENT_ARTISTS, TOP_ARTISTS, GENRES, PLAYLISTS)
        @Retention(AnnotationRetention.SOURCE)
        annotation class HomeSection

        const val SUGGESTIONS = 0
        const val RECENT_ALBUMS = 1
        const val TOP_ALBUMS = 2
        const val RECENT_ARTISTS = 3
        const val TOP_ARTISTS = 4
        const val GENRES = 5
        const val PLAYLISTS = 6

    }

    private inner class SuggestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ids: ArrayList<Int> = arrayListOf(R.id.image_1, R.id.image_2, R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6, R.id.image_7)
        private var textView: TextView = itemView.findViewById(R.id.text)
        private var playSuggestion: FloatingActionButton = itemView.findViewById(R.id.playSuggestions)
        private var title: TextView = itemView.findViewById(R.id.sectionTitle)

        fun bindView(home: Home) {
            val dataSet = home.arrayList as ArrayList<Song>
            for (id in 0 until ids.size) {
                val imageView = itemView.findViewById<ImageView>(ids[id])
                imageView.setOnClickListener {
                    MusicPlayerRemote.enqueue(dataSet[id])
                }
                GlideApp.with(activity)
                        .asBitmapPalette()
                        .load(RetroGlideExtension.getSongModel(dataSet[id]))
                        .transition(RetroGlideExtension.getDefaultTransition())
                        .songOptions(dataSet[id])
                        .into(object : RetroMusicColoredTarget(imageView) {
                            override fun onColorReady(color: Int) {

                            }
                        })
            }


            val color = ThemeStore.accentColor(activity)

            textView.apply {
                setBackgroundColor(color);
                setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))
            }
            title.text = activity.getString(home.title)

            playSuggestion.apply {
                TintHelper.setTintAuto(this, MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)), false)
                TintHelper.setTintAuto(this, color, true)
                setOnClickListener { MusicPlayerRemote.openQueue(home.arrayList as ArrayList<Song>, 0, true) }
            }
        }
    }

    private inner class AlbumViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                adapter = AlbumFullWidthAdapter(activity, home.arrayList as ArrayList<Album>, displayMetrics)
            }
            title.text = activity.getString(home.title)
        }

        val recyclerView: MetalRecyclerViewPager = view.findViewById(R.id.recyclerView)
        val title: TextView = view.findViewById(R.id.sectionTitle)

    }

    private inner class ArtistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                layoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
                val artistAdapter = ArtistAdapter(activity, home.arrayList as ArrayList<Artist>, PreferenceUtil.getInstance().getHomeGridStyle(context!!), false, null)
                adapter = artistAdapter
            }
            title.text = activity.getString(home.title)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val title: TextView = view.findViewById(R.id.sectionTitle)

    }

    private inner class GenreViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(home: Home) {
            recyclerView.apply {
                val genreAdapter = GenreAdapter(activity, home.arrayList as ArrayList<Genre>, R.layout.item_list)
                layoutManager = LinearLayoutManager(context)
                adapter = genreAdapter

            }
            title.text = activity.getString(home.title)
        }

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        val title: TextView = view.findViewById(R.id.sectionTitle)

    }
}