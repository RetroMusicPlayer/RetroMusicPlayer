package code.name.monkey.appthemehelper.util;

import android.content.res.ColorStateList;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;


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
