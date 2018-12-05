package code.name.monkey.retromusic.views;

import android.content.Context;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.util.ColorUtil;
import code.name.monkey.appthemehelper.util.MaterialValueHelper;

public class MaterialButtonTextColor extends MaterialButton {
    public MaterialButtonTextColor(Context context) {
        this(context, null);
    }

    public MaterialButtonTextColor(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MaterialButtonTextColor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(MaterialValueHelper.getPrimaryTextColor(getContext(), ColorUtil.isColorLight(ThemeStore.primaryColor(getContext()))));
    }
}
