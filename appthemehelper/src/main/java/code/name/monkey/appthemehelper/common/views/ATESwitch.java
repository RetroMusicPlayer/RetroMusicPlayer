package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATESwitch extends SwitchCompat {

    public ATESwitch(Context context) {
        super(context);
        init(context, null);
    }

    public ATESwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATESwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ATH.setTint(this, ThemeStore.accentColor(context));
    }

    @Override
    public boolean isShown() {
        return getParent() != null && getVisibility() == View.VISIBLE;
    }
}
