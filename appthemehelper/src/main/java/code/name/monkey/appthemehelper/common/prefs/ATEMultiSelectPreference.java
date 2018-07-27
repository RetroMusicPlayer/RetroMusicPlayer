package code.name.monkey.appthemehelper.common.prefs;

import android.content.Context;
import android.util.AttributeSet;

import com.afollestad.materialdialogs.prefs.MaterialListPreference;
import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEMultiSelectPreference extends MaterialListPreference {

    public ATEMultiSelectPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATEMultiSelectPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEMultiSelectPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ATEMultiSelectPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom);
    }
}
