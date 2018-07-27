package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.content.Context;
import android.support.v7.preference.ListPreference;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEListPreference extends ListPreference {

    public ATEListPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATEListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEListPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ATEListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom_support);
        if (getSummary() == null || getSummary().toString().trim().isEmpty())
            setSummary("%s");
    }
}
