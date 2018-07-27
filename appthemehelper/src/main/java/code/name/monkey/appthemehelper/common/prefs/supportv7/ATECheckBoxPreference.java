package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.preference.CheckBoxPreference;
import android.util.AttributeSet;

import code.name.monkey.appthemehelper.R;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATECheckBoxPreference extends CheckBoxPreference {

    public ATECheckBoxPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATECheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATECheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATECheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom_support);
        setWidgetLayoutResource(R.layout.ate_preference_checkbox);
    }
}