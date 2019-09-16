package code.name.monkey.retromusic.adapter.album

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.glide.SongGlideRequest
import code.name.monkey.retromusic.helper.HorizontalAdapterHelper
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Album
import code.name.monkey.retromusic.util.MusicUtil
import com.bumptech.glide.Glide
import java.util.*


class HorizontalAlbumAdapter(
        activity: AppCompatActivity,
        dataSet: ArrayList<Album>,
        usePalette: Boolean,
        cabHolder: CabHolder?
) : AlbumAdapter(
        activity,
        dataSet,
        HorizontalAdapterHelper.LAYOUT_RES,
        usePalette,
        cabHolder
) {

    override fun createViewHolder(view: View, viewType: Int): ViewHolder {
        val params = view.layoutParams as ViewGroup.MarginLayoutParams
        HorizontalAdapterHelper.applyMarginToLayoutParams(activity, params, viewType)
        return ViewHolder(view)
    }

    override fun setColors(color: Int, holder: ViewHolder) {
        holder.title?.setTextColor(MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(color)))
        holder.text?.setTextColor(MaterialValueHelper.getSecondaryTextColor(activity, ColorUtil.isColorLight(color)))
    }

    override fun loadAlbumCover(album: Album, holder: ViewHolder) {
        if (holder.image == null) return

        SongGlideRequest.Builder.from(Glide.with(activity), album.safeGetFirstSong())
                .checkIgnoreMediaStore(activity)
                .generatePalette(activity).build()
                .into(object : RetroMusicColoredTarget(holder.image!!) {
                    override fun onLoadCleared(placeholder: Drawable?) {
                        super.onLoadCleared(placeholder)
                        setColors(albumArtistFooterColor, holder)
                    }

                    override fun onColorReady(color: Int) {
                        if (usePalette)
                            setColors(color, holder)
                        else
                            setColors(albumArtistFooterColor, holder)
                    }
                })
    }

    override fun getAlbumText(album: Album): String? {
        return MusicUtil.getYearString(album.year)
    }

    override fun getItemViewType(position: Int): Int {
        return HorizontalAdapterHelper.getItemViewtype(position, itemCount)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    companion object {
        val TAG: String = AlbumAdapter::class.java.simpleName
    }
}
