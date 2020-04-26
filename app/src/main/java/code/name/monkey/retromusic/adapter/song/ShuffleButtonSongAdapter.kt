package code.name.monkey.retromusic.adapter.song

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.helper.MusicPlayerRemote
import code.name.monkey.retromusic.interfaces.CabHolder
import code.name.monkey.retromusic.model.Song
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.textview.MaterialTextView

class ShuffleButtonSongAdapter(
    activity: AppCompatActivity,
    dataSet: MutableList<Song>,
    itemLayoutRes: Int,
    cabHolder: CabHolder?
) : AbsOffsetSongAdapter(activity, dataSet, itemLayoutRes, cabHolder) {

    override fun createViewHolder(view: View): ViewHolder {
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongAdapter.ViewHolder, position: Int) {
        if (holder.itemViewType == OFFSET_ITEM) {
            val viewHolder = holder as ViewHolder
            val info =
                activity.resources.getQuantityString(
                    R.plurals.numSongs,
                    dataSet.size,
                    dataSet.size
                )
            viewHolder.info?.text = info
            viewHolder.shuffleAction?.setOnClickListener {
                MusicPlayerRemote.openAndShuffleQueue(dataSet, true)
            }
            showChangeLayout(viewHolder)
            showSortMenu(viewHolder)

        } else {
            super.onBindViewHolder(holder, position - 1)
        }
    }

    private fun showChangeLayout(viewHolder: ViewHolder) {
        viewHolder.changeLayoutType?.setOnClickListener {
            val popupMenu = PopupMenu(activity, viewHolder.changeLayoutType)
            popupMenu.inflate(R.menu.menu_layout_types)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.layout.item_card ->
                        popupMenu.menu.findItem(R.id.action_layout_card).isChecked = true
                    R.layout.item_grid ->
                        popupMenu.menu.findItem(R.id.action_layout_normal).isChecked = true

                    R.layout.item_card_color ->
                        popupMenu.menu.findItem(R.id.action_layout_colored_card).isChecked = true

                    R.layout.item_grid_circle ->
                        popupMenu.menu.findItem(R.id.action_layout_circular).isChecked = true

                    R.layout.image ->
                        popupMenu.menu.findItem(R.id.action_layout_image).isChecked = true

                    R.layout.item_image_gradient ->
                        popupMenu.menu.findItem(R.id.action_layout_gradient_image).isChecked = true
                }
                PreferenceUtil.getInstance(activity).songGridStyle = it.itemId
                true
            }
            popupMenu.show()
            popupMenu.menu
                .findItem(PreferenceUtil.getInstance(activity).songGridStyle).isChecked = true
        }
    }

    private fun showSortMenu(viewHolder: ViewHolder) {
        viewHolder.sortOrder?.setOnClickListener {
            val popupMenu = PopupMenu(activity, viewHolder.sortOrder)
            popupMenu.inflate(R.menu.menu_song_sort_order)
            popupMenu.show()
        }
    }

    inner class ViewHolder(itemView: View) : AbsOffsetSongAdapter.ViewHolder(itemView) {
        val sortOrder: View? = itemView.findViewById(R.id.sortOrder)
        val changeLayoutType: View? = itemView.findViewById(R.id.changeLayoutType)
        val shuffleAction: View? = itemView.findViewById(R.id.shuffleAction)
        val info: MaterialTextView? = itemView.findViewById(R.id.info)
    }
}