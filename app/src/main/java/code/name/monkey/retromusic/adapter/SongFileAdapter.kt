/*
 * Copyright 2019 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package code.name.monkey.retromusic.adapter

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.base.AbsMultiSelectAdapter
import code.name.monkey.retromusic.adapter.base.MediaEntryViewHolder
import code.name.monkey.retromusic.glide.audiocover.AudioFileCover
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.util.MusicUtil
import code.name.monkey.retromusic.util.RetroUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.signature.MediaStoreSignature
import me.zhanghai.android.fastscroll.PopupTextProvider
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

class SongFileAdapter(
    private val activity: AppCompatActivity,
    private var dataSet: List<File>,
    private val itemLayoutRes: Int,
    private val callbacks: Callbacks?,
    cabHolder: CabHolder?
) : AbsMultiSelectAdapter<SongFileAdapter.ViewHolder, File>(
    activity, cabHolder, R.menu.menu_media_selection
), PopupTextProvider {

    init {
        this.setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position].isDirectory) FOLDER else FILE
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].hashCode().toLong()
    }

    fun swapDataSet(songFiles: List<File>) {
        this.dataSet = songFiles
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, index: Int) {
        val file = dataSet[index]
        holder.itemView.isActivated = isChecked(file)
        holder.title?.text = getFileTitle(file)
        if (holder.text != null) {
            if (holder.itemViewType == FILE) {
                holder.text?.text = getFileText(file)
            } else {
                holder.text?.visibility = View.GONE
            }
        }

        if (holder.image != null) {
            loadFileImage(file, holder)
        }
    }

    private fun getFileTitle(file: File): String {
        return file.name
    }

    private fun getFileText(file: File): String? {
        return if (file.isDirectory) null else readableFileSize(file.length())
    }

    private fun loadFileImage(file: File, holder: ViewHolder) {
        val iconColor = ATHUtil.resolveColor(activity, R.attr.colorControlNormal)
        if (file.isDirectory) {
            holder.image?.let {
                it.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN)
                it.setImageResource(R.drawable.ic_folder_white_24dp)
            }
            holder.imageTextContainer?.setCardBackgroundColor(
                ATHUtil.resolveColor(
                    activity,
                    R.attr.colorSurface
                )
            )
        } else {
            val error = RetroUtil.getTintedVectorDrawable(
                activity, R.drawable.ic_file_music_white_24dp, iconColor
            )
            Glide.with(activity)
                .load(AudioFileCover(file.path))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(error)
                .placeholder(error)
                .animate(android.R.anim.fade_in)
                .signature(MediaStoreSignature("", file.lastModified(), 0))
                .into(holder.image)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getIdentifier(position: Int): File? {
        return dataSet[position]
    }

    override fun getName(`object`: File): String {
        return getFileTitle(`object`)
    }

    override fun onMultipleItemAction(menuItem: MenuItem, selection: ArrayList<File>) {
        if (callbacks == null) return
        callbacks.onMultipleItemAction(menuItem, selection)
    }

    override fun getPopupText(position: Int): String {
        return getSectionName(position)
    }

    private fun getSectionName(position: Int): String {
        return MusicUtil.getSectionName(dataSet[position].name)
    }

    interface Callbacks {
        fun onFileSelected(file: File)

        fun onFileMenuClicked(file: File, view: View)

        fun onMultipleItemAction(item: MenuItem, files: ArrayList<File>)
    }

    inner class ViewHolder(itemView: View) : MediaEntryViewHolder(itemView) {

        init {
            if (menu != null && callbacks != null) {
                menu?.setOnClickListener { v ->
                    val position = layoutPosition
                    if (isPositionInRange(position)) {
                        callbacks.onFileMenuClicked(dataSet[position], v)
                    }
                }
            }
            if (imageTextContainer != null) {
                imageTextContainer?.cardElevation = 0f
            }
        }

        override fun onClick(v: View?) {
            val position = layoutPosition
            if (isPositionInRange(position)) {
                if (isInQuickSelectMode) {
                    toggleChecked(position)
                } else {
                    callbacks?.onFileSelected(dataSet[position])
                }
            }
        }

        override fun onLongClick(v: View?): Boolean {
            val position = layoutPosition
            return isPositionInRange(position) && toggleChecked(position)
        }

        private fun isPositionInRange(position: Int): Boolean {
            return position >= 0 && position < dataSet.size
        }
    }

    companion object {

        private const val FILE = 0
        private const val FOLDER = 1

        fun readableFileSize(size: Long): String {
            if (size <= 0) return "$size B"
            val units = arrayOf("B", "KB", "MB", "GB", "TB")
            val digitGroups = (log10(size.toDouble()) / log10(1024.0)).toInt()
            return DecimalFormat("#,##0.##").format(size / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
        }
    }
}