package code.name.monkey.retromusic.adapter.backup

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ItemListBackupBinding
import code.name.monkey.retromusic.helper.BackupEntry


class BackupAdapter(
    val activity: FragmentActivity,
    var dataSet: MutableList<BackupEntry>,
    val backupClickedListener: BackupClickedListener
) : RecyclerView.Adapter<BackupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListBackupBinding.inflate(LayoutInflater.from(activity), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = dataSet[position].name
    }

    override fun getItemCount(): Int = dataSet.size

    @SuppressLint("NotifyDataSetChanged")
    fun swapDataset(dataSet: List<BackupEntry>) {
        this.dataSet = ArrayList(dataSet)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemListBackupBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.menu.setOnClickListener { view ->
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
        fun onBackupClicked(entry: BackupEntry)

        fun onBackupMenuClicked(entry: BackupEntry, menuItem: MenuItem): Boolean
    }
}