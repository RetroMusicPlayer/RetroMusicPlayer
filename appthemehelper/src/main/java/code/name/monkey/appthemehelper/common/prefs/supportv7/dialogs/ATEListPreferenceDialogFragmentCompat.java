package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs;

import android.os.Bundle;
import androidx.preference.ListPreference;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEListPreference;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class ATEListPreferenceDialogFragmentCompat extends ATEPreferenceDialogFragment implements MaterialDialog.ListCallbackSingleChoice {
    private int mClickedDialogEntryIndex;

    public static ATEListPreferenceDialogFragmentCompat newInstance(String key) {
        final ATEListPreferenceDialogFragmentCompat fragment = new ATEListPreferenceDialogFragmentCompat();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    private ATEListPreference getListPreference() {
        return (ATEListPreference) getPreference();
    }

    @Override
    protected void onPrepareDialogBuilder(MaterialDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        final ListPreference preference = getListPreference();

        if (preference.getEntries() == null || preference.getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
        builder.items(preference.getEntries())
                .alwaysCallSingleChoiceCallback()
                .itemsCallbackSingleChoice(mClickedDialogEntryIndex, this);

        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */
        builder.positiveText("");
        builder.negativeText("");
        builder.neutralText("");
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        final ListPreference preference = getListPreference();
        if (positiveResult && mClickedDialogEntryIndex >= 0 &&
                preference.getEntryValues() != null) {
            String value = preference.getEntryValues()[mClickedDialogEntryIndex].toString();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }

    @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        mClickedDialogEntryIndex = which;
        onClick(dialog, DialogAction.POSITIVE);
        dismiss();
        return true;
    }
}
