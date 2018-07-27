package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.content.Context;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.View;

import code.name.monkey.appthemehelper.R;
import code.name.monkey.appthemehelper.common.prefs.BorderCircleView;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATEColorPreference extends Preference {

    private View mView;
    private int color;
    private int border;

    public ATEColorPreference(Context context) {
        this(context, null, 0);
        init(context, null);
    }

    public ATEColorPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init(context, attrs);
    }

    public ATEColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);

    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom_support);
        setWidgetLayoutResource(R.layout.ate_preference_color);
        setPersistent(false);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        mView = holder.itemView;
        invalidateColor();
    }

    public void setColor(int color, int border) {
        this.color = color;
        this.border = border;
        invalidateColor();
    }

    private void invalidateColor() {
        if (mView != null) {
            BorderCircleView circle = (BorderCircleView) mView.findViewById(R.id.circle);
            if (this.color != 0) {
                circle.setVisibility(View.VISIBLE);
                circle.setBackgroundColor(color);
                circle.setBorderColor(border);
            } else {
                circle.setVisibility(View.GONE);
            }
        }
    }
}