package code.name.monkey.retromusic.views

import android.content.Context
import android.util.AttributeSet
import code.name.monkey.appthemehelper.ThemeStore
import code.name.monkey.appthemehelper.util.ATHUtil
import code.name.monkey.appthemehelper.util.ColorUtil
import code.name.monkey.appthemehelper.util.NavigationViewUtil
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.util.PreferenceUtil
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationBarTinted @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        BottomNavigationView(context, attrs, defStyleAttr) {

    init {
        labelVisibilityMode = PreferenceUtil.getInstance().tabTitleMode
        setBackgroundColor(ThemeStore.primaryColor(context))
        selectedItemId = PreferenceUtil.getInstance().lastPage

        val iconColor = ATHUtil.resolveColor(context, R.attr.iconColor)
        val accentColor = ThemeStore.accentColor(context)
        NavigationViewUtil.setItemIconColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor)
        NavigationViewUtil.setItemTextColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor)
    }
}