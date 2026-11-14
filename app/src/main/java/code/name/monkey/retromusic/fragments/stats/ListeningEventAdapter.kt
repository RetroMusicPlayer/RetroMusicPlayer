package code.name.monkey.retromusic.fragments.stats

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.databinding.ItemListeningEventBinding
import code.name.monkey.retromusic.db.ListeningEventEntity
import code.name.monkey.retromusic.util.MusicUtil

class ListeningEventAdapter :
    ListAdapter<ListeningEventEntity, ListeningEventAdapter.ViewHolder>(DIFF) {

    class ViewHolder(val binding: ItemListeningEventBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListeningEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        val context = holder.itemView.context
        val date = DateUtils.formatDateTime(
            context,
            event.playedAt,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_ALL
        )
        holder.binding.title.text = event.title
        holder.binding.subtitle.text = context.getString(
            R.string.listening_event_subtitle,
            event.artistName,
            MusicUtil.getReadableDurationString(event.listenedMs),
            date
        )
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<ListeningEventEntity>() {
            override fun areItemsTheSame(
                oldItem: ListeningEventEntity,
                newItem: ListeningEventEntity
            ) = oldItem.id == newItem.id

            // Events are immutable once written.
            override fun areContentsTheSame(
                oldItem: ListeningEventEntity,
                newItem: ListeningEventEntity
            ) = oldItem.id == newItem.id
        }
    }
}
