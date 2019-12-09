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

package code.name.monkey.appthemehelper.common.prefs.supportv7


import androidx.fragment.app.DialogFragment
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEListPreferenceDialogFragmentCompat
import code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs.ATEPreferenceDialogFragment


/**
 * @author Karim Abou Zeid (kabouzeid)
 */
abstract class ATEPreferenceFragmentCompat : PreferenceFragmentCompat() {
    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (callbackFragment is OnPreferenceDisplayDialogCallback) {
            (callbackFragment as OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }

        if (activity is OnPreferenceDisplayDialogCallback) {
            (activity as OnPreferenceDisplayDialogCallback).onPreferenceDisplayDialog(this, preference)
            return
        }

        if (fragmentManager?.findFragmentByTag("android.support.v7.preference.PreferenceFragment.DIALOG") == null) {
            val dialogFragment = onCreatePreferenceDialog(preference)

            if (dialogFragment != null) {
                dialogFragment.setTargetFragment(this, 0)
                dialogFragment.show(fragmentManager!!, "android.support.v7.preference.PreferenceFragment.DIALOG")
                return
            }
        }

        super.onDisplayPreferenceDialog(preference)
    }

    open fun onCreatePreferenceDialog(preference: Preference): DialogFragment? {
        if (preference is ATEListPreference) {
            return ATEListPreferenceDialogFragmentCompat.newInstance(preference.getKey())
        } else if (preference is ATEDialogPreference) {
            return ATEPreferenceDialogFragment.newInstance(preference.getKey())
        }
        return null
    }
}