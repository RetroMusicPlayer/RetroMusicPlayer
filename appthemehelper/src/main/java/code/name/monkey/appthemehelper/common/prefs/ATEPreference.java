package code.name.monkey.appthemehelper.common.prefs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEPreference extends Preference {

    public ATEPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATEPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom);
    }
}