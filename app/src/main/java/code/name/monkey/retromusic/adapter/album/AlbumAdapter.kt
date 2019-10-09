package code.name.monkey.retromusic.adapter.album

import android.app.ActivityOptions
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.helper.SortOrder
import code.name.monkey.retromusic.helper.menu.SongsMenuHelper
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.NavigationUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import com.bumptech.glide.Glide
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView


open class AlbumAdapter(protected val activity: AppCompatActivity,
                        dataSet: ArrayList<Album>,
                        @param:LayoutRes protected var itemLayoutRes: Int,
                        usePalette: Boolean,
                        cabHolder: CabHolder?) : AbsMultiSelectAdapter<AlbumAdapter.ViewHolder, Album>(activity, cabHolder, code.name.monkey.retromusic.R.menu.menu_media_selection), FastScrollRecyclerView.SectionedAdapter {
    var dataSet: ArrayList<Album>
        protected set

    protected var usePalette = false


    init {
        this.dataSet = dataSet
        this.usePalette = usePalette
        this.setHasStableIds(true)
    }

    fun useItemLayout(itemLayoutRes: Int) {
        this.itemLayoutRes = itemLayoutRes
        notifyDataSetChanged()
    }

    fun usePalette(usePalette: Boolean) {
        this.usePalette = usePalette
        notifyDataSetChanged()
    }

    fun swapDataSet(dataSet: ArrayList<Album>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
        return createViewHolder(view, viewType)
    }

    protected open fun createViewHolder(view: View, viewType: Int): ViewHolder {
        return ViewHolder(view)
    }

    private fun getAlbumTitle(album: Album): String? {
        return album.title
    }

    protected open fun getAlbumText(album: Album): String? {
        return album.artistName
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val album = dataSet[position]

        val isChecked = isChecked(album)
        holder.itemView.isActivated = isChecked

        if (holder.title != null) {
            holder.title!!.text = getAlbumTitle(album)
        }
        if (holder.text != null) {
            holder.text!!.text = getAlbumText(album)
        }
        if (holder.playSongs != null) {
            holder.playSongs!!.setOnClickListener { MusicPlayerRemote.openQueue(album.songs!!, 0, true) }
        }
        loadAlbumCover(album, holder)
    }

    protected open fun setColors(color: Int, holder: ViewHolder) {
        if (holder.paletteColorContainer != null) {
            holder.title?.setTextColor(
                    MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(color)))
            holder.text?.setTextColor(MaterialValueHelper.getSecondaryTextColor(activity, ColorUtil.isColorLight(color)))
            holder.paletteColorContainer?.setBackgroundColor(color)
        }

        holder.mask?.backgroundTintList = ColorStateList.valueOf(color)
    }

    protected open fun loadAlbumCover(album: Album, holder: ViewHolder) {
        if (holder.image == null) {
            return
        }

        SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build()
                .into(object : RetroMusicColoredTarget(holder.image!!) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
                        setColors(defaultFooterColor, holder)
                    }

                    override fun onColorReady(color: Int) {
                        setColors(color, holder)
                    }
                })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.toLong()
    }

    override fun getIdentifier(position: Int): Album? {
        return dataSet[position]
    }

    override fun getName(album: Album): String {
        return album.title!!
    }

    override fun onMultipleItemAction(menuItem: MenuItem,
                                      selection: ArrayList<Album>) {
        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    private fun getSongList(albums: List<Album>): ArrayList<Song> {
        val songs = ArrayList<Song>()
        for (album in albums) {
            songs.addAll(album.songs!!)
        }
        return songs
    }

    override fun getSectionName(position: Int): String {
        var sectionName: String? = null
        when (PreferenceUtil.getInstance(activity).albumSortOrder) {
            SortOrder.AlbumSortOrder.ALBUM_A_Z, SortOrder.AlbumSortOrder.ALBUM_Z_A -> sectionName = dataSet[position].title
            SortOrder.AlbumSortOrder.ALBUM_ARTIST -> sectionName = dataSet[position].artistName
            SortOrder.AlbumSortOrder.ALBUM_YEAR -> return MusicUtil.getYearString(dataSet[position].year)
        }

        return MusicUtil.getSectionName(sectionName)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            setImageTransitionName(activity.getString(code.name.monkey.retromusic.R.string.transition_album_art))
            if (menu != null) {
                menu!!.visibility = View.GONE
            }
        }

        override fun onClick(v: View?) {
            super.onClick(v)
            if (isInQuickSelectMode) {
                toggleChecked(adapterPosition)
            } else {
                val activityOptions = ActivityOptions.makeSceneTransitionAnimation(activity, image, activity.getString(R.string.transition_album_art))
                NavigationUtil.goToAlbumOptions(activity, dataSet[adapterPosition].id, activityOptions)
            }
        }

        override fun onLongClick(v: View?): Boolean {
            toggleChecked(adapterPosition)
            return super.onLongClick(v)
        }
    }

    companion object {
        val TAG: String = AlbumAdapter::class.java.simpleName
    }
}
