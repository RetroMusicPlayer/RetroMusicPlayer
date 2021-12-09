package code.name.monkey.retromusic.adapter.backup

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import java.io.File


class BackupAdapter(
    val activity: FragmentActivity,
    var dataSet: MutableList<File>,
    val backupClickedListener: BackupClickedListener
) : RecyclerView.Adapter<BackupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(activity).inflate(R.layout.item_list_card, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = dataSet[position].nameWithoutExtension
    }

    override fun getItemCount(): Int = dataSet.size

    fun swapDataset(dataSet: List<File>) {
        this.dataSet = ArrayList(dataSet)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val menu: AppCompatImageView = itemView.findViewById(R.id.menu)

        init {
            menu.setOnClickListener { view ->
                val popupMenu = PopupMenu(activity, view)
                popupMenu.inflate(R.menu.menu_backup)
                popupMenu.setOnMenuItemClickListener { menuItem ->
                    return@setOnMenuItemClickListener backupClickedListener.onBackupMenuClicked(
                        dataSet[bindingAdapterPosition],
                        menuItem
                    )
                }
                popupMenu.show()
            }
            itemView.setOnClickListener {
                backupClickedListener.onBackupClicked(dataSet[bindingAdapterPosition])
            }
        }
    }

    interface BackupClickedListener {
        fun onBackupClicked(file: File)

        fun onBackupMenuClicked(file: File, menuItem: MenuItem): Boolean
    }
}