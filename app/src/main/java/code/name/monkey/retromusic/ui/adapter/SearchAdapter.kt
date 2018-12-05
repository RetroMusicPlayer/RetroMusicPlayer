package code.name.monkey.retromusic.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.ArtistGlideRequest
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.menu.SongMenuHelper
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import com.bumptech.glide.Glide
import java.util.*


class SearchAdapter(private val activity: AppCompatActivity, private var dataSet: List<Any>?) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    fun swapDataSet(dataSet: ArrayList<Any>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet!![position] is Album) return ALBUM
        if (dataSet!![position] is Artist) return ARTIST
        return if (dataSet!![position] is Song) SONG else HEADER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == HEADER) ViewHolder(LayoutInflater.from(activity).inflate(R.layout.sub_header, parent, false), viewType) else ViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_list, parent, false), viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ALBUM -> {
                val album = dataSet!![position] as Album
                holder.title!!.text = album.title
                holder.text!!.text = album.artistName
                SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                        .checkIgnoreMediaStore(activity).build()
                        .into(holder.image!!)
            }
            ARTIST -> {
                val artist = dataSet!![position] as Artist
                holder.title!!.text = artist.name
                holder.text!!.text = MusicUtil.getArtistInfoString(activity, artist)
                ArtistGlideRequest.Builder.from(Glide.with(activity), artist)
                        .build().into(holder.image!!)
            }
            SONG -> {
                val song = dataSet!![position] as Song
                holder.title!!.text = song.title
                holder.text!!.text = song.albumName
            }
            else -> {
                holder.title!!.text = dataSet!![position].toString()
                holder.title!!.setTextColor(ThemeStore.accentColor(activity))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet!!.size
    }

    inner class ViewHolder(itemView: View, itemViewType: Int) : MediaEntryViewHolder(itemView) {
        init {
            itemView.setOnLongClickListener(null)

            if (itemViewType != HEADER) {
                if (separator != null) {
                    separator!!.visibility = View.GONE
                }
            }

            if (menu != null) {
                if (itemViewType == SONG) {
                    menu!!.visibility = View.VISIBLE
                    menu!!.setOnClickListener(object : SongMenuHelper.OnClickSongMenu(activity) {
                        override val song: Song
                            get() = dataSet!![adapterPosition] as Song
                    })
                } else {
                    menu!!.visibility = View.GONE
                }
            }

            when (itemViewType) {
                ALBUM -> setImageTransitionName(activity.getString(R.string.transition_album_art))
                ARTIST -> setImageTransitionName(activity.getString(R.string.transition_artist_image))
                else -> {
                    val container = itemView.findViewById<View>(R.id.image_container)
                    if (container != null) {
                        container.visibility = View.GONE
                    }
                }
            }
        }

        override fun onClick(v: View?) {
            val item = dataSet!![adapterPosition]
            when (itemViewType) {
                ALBUM -> NavigationUtil.goToAlbum(activity,
                        (item as Album).id, Pair.create(image, activity.resources.getString(R.string.transition_album_art)))
                ARTIST -> NavigationUtil.goToArtist(activity,
                        (item as Artist).id, Pair.create(image, activity.resources.getString(R.string.transition_artist_image)))
                SONG -> {
                    val playList = ArrayList<Song>()
                    playList.add(item as Song)
                    MusicPlayerRemote.openQueue(playList, 0, true)
                }
            }
        }
    }

    companion object {

        private val HEADER = 0
        private val ALBUM = 1
        private val ARTIST = 2
        private val SONG = 3
    }
}
