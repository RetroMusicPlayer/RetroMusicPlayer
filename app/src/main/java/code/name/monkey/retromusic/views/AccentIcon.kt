package code.name.monkey.retromusic.views

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import code.name.monkey.appthemehelper.ThemeStore

class AccentIcon @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1
) : AppCompatImageView(context, attrs, defStyleAttr) {
    init {
        imageTintList = ColorStateList.valueOf(ThemeStore.accentColor(context))
    }
}