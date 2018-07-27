package code.name.monkey.appthemehelper.common.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.ThemeStore;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEEditText extends EditText {

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        ATH.setTint(this, ThemeStore.accentColor(context));
        setTextColor(ThemeStore.textColorPrimary(context));
    }
}
