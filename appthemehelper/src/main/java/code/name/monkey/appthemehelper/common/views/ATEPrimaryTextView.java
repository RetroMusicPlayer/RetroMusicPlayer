package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEPrimaryTextView extends android.support.v7.widget.AppCompatTextView {

    public ATEPrimaryTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ATEPrimaryTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEPrimaryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setTextColor(ThemeStore.textColorPrimary(context));
    }
}
