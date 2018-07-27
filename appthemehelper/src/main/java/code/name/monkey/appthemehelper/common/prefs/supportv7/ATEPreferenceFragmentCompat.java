package code.name.monkey.appthemehelper.common.prefs.supportv7;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEEditTextPreferenceDialogFragmentCompat;
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEListPreferenceDialogFragmentCompat;
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEPreferenceDialogFragment;


public abstract class ATEPreferenceFragmentCompat extends PreferenceFragmentCompat {
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (this.getCallbackFragment() instanceof PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
            ((PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) this.getCallbackFragment()).onPreferenceDisplayDialog(this, preference);
            return;
        }

        if (this.getActivity() instanceof PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) {
            ((PreferenceFragmentCompat.OnPreferenceDisplayDialogCallback) this.getActivity()).onPreferenceDisplayDialog(this, preference);
            return;
        }

        if (this.getFragmentManager().findFragmentByTag("android.support.v7.preference.PreferenceFragment.DIALOG") == null) {
            DialogFragment dialogFragment = onCreatePreferenceDialog(preference);

            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0);
                dialogFragment.show(this.getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
                return;
            }
        }

        super.onDisplayPreferenceDialog(preference);
    }

    @Nullable
    public DialogFragment onCreatePreferenceDialog(Preference preference) {
        if (preference instanceof ATEEditTextPreference) {
            return ATEEditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
        } else if (preference instanceof ATEListPreference) {
            return ATEListPreferenceDialogFragmentCompat.newInstance(preference.getKey());
        } else if (preference instanceof ATEDialogPreference) {
            return ATEPreferenceDialogFragment.newInstance(preference.getKey());
        }
        return null;
    }
}
