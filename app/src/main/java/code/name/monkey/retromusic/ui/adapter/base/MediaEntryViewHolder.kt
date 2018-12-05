package code.name.monkey.retromusic.ui.adapter.base

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.retromusic.R

open class MediaEntryViewHolder(w: View) : RecyclerView.ViewHolder(w), View.OnClickListener, View.OnLongClickListener {
    override fun onLongClick(v: View?): Boolean {
        return false
    }

    override fun onClick(v: View?) {

    }

    var image: ImageView? = null
    var imageText: TextView? = null
    var title: TextView? = null
    var text: TextView? = null
    var imageContainer: ViewGroup? = null
    var imageContainerCard: CardView? = null
    var menu: View? = null
    var separator: View? = null
    var shortSeparator: View? = null
    var dragView: View? = null
    var paletteColorContainer: View? = null
    var time: TextView? = null
    var recyclerView: RecyclerView? = null
    var playSongs: ImageButton? = null
    var mask: View? = null
    var imageTextContainer: CardView? = null

    init {
        title = w.findViewById(R.id.title)
        text = w.findViewById(R.id.text)

        image = w.findViewById(R.id.image)
        imageContainer = w.findViewById(R.id.image_container)
        imageTextContainer = w.findViewById(R.id.image_text_container)
        imageContainerCard = w.findViewById(R.id.image_container_card)

        imageText = w.findViewById(R.id.image_text)

        menu = w.findViewById(R.id.menu)
        dragView = w.findViewById(R.id.drag_view)

        separator = w.findViewById(R.id.separator)
        shortSeparator = w.findViewById(R.id.short_separator)
        paletteColorContainer = w.findViewById(R.id.palette_color_container)

        time = w.findViewById(R.id.time);
        recyclerView = w.findViewById(R.id.recycler_view)

        mask = w.findViewById(R.id.mask)
        playSongs = w.findViewById(R.id.play_songs)

        w.setOnClickListener(this)
        w.setOnLongClickListener(this)

        if (imageTextContainer != null) {
            imageTextContainer!!.setCardBackgroundColor(ThemeStore.primaryColor(itemView.context))
        }
        if (imageContainerCard != null) {
            imageContainerCard!!.setCardBackgroundColor(ThemeStore.primaryColor(itemView.context))
        }
    }

    fun setImageTransitionName(transitionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image!!.transitionName = transitionName
        }
    }
}