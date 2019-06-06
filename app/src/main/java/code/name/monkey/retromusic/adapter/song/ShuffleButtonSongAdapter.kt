package code.name.monkey.retromusic.adapter.song

import android.content.res.ColorStateList
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.RetroUtil
import com.google.android.material.button.MaterialButton
import java.util.*


class ShuffleButtonSongAdapter(activity: AppCompatActivity,
                               dataSet: ArrayList<Song>,
                               @LayoutRes itemLayoutRes: Int,
                               usePalette: Boolean,
                               cabHolder: CabHolder?) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, usePalette, cabHolder) {

    override fun createViewHolder(view: View): SongAdapter.ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == OFFSET_ITEM) {
            val accentColor = ThemeStore.accentColor(activity.applicationContext)
            val buttonColor = RetroUtil.toolbarColor(activity)
            val textColor = MaterialValueHelper.getPrimaryTextColor(activity, ColorUtil.isColorLight(buttonColor))
            val viewHolder = holder as ViewHolder

            viewHolder.playAction?.let {
                it.backgroundTintList = ColorStateList.valueOf(buttonColor)
                it.setTextColor(textColor)
                it.iconTint = ColorStateList.valueOf(textColor)
                it.setOnClickListener {
                    MusicPlayerRemote.openQueue(dataSet, 0, true)
                }
            }
            viewHolder.shuffleAction?.let {
                it.backgroundTintList = ColorStateList.valueOf(buttonColor)
                it.setTextColor(textColor)
                it.iconTint = ColorStateList.valueOf(textColor)
                it.setOnClickListener {
                    MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                }
            }


            if (holder.title != null) {
                holder.title!!.text = activity.resources.getString(R.string.action_shuffle_all)
                holder.title!!.setTextColor(accentColor)
            }
            if (holder.text != null) {
                holder.text!!.visibility = View.GONE
            }
            if (holder.menu != null) {
                holder.menu!!.visibility = View.GONE
            }
            if (holder.image != null) {
                val padding = activity.resources.getDimensionPixelSize(R.dimen.default_item_margin) / 2
                holder.image!!.setPadding(padding, padding, padding, padding)
                holder.image!!.setColorFilter(accentColor)
                holder.image!!.setImageResource(R.drawable.ic_shuffle_white_24dp)
            }
            if (holder.separator != null) {
                holder.separator!!.visibility = View.GONE
            }
            if (holder.shortSeparator != null) {
                holder.shortSeparator!!.visibility = View.GONE
            }
        } else {
            super.onBindViewHolder(holder, position - 1)
        }
    }

    inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {
        val playAction: MaterialButton? = itemView.findViewById(R.id.playAction)
        val shuffleAction: MaterialButton? = itemView.findViewById(R.id.shuffleAction)

        override fun onClick(v: View?) {
            if (itemViewType == OFFSET_ITEM) {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
                return
            }
            super.onClick(v)
        }
    }
}