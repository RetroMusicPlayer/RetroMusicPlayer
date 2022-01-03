package code.name.monkey.retromusic.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import com.google.android.material.button.MaterialButton
import com.google.api.services.youtube.model.SearchResult
import com.google.api.services.youtube.model.Video
import com.squareup.picasso.Picasso

data class YTSearchResult(
    val title: String,
    val author: String,
    val thumbnail: String,
    val link: String
    )

class YTSearchAdapter(
    private var data: List<SearchResult>,
    private val onClick: (String) -> Unit
): RecyclerView.Adapter<YTSearchAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YTSearchAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_yt_search, parent, false)
        return ViewHolder(view=view)
    }

    override fun onBindViewHolder(holder: YTSearchAdapter.ViewHolder, position: Int) {
        holder.title.text = data[position].snippet.title
        holder.author.text = data[position].snippet.channelTitle
        Picasso.get().load(data[position].snippet.thumbnails.default.url).centerCrop().fit().into(
            holder.image
        )
        holder.button.setOnClickListener {
            onClick(data[position].id.videoId)
        }
    }

    override fun getItemCount() = data.size

    fun update(newData: List<SearchResult>) {
        data = newData
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.title)
        val author: TextView = view.findViewById(R.id.author)
        val button: View = view.findViewById(R.id.downloadButton)
    }
}