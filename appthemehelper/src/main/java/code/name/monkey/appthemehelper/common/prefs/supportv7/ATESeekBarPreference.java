package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.SeekBarPreference;

public class ATESeekBarPreference extends SeekBarPreference {
    public ATESeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ATESeekBarPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ATESeekBarPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ATESeekBarPreference(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {

    }
}
