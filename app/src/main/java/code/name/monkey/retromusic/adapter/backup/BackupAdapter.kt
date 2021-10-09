package code.name.monkey.retromusic.adapter.backup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import java.io.File

class BackupAdapter(
    val context: Context,
    var dataSet: MutableList<File>,
) : RecyclerView.Adapter<BackupAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_card, parent, false)
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

        init {
        }
    }
}