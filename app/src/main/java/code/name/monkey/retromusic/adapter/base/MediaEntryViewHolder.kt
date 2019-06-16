package code.name.monkey.retromusic.adapter.base

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

open class MediaEntryViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {
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


        title = view.findViewById(R.id.title)
        text = view.findViewById(R.id.text)

        image = view.findViewById(R.id.image)
        imageContainer = view.findViewById(R.id.image_container)
        imageTextContainer = view.findViewById(R.id.image_text_container)
        imageContainerCard = view.findViewById(R.id.image_container_card)

        imageText = view.findViewById(R.id.image_text)

        menu = view.findViewById(R.id.menu)
        dragView = view.findViewById(R.id.drag_view)

        separator = view.findViewById(R.id.separator)
        shortSeparator = view.findViewById(R.id.short_separator)
        paletteColorContainer = view.findViewById(R.id.palette_color_container)

        time = view.findViewById(R.id.time);
        recyclerView = view.findViewById(R.id.recycler_view)

        mask = view.findViewById(R.id.mask)
        playSongs = view.findViewById(R.id.playSongs)

        view.setOnClickListener(this@MediaEntryViewHolder)
        view.setOnLongClickListener(this@MediaEntryViewHolder)

        imageContainerCard?.setCardBackgroundColor(ThemeStore.primaryColor(itemView.context))

    }

    fun setImageTransitionName(transitionName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && image != null) {
            image!!.transitionName = transitionName
        }
    }
}