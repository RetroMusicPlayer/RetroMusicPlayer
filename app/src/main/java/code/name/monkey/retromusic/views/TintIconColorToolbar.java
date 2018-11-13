package code.name.monkey.retromusic.views;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import code.name.monkey.appthemehelper.ThemeStore;

public class TintIconColorToolbar extends Toolbar {
    public TintIconColorToolbar(Context context) {
        super(context);
    }

    public TintIconColorToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TintIconColorToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void setNavigationIcon(@Nullable Drawable icon) {
        super.setNavigationIcon(icon);
        if (icon != null) {
            icon.setColorFilter(ThemeStore.accentColor(getContext()), PorterDuff.Mode.SRC_IN);
        }
    }
}
