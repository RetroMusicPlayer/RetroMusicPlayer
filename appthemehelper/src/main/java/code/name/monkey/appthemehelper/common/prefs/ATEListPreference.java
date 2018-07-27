package code.name.monkey.appthemehelper.common.prefs;

import android.content.Context;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.prefs.MaterialListPreference;
import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEListPreference extends MaterialListPreference {

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
        setLayoutResource(R.layout.ate_preference_custom);
        if (getSummary() == null || getSummary().toString().trim().isEmpty())
            setSummary("%s");
    }
}
