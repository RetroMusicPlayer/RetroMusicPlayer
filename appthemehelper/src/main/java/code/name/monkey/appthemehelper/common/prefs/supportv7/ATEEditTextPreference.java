package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.content.Context;
import android.support.v7.preference.EditTextPreference;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEEditTextPreference extends EditTextPreference {

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
        setLayoutResource(R.layout.ate_preference_custom_support);
    }
}
