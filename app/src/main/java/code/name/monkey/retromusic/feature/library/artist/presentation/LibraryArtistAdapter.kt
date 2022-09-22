package code.name.monkey.retromusic.feature.library.artist.presentation

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.extensions.hide
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.model.Artist
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.PreferenceUtil
import code.name.monkey.retromusic.util.color.MediaNotificationProcessor
import me.zhanghai.android.fastscroll.PopupTextProvider

class LibraryArtistAdapter(
    override val activity: FragmentActivity,
    var dataSet: List<LibraryArtistUi>,
    var itemLayoutRes: Int,
    val libraryArtistClickListener: LibraryArtistClickListener,
) : AbsMultiSelectAdapter<LibraryArtistAdapter.ViewHolder, LibraryArtistUi>(activity, R.menu.menu_media_selection),
    PopupTextProvider {

    var albumArtistsOnly = false

    init {
        this.setHasStableIds(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataSet(dataSet: List<LibraryArtistUi>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
        albumArtistsOnly = PreferenceUtil.albumArtistsOnly
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].id.hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            try {
                LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false)
            } catch (e: Resources.NotFoundException) {
                LayoutInflater.from(activity).inflate(R.layout.item_grid_circle, parent, false)
            }
        return createViewHolder(view)
    }

    private fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artist = dataSet[position]
        val isChecked = isChecked(artist)
        holder.itemView.isActivated = isChecked
        holder.title?.text = artist.title
        holder.text?.hide()
        val transitionName =
            if (albumArtistsOnly) artist.title else artist.id.toString()
        if (holder.imageContainer != null) {
            holder.imageContainer?.transitionName = transitionName
        } else {
            holder.image?.transitionName = transitionName
        }
        loadArtistImage(artist, holder)
    }

    private fun setColors(processor: MediaNotificationProcessor, holder: ViewHolder) {
        holder.mask?.backgroundTintList = ColorStateList.valueOf(processor.primaryTextColor)
        if (holder.paletteColorContainer != null) {
            holder.paletteColorContainer?.setBackgroundColor(processor.backgroundColor)
            holder.title?.setTextColor(processor.primaryTextColor)
        }
        holder.imageContainerCard?.setCardBackgroundColor(processor.backgroundColor)
    }

    private fun loadArtistImage(artist: LibraryArtistUi, holder: ViewHolder) {
        if (holder.image == null) {
            return
        }
        GlideApp
            .with(activity)
            .asBitmapPalette()
            .load(artist.coverArtUrl)
            .transition(RetroGlideExtension.getDefaultTransition())
            .into(object : RetroMusicColoredTarget(holder.image!!) {
                override fun onColorReady(colors: MediaNotificationProcessor) {
                    setColors(colors, holder)
                }
            })
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): LibraryArtistUi {
        return dataSet[position]
    }

    override fun getName(model: LibraryArtistUi): String {
        return model.title
    }

    override fun onMultipleItemAction(
        menuItem: MenuItem,
        selection: List<LibraryArtistUi>
    ) {
// TODO: fix play
//        SongsMenuHelper.handleMenuClick(activity, getSongList(selection), menuItem.itemId)
    }

    private fun getSongList(artists: List<Artist>): List<Song> {
        val songs = ArrayList<Song>()
        for (artist in artists) {
            songs.addAll(artist.songs) // maybe async in future?
        }
        return songs
    }

    override fun getPopupText(position: Int): String {
        return getSectionName(position)
    }

    private fun getSectionName(position: Int): String {
        return MusicUtil.getSectionName(dataSet[position].title)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            menu?.isVisible = false
        }

        override fun onClick(v: View?) {
            super.onClick(v)
            if (isInQuickSelectMode) {
                toggleChecked(layoutPosition)
            } else {
                val artist = dataSet[layoutPosition]
                image?.let {
                    libraryArtistClickListener.onArtist(artist.id, imageContainer ?: it)
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            return toggleChecked(layoutPosition)
        }
    }
}
