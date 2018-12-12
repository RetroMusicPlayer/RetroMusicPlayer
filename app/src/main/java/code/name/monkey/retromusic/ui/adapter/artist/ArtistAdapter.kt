package code.name.monkey.retromusic.ui.adapter.artist

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import java.util.*


class ArtistAdapter(val activity: AppCompatActivity,
                    var dataSet: ArrayList<Artist>,
                    @LayoutRes var itemLayoutRes: Int,
                    var usePalette: Boolean,
                    cabHolder: CabHolder?) : AbsMultiSelectAdapter<ArtistAdapter.ViewHolder, Artist>(activity, cabHolder, R.menu.menu_media_selection), FastScrollRecyclerView.SectionedAdapter {

    fun swapDataSet(dataSet: ArrayList<Artist>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    fun usePalette(usePalette: Boolean) {
        this.usePalette = usePalette
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
        return createViewHolder(view)
    }

    protected fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = dataSet[position]

        val isChecked = isChecked(artist)
        holder.itemView.isActivated = isChecked

        if (holder.title != null) {
            holder.title!!.text = artist.name
        }
        if (holder.text != null) {
            holder.text!!.visibility = View.GONE
        }
        if (holder.shortSeparator != null) {
            holder.shortSeparator!!.visibility = View.VISIBLE
        }
        loadArtistImage(artist, holder)
    }

    fun setColors(color: Int, holder: ViewHolder) {
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer!!.setBackgroundColor(color)
            if (holder.title != null) {
                holder.title!!.setTextColor(
                        MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(color)))
            }
        }
        if (holder.mask != null) {
            holder.mask!!.backgroundTintList = ColorStateList.valueOf(color)
        }
    }

    private fun loadArtistImage(artist: Artist, holder: ViewHolder) {
        if (holder.image == null) {
            return
        }
        GlideApp.with(activity)
                .asBitmapPalette()
                .load(RetroGlideExtension.getArtistModel(artist))
                .transition(RetroGlideExtension.getDefaultTransition())
                .artistOptions(artist)
                .dontAnimate()
                .into(object : RetroMusicColoredTarget(holder.image!!) {
                    override fun onColorReady(color: Int) {
                        setColors(color, holder)
                    }
                })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): Artist? {
        return dataSet[position]
    }

    override fun getName(artist: Artist): String {
        return artist.name
    }

    override fun onMultipleItemAction(menuItem: MenuItem,
                                      selection: ArrayList<Artist>) {
        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    private fun getSongList(artists: List<Artist>): ArrayList<Song> {
        val songs = ArrayList<Song>()
        for (artist in artists) {
            songs.addAll(artist.songs) // maybe async in future?
        }
        return songs
    }

    override fun getSectionName(position: Int): String {
        return MusicUtil.getSectionName(dataSet[position].name)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            setImageTransitionName(activity.getString(R.string.transition_artist_image))
            if (menu != null) {
                menu!!.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            super.onClick(v)
            if (isInQuickSelectMode) {
                toggleChecked(adapterPosition)
            } else {
                val artistPairs = arrayOf<Pair<*, *>>(Pair.create<ImageView, String>(image,
                        activity.resources.getString(R.string.transition_artist_image)))
                NavigationUtil.goToArtist(activity, dataSet[adapterPosition].id, *artistPairs)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            toggleChecked(adapterPosition)
            return super.onLongClick(v)
        }
    }
}
