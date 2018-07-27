package code.name.monkey.appthemehelper.common.prefs;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import code.name.monkey.appthemehelper.ATH;
import code.name.monkey.appthemehelper.R;
import code.name.monkey.appthemehelper.ThemeStore;
import code.name.monkey.appthemehelper.common.views.ATESwitch;

import java.lang.reflect.Field;

/**
 * @author Aidan Follestad (afollestad)
 */
public class ATESwitchPreference extends SwitchPreference {

    static final boolean COMPAT_MODE = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

    private ATESwitch mSwitch;

    public ATESwitchPreference(Context context) {
        super(context);
        init(context, null);
    }

    public ATESwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ATESwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ATESwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setLayoutResource(R.layout.ate_preference_custom);
        if (COMPAT_MODE) {
            setWidgetLayoutResource(R.layout.ate_preference_switch);
        } else {
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
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        if (COMPAT_MODE) {
            mSwitch = (ATESwitch) view.findViewById(R.id.switchWidget);
            mSwitch.setChecked(isChecked());
        } else {
            View parentSwitch = findSwitchView(view);
            if (parentSwitch != null) {
                ATH.setTint(parentSwitch, ThemeStore.accentColor(view.getContext()));
            }
        }
    }

    @Nullable
    private View findSwitchView(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View child = ((ViewGroup) view).getChildAt(i);
                if (child instanceof Switch || child instanceof SwitchCompat) {
                    return child;
                } else if (child instanceof ViewGroup) {
                    View potentialSwitch = findSwitchView(child);
                    if (potentialSwitch != null) return potentialSwitch;
                }
            }
        } else if (view instanceof Switch || view instanceof SwitchCompat) {
            return view;
        }
        return null;
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (COMPAT_MODE) {
            if (mSwitch != null) {
                mSwitch.setChecked(checked);
            }
        }
    }
}
