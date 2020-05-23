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


import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.preference.DialogPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import code.name.monkey.appthemehelper.R;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class ATEPreferenceDialogFragment extends DialogFragment {

    static final String ARG_KEY = "key";

    private DialogPreference mPreference;

    public static ATEPreferenceDialogFragment newInstance(String key) {
        ATEPreferenceDialogFragment fragment = new ATEPreferenceDialogFragment();
        Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        fragment.setArguments(b);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment rawFragment = this.getTargetFragment();
        if (!(rawFragment instanceof DialogPreference.TargetFragment)) {
            throw new IllegalStateException("Target fragment must implement TargetFragment interface");
        } else {
            DialogPreference.TargetFragment fragment = (DialogPreference.TargetFragment) rawFragment;
            String key = this.getArguments().getString(ARG_KEY);
            this.mPreference = (DialogPreference) fragment.findPreference(key);
        }
    }

    public DialogPreference getPreference() {
        return this.mPreference;
    }

    @NonNull
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        MaterialAlertDialogBuilder materialDialog = new MaterialAlertDialogBuilder(requireActivity(),
                R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle(mPreference.getTitle())
                .setIcon(mPreference.getIcon())
                .setMessage(mPreference.getDialogMessage())
                .setPositiveButton(mPreference.getPositiveButtonText(), (dialogInterface, i) -> {
                    onDialogClosed(true);
                })
                .setNegativeButton(mPreference.getNegativeButtonText(), (dialogInterface, i) -> {
                    onDialogClosed(false);
                });

        //this.onPrepareDialogBuilder(materialDialog);
        AlertDialog dialog = materialDialog.create();
        if (this.needInputMethod()) {
            this.requestInputMethod(dialog);
        }
        return dialog;
    }

    public void onDialogClosed(boolean positiveResult) {

    }

    protected void onPrepareDialogBuilder(MaterialAlertDialogBuilder builder) {
    }

    protected boolean needInputMethod() {
        return false;
    }

    private void requestInputMethod(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setSoftInputMode(5);
    }
}