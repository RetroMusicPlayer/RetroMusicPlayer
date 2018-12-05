package code.name.monkey.appthemehelper.util;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public final class NavigationViewUtil {

    private NavigationViewUtil() {
    }

    public static void setItemIconColors(@NonNull NavigationView navigationView, @ColorInt int normalColor, @ColorInt int selectedColor) {
        final ColorStateList iconSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        normalColor,
                        selectedColor
                });
        navigationView.setItemIconTintList(iconSl);
        Drawable drawable = navigationView.getItemBackground();
        navigationView.setItemBackground(TintHelper.createTintedDrawable(drawable, ColorUtil.withAlpha(ThemeStore.accentColor(navigationView.getContext()), 0.2f)));
    }

    public static void setItemTextColors(@NonNull NavigationView navigationView, @ColorInt int normalColor, @ColorInt int selectedColor) {
        final ColorStateList textSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        normalColor,
                        selectedColor
                });
        navigationView.setItemTextColor(textSl);
    }

    public static void setItemIconColors(@NonNull BottomNavigationView bottomNavigationView, @ColorInt int normalColor, @ColorInt int selectedColor) {
        final ColorStateList iconSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        normalColor,
                        selectedColor
                });
        bottomNavigationView.setItemIconTintList(iconSl);
    }

    public static void setItemTextColors(@NonNull BottomNavigationView bottomNavigationView, @ColorInt int normalColor, @ColorInt int selectedColor) {
        final ColorStateList textSl = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        normalColor,
                        selectedColor
                });
        bottomNavigationView.setItemTextColor(textSl);
    }
}