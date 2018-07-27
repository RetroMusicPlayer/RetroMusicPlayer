package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATESeekBar extends android.support.v7.widget.AppCompatSeekBar {

    public ATESeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public ATESeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATESeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ATH.setTint(this, ThemeStore.accentColor(context));
    }
}
