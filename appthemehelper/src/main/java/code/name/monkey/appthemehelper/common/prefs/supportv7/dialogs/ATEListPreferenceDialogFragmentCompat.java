/*
 * Copyright (c) 2019 Hemanth Savarala.
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by
 *  the Free Software Foundation either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.preference.ListPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEListPreference;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class ATEListPreferenceDialogFragmentCompat extends ATEPreferenceDialogFragment {
    private static final String TAG = "ATEPreferenceDialog";
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
    protected void onPrepareDialogBuilder(MaterialAlertDialogBuilder builder) {
        super.onPrepareDialogBuilder(builder);

        final ListPreference preference = getListPreference();

        if (preference.getEntries() == null || preference.getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
        builder.setSingleChoiceItems(preference.getEntries(), mClickedDialogEntryIndex, (dialog, which) -> {
            mClickedDialogEntryIndex = which;
            onClick(dialog, which);
            dismiss();
        });

        /*
         * The typical interaction for list-based dialogs is to have
         * click-on-an-item dismiss the dialog instead of the user having to
         * press 'Ok'.
         */
        builder.setPositiveButton(null, null);
        builder.setNegativeButton(null, null);
        builder.setNeutralButton(null, null);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        final ListPreference preference = getListPreference();
        Log.i(TAG, "onDialogClosed: " + positiveResult);
        if (positiveResult && mClickedDialogEntryIndex >= 0 &&
                preference.getEntryValues() != null) {
            String value = preference.getEntryValues()[mClickedDialogEntryIndex].toString();
            Log.i(TAG, "onDialogClosed: value " + value);
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
                Log.i(TAG, "onDialogClosed: set value ");
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.i(TAG, "onClick: " + which);
        mClickedDialogEntryIndex = which;
        super.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
    }
}