package code.name.monkey.retromusic.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.MaterialValueHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.glide.RetroGlideExtension
import code.name.monkey.retromusic.glide.RetroMusicColoredTarget
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.ui.adapter.CollageSongAdapter.CollageSongViewHolder
import code.name.monkey.retromusic.ui.adapter.base.MediaEntryViewHolder
import java.util.*

/**
 * @author Hemanth S (h4h13).
 */
class CollageSongAdapter(private val activity: Activity, private val dataSet: ArrayList<Song>) : RecyclerView.Adapter<CollageSongViewHolder>() {

    override fun onBindViewHolder(holder: CollageSongViewHolder, position: Int) {
        holder.bindSongs()
        if (dataSet.size > 8) {
            for (i in 0 until dataSet.subList(0, 8).size) {
                GlideApp.with(activity)
                        .asBitmapPalette()
                        .load(RetroGlideExtension.getSongModel(dataSet[i]))
                        .transition(RetroGlideExtension.getDefaultTransition())
                        .songOptions(dataSet[i])
                        .into(object : RetroMusicColoredTarget(holder.itemView.findViewById(holder.ids[i]) as ImageView) {
                            override fun onColorReady(color: Int) {

                            }
                        })
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollageSongViewHolder {
        return CollageSongViewHolder(LayoutInflater.from(activity).inflate(R.layout.item_collage, parent, false))
    }

    inner class CollageSongViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        val ids = arrayListOf(R.id.image_2, R.id.image_3, R.id.image_4, R.id.image_5, R.id.image_6, R.id.image_7, R.id.image_8, R.id.image_9)
        private var textView: TextView = itemView.findViewById(R.id.image_1)

        fun bindSongs() {
            for (i in ids) {
                val imageView = itemView.findViewById<ImageView>(i)
                imageView.setOnClickListener {
                    textView.setOnClickListener { MusicPlayerRemote.openQueue(dataSet, 0, true) }
                }
            }

            val context = itemView.context
            val color = ThemeStore.accentColor(context);

            textView.setOnClickListener { MusicPlayerRemote.openQueue(dataSet, 0, true) }
            textView.setBackgroundColor(color);
            textView.setTextColor(MaterialValueHelper.getPrimaryTextColor(context, ColorUtil.isColorLight(color)))
        }
    }
}
