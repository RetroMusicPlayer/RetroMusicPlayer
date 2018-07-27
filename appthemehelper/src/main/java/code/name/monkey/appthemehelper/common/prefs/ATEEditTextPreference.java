package code.name.monkey.appthemehelper.common.prefs;

import android.content.Context;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.prefs.MaterialEditTextPreference;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEEditTextPreference extends MaterialEditTextPreference {

    public ATEEditTextPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATEEditTextPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ATEEditTextPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(code.name.monkey.appthemehelper.R.layout.ate_preference_custom);
    }
}
