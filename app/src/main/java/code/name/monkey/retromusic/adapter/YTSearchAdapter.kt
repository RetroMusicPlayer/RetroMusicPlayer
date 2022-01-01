package code.name.monkey.retromusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import com.google.android.material.button.MaterialButton

data class YTSearchResult(
    val title: String,
    val author: String,
    val thumbnail: String,
    val link: String
    )

class YTSearchAdapter(
    private val data: Array<YTSearchResult>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<YTSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YTSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_yt_search, parent, false)
        return ViewHolder(view=view)
    }

    override fun onBindViewHolder(holder: YTSearchAdapter.ViewHolder, position: Int) {
        holder.title.text = data[position].title
        holder.author.text = data[position].author
        holder.button.setOnClickListener {
            onClick(data[position].link)
        }
    }

    override fun getItemCount() = data.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val author: TextView = view.findViewById(R.id.author)
        val button: View = view.findViewById(R.id.downloadButton)
    }
}