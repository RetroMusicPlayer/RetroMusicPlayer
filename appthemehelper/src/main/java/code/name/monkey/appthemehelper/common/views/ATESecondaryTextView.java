package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATESecondaryTextView extends AppCompatTextView {

    public ATESecondaryTextView(Context context) {
        super(context);
        init(context, null);
    }

    public ATESecondaryTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATESecondaryTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setTextColor(ThemeStore.textColorSecondary(context));
    }
}
