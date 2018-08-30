package code.name.monkey.appthemehelper.common.views;

import android.content.Context;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.ThemeStore;


/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEEditText extends AppCompatEditText {

    public ATEEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ATEEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ATH.setTint(this, ThemeStore.accentColor(context));
        setTextColor(ThemeStore.textColorPrimary(context));
    }
}
