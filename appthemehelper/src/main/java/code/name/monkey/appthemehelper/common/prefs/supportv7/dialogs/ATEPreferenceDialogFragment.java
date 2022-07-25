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
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.DialogPreference;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
public class ATEPreferenceDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {
    protected static final String ARG_KEY = "key";
    private static final String TAG = "ATEPreferenceDialog";
    private int mWhichButtonClicked;
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
            this.mPreference = fragment.findPreference(key);
        }
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity context = this.getActivity();
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context,
                com.google.android.material.R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                .setTitle(this.mPreference.getDialogTitle())
                .setIcon(this.mPreference.getDialogIcon())
                .setMessage(this.mPreference.getDialogMessage())
                .setPositiveButton(this.mPreference.getPositiveButtonText(), this)
                .setNegativeButton(this.mPreference.getNegativeButtonText(), this);

        this.onPrepareDialogBuilder(builder);
        AlertDialog dialog = builder.create();
        if (this.needInputMethod()) {
            this.requestInputMethod(dialog);
        }
        return dialog;
    }

    public DialogPreference getPreference() {
        return this.mPreference;
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.i(TAG, "onDismiss: " + mWhichButtonClicked);
        onDialogClosed(mWhichButtonClicked == DialogInterface.BUTTON_POSITIVE);
    }

    public void onDialogClosed(boolean positiveResult) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.i(TAG, "onClick: " + which);
        mWhichButtonClicked = which;
        onDialogClosed(which == DialogInterface.BUTTON_POSITIVE);
    }
}