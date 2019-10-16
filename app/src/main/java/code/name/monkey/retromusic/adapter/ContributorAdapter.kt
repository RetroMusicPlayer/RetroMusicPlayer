package code.name.monkey.retromusic.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.model.Contributor
import code.name.monkey.retromusic.util.RetroUtil.openUrl
import code.name.monkey.retromusic.views.CircularImageView
import com.bumptech.glide.Glide

class ContributorAdapter(
        private var contributors: List<Contributor>
) : RecyclerView.Adapter<ContributorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == HEADER) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contributor_header, parent, false))
        } else
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contributor, parent, false))
    }

    companion object {
        const val HEADER: Int = 0
        const val ITEM: Int = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER
        } else {
            ITEM
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contributor = contributors[position]
        holder.bindData(contributor)
        holder.itemView.setOnClickListener {
            openUrl(it?.context as Activity, contributors[position].link)
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
            println(contributor.profileImage)
            Glide.with(image.context)
                    .load(contributor.profileImage)
                    .error(R.drawable.ic_account_white_24dp)
                    .placeholder(R.drawable.ic_account_white_24dp)
                    .dontAnimate()
                    .into(image)
        }
    }
}
