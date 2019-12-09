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


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.preference.ListPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEListPreference;

/**
 * Created by hemanths on 2019-09-03.
 */
public class ATEListPreferenceDialogFragmentCompat extends ATEPreferenceDialogFragment {
    private int mClickedDialogEntryIndex;

    @NonNull
    public static ATEListPreferenceDialogFragmentCompat newInstance(@NonNull String key) {
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
    protected void onPrepareDialogBuilder(@NonNull MaterialAlertDialogBuilder builder) {
        super.onPrepareDialogBuilder(builder);

        final ListPreference preference = getListPreference();

        if (preference.getEntries() == null || preference.getEntryValues() == null) {
            throw new IllegalStateException(
                    "ListPreference requires an entries array and an entryValues array.");
        }

        mClickedDialogEntryIndex = preference.findIndexOfValue(preference.getValue());
        builder.setSingleChoiceItems(preference.getEntries(), mClickedDialogEntryIndex, (dialogInterface, i) -> {
            mClickedDialogEntryIndex = i;
        });

        builder.setPositiveButton("Ok", null);
        builder.setNegativeButton("", null);
        builder.setNeutralButton("", null);
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

   /* @Override
    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
        mClickedDialogEntryIndex = which;
        onClick(dialog, DialogAction.POSITIVE);
        dismiss();
        return true;
    }*/
}