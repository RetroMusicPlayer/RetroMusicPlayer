package code.name.monkey.appthemehelper.common.prefs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import code.name.monkey.appthemehelper.ThemeStore;

public class ATEPreferenceCategory extends PreferenceCategory {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public ATEPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ATEPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ATEPreferenceCategory(Context context) {
        super(context);
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        TextView mTitle = (TextView) view.findViewById(android.R.id.title);
        mTitle.setTextColor(ThemeStore.accentColor(view.getContext()));
    }
}
