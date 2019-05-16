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

package code.name.monkey.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Html
import android.util.AttributeSet
import androidx.fragment.app.DialogFragment
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.dialogs.BlacklistFolderChooserDialog
import code.name.monkey.retromusic.providers.BlacklistStore
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItems
import java.io.File
import java.util.*


class BlacklistPreference : ATEDialogPreference {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)
}

class BlacklistPreferenceDialog : DialogFragment(), BlacklistFolderChooserDialog.FolderCallback {
    companion object {
        private const val EXTRA_KEY = "key"

        fun newInstance(key: String): BlacklistPreferenceDialog {
            val args = Bundle()
            args.putString(EXTRA_KEY, key)
            val fragment = BlacklistPreferenceDialog()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val blacklistFolderChooserDialog = childFragmentManager.findFragmentByTag("FOLDER_CHOOSER") as BlacklistFolderChooserDialog?
        blacklistFolderChooserDialog?.setCallback(this)
        refreshBlacklistData()
        return MaterialDialog(context!!, BottomSheet()).sstathow {
            title(code.name.monkey.retromusic.R.string.blacklist)
            positiveButton(android.R.string.ok) {
                dismiss()
            }
            neutralButton(text = getString(R.string.clear_action)) {
                MaterialDialog(context, BottomSheet()).show {
                    title(code.name.monkey.retromusic.R.string.clear_blacklist)
                    message(code.name.monkey.retromusic.R.string.do_you_want_to_clear_the_blacklist)
                    positiveButton(code.name.monkey.retromusic.R.string.clear_action) {
                        BlacklistStore.getInstance(context).clear();
                        refreshBlacklistData();
                    }
                    negativeButton(android.R.string.cancel)
                }
            }
            negativeButton(R.string.add_action) {
                val dialog = BlacklistFolderChooserDialog.create()
                dialog.setCallback(this@BlacklistPreferenceDialog)
                dialog.show(childFragmentManager, "FOLDER_CHOOSER");
            }
            listItems(items = paths) { dialog, index, text ->
                MaterialDialog(context, BottomSheet()).show {
                    title(code.name.monkey.retromusic.R.string.remove_from_blacklist)
                    message(text = Html.fromHtml(getString(code.name.monkey.retromusic.R.string.do_you_want_to_remove_from_the_blacklist, text)))
                    positiveButton(code.name.monkey.retromusic.R.string.remove_action) {
                        BlacklistStore.getInstance(context).removePath(File(text));
                        refreshBlacklistData();
                    }
                    negativeButton(android.R.string.cancel)
                }
            }
            noAutoDismiss()
        }
    }

    private lateinit var paths: ArrayList<String>

    private fun refreshBlacklistData() {
        this.paths = BlacklistStore.getInstance(context!!).paths
        val dialog = dialog as MaterialDialog
        dialog.listItems(items = paths)
    }

    override fun onFolderSelection(dialog: BlacklistFolderChooserDialog, folder: File) {
        BlacklistStore.getInstance(context!!).addPath(folder);
        refreshBlacklistData();
    }

    /*public static final String TAG = BlacklistPreferenceDialog.class.getSimpleName();

    private ArrayList<String> paths;

    public static BlacklistPreferenceDialog newInstance() {
        return new BlacklistPreferenceDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BlacklistFolderChooserDialog blacklistFolderChooserDialog = (BlacklistFolderChooserDialog) getChildFragmentManager()
                .findFragmentByTag("FOLDER_CHOOSER");
        if (blacklistFolderChooserDialog != null) {
            blacklistFolderChooserDialog.setCallback(this);
        }

        refreshBlacklistData();
        return new MaterialDialog.Builder(getContext())
                .title(R.string.blacklist)
                .positiveText(android.R.string.ok)
                .neutralText(R.string.clear_action)
                .negativeText(R.string.add_action)
                .items(paths)
                .autoDismiss(false)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int i,
                                            final CharSequence charSequence) {
                        new MaterialDialog.Builder(getContext())
                                .title(R.string.remove_from_blacklist)
                                .content(Html.fromHtml(
                                        getString(R.string.do_you_want_to_remove_from_the_blacklist, charSequence)))
                                .positiveText(R.string.remove_action)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog materialDialog,
                                                        @NonNull DialogAction dialogAction) {
                                        BlacklistStore.getInstance(getContext())
                                                .removePath(new File(charSequence.toString()));
                                        refreshBlacklistData();
                                    }
                                }).show();
                    }
                })
                // clear
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                                        @NonNull DialogAction dialogAction) {
                        new MaterialDialog.Builder(getContext())
                                .title(R.string.clear_blacklist)
                                .content(R.string.do_you_want_to_clear_the_blacklist)
                                .positiveText(R.string.clear_action)
                                .negativeText(android.R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog materialDialog,
                                                        @NonNull DialogAction dialogAction) {
                                        BlacklistStore.getInstance(getContext()).clear();
                                        refreshBlacklistData();
                                    }
                                }).show();
                    }
                })
                // add
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                                        @NonNull DialogAction dialogAction) {
                        BlacklistFolderChooserDialog dialog = BlacklistFolderChooserDialog.create();
                        dialog.setCallback(BlacklistPreferenceDialog.this);
                        dialog.show(getChildFragmentManager(), "FOLDER_CHOOSER");
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                                        @NonNull DialogAction dialogAction) {
                        dismiss();
                    }
                })
                .build();
    }

    private void refreshBlacklistData() {
        paths = BlacklistStore.getInstance(getContext()).getPaths();

        MaterialDialog dialog = (MaterialDialog) getDialog();
        if (dialog != null) {
            String[] pathArray = new String[paths.size()];
            pathArray = paths.toArray(pathArray);
            dialog.setItems((CharSequence[]) pathArray);
        }
    }

    @Override
    public void onFolderSelection(@NonNull BlacklistFolderChooserDialog folderChooserDialog,
                                  @NonNull File file) {
        BlacklistStore.getInstance(getContext()).addPath(file);
        refreshBlacklistData();
    }*/
}
