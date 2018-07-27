package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.TextView;

import code.name.monkey.appthemehelper.R;
import code.name.monkey.appthemehelper.ThemeStore;

public class ATEPreferenceCategory extends PreferenceCategory {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATEPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public ATEPreferenceCategory(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ATEPreferenceCategory(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATEPreferenceCategory(Context context) {
        super(context);
        init(context, null);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        TextView mTitle = (TextView) holder.itemView;
        mTitle.setTextColor(ThemeStore.accentColor(getContext()));
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_category);
    }
}
