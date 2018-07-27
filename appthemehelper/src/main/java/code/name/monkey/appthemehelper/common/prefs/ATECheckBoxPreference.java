package code.name.monkey.appthemehelper.common.prefs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.lang.reflect.Field;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.R;
import code.name.monkey.appthemehelper.ThemeStore;

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
        setLayoutResource(R.layout.ate_preference_custom);

        try {
            Field canRecycleLayoutField = Preference.class.getDeclaredField("mCanRecycleLayout");
            canRecycleLayoutField.setAccessible(true);
            canRecycleLayoutField.setBoolean(this, true);
        } catch (Exception ignored) {
        }

        try {
            Field hasSpecifiedLayout = Preference.class.getDeclaredField("mHasSpecifiedLayout");
            hasSpecifiedLayout.setAccessible(true);
            hasSpecifiedLayout.setBoolean(this, true);
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);

        View parentCheckbox = findCheckboxView(view);
        if (parentCheckbox != null) {
            ATH.setTint(parentCheckbox, ThemeStore.accentColor(view.getContext()));
        }
    }

    @Nullable
    private View findCheckboxView(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                if (child instanceof CheckBox) {
                    return child;
                } else if (child instanceof ViewGroup) {
                    View potentialCheckbox = findCheckboxView(child);
                    if (potentialCheckbox != null) return potentialCheckbox;
                }
            }
        } else if (view instanceof CheckBox) {
            return view;
        }
        return null;
    }
}