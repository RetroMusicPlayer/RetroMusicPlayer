package code.name.monkey.appthemehelper.util

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import com.google.android.material.tabs.TabLayout


object TabLayoutUtil {

    fun setTabIconColors(tabLayout: TabLayout?, @ColorInt normalColor: Int, @ColorInt selectedColor: Int) {
        if (tabLayout == null)
            return

        val sl = ColorStateList(arrayOf(intArrayOf(-android.R.attr.state_selected), intArrayOf(android.R.attr.state_selected)),
                intArrayOf(normalColor, selectedColor))
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null && tab.icon != null) {
                tab.icon = TintHelper.createTintedDrawable(tab.icon, sl)
            }
        }
    }
}
