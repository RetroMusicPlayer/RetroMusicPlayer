package code.name.monkey.retromusic.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.glide.GlideApp
import code.name.monkey.retromusic.model.Contributor
import code.name.monkey.retromusic.util.RetroUtil.openUrl
import code.name.monkey.retromusic.views.CircularImageView

class ContributorAdapter(private var contributors: List<Contributor>) : RecyclerView.Adapter<ContributorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contributor, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributor = contributors[position]
        holder.bindData(contributor)
        holder.itemView.setOnClickListener {
            openUrl(it!!.context as Activity, contributors[position].link)
        }
    }

    override fun getItemCount(): Int {
        return contributors.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val text: TextView = itemView.findViewById(R.id.text)
        val image: CircularImageView = itemView.findViewById(R.id.image)

        internal fun bindData(contributor: Contributor) {
            title.text = contributor.name
            text.text = contributor.summary
            GlideApp.with(image.context)
                    .load(contributor.profileImage)
                    .error(R.drawable.ic_person_flat)
                    .placeholder(R.drawable.ic_person_flat)
                    .into(image)
        }
    }
}
