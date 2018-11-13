package code.name.monkey.retromusic.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ATHUtil;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.NavigationViewUtil;
import code.name.monkey.retromusic.R;
import code.name.monkey.retromusic.util.PreferenceUtil;

public class BottomNavigationBarTinted extends BottomNavigationView {
    public BottomNavigationBarTinted(Context context) {
        this(context, null);
    }

    public BottomNavigationBarTinted(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationBarTinted(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLabelVisibilityMode(PreferenceUtil.getInstance().getTabTitleMode());
        setBackgroundColor(ThemeStore.primaryColor(context));
        setSelectedItemId(PreferenceUtil.getInstance().getLastPage());

        int iconColor = ATHUtil.resolveColor(context, R.attr.iconColor);
        int accentColor = ThemeStore.accentColor(context);
        NavigationViewUtil.setItemIconColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor);
        NavigationViewUtil.setItemTextColors(this, ColorUtil.withAlpha(iconColor, 0.5f), accentColor);

    }
}
