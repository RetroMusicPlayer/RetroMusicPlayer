package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEEditTextPreference;


public class ATEEditTextPreferenceDialogFragmentCompat extends ATEPreferenceDialogFragment implements MaterialDialog.InputCallback {

    private CharSequence input;

    public static ATEEditTextPreferenceDialogFragmentCompat newInstance(String key) {
        ATEEditTextPreferenceDialogFragmentCompat fragment = new ATEEditTextPreferenceDialogFragmentCompat();
        Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    private ATEEditTextPreference getEditTextPreference() {
        return (ATEEditTextPreference) getPreference();
    }

    @Override
    protected void onPrepareDialogBuilder(MaterialDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.input("", getEditTextPreference().getText(), this);
    }

    protected boolean needInputMethod() {
        return true;
    }

    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            String value = input.toString();
            if (this.getEditTextPreference().callChangeListener(value)) {
                this.getEditTextPreference().setText(value);
            }
        }

    }

    @Override
    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
        this.input = input;
    }
}
