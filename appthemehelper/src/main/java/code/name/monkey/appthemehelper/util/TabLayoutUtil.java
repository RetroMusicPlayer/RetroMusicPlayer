package code.name.monkey.appthemehelper.util;

import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;


public final class TabLayoutUtil {

    public static void setTabIconColors(@Nullable TabLayout tabLayout, @ColorInt int normalColor, @ColorInt int selectedColor) {
        if (tabLayout == null)
            return;

        final ColorStateList sl = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_selected},
                new int[]{android.R.attr.state_selected}
        },
                new int[]{
                        normalColor,
                        selectedColor
                });
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            final TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getIcon() != null) {
                tab.setIcon(TintHelper.createTintedDrawable(tab.getIcon(), sl));
            }
        }
    }

    private TabLayoutUtil() {
    }
}
