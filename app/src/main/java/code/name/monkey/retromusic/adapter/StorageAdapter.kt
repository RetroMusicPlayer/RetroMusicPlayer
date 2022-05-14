package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import java.io.File

class StorageAdapter(
    val storageList: List<Storage>,
    val storageClickListener: StorageClickListener
) :
    RecyclerView.Adapter<StorageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_storage,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(storageList[position])
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)

        fun bindData(storage: Storage) {
            title.text = storage.title
        }

        init {
            itemView.setOnClickListener { storageClickListener.onStorageClicked(storageList[bindingAdapterPosition]) }
        }
    }
}

interface StorageClickListener {
    fun onStorageClicked(storage: Storage)
}

class Storage {
    lateinit var title: String
    lateinit var file: File
}