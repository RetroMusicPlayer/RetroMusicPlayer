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
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.preference.ListPreference
import androidx.preference.PreferenceDialogFragmentCompat
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.extensions.colorControlNormal
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MaterialListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) : ListPreference(context, attrs, defStyleAttr, defStyleRes) {

    private val mLayoutRes = R.layout.ate_preference_list

    init {
        icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
            context.colorControlNormal(),
            SRC_IN
        )
    }

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): String {
        // Default value from attribute. Fallback value is set to 0.
        return a.getString(index)!!
    }

    fun setCustomValue(any: Any) {
        when (any) {
            is String -> persistString(any)
            is Int -> persistInt(any)
            is Boolean -> persistBoolean(any)
        }
    }
}

class MaterialListPreferenceDialog : PreferenceDialogFragmentCompat() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val materialListPreference = preference as MaterialListPreference
        val entries = arguments?.getStringArrayList(EXTRA_ENTRIES)
        val entriesValues = arguments?.getStringArrayList(EXTRA_ENTRIES_VALUES)
        val position: Int = arguments?.getInt(EXTRA_POSITION) ?: 0
        /* materialDialog = MaterialDialog(requireContext())
             .title(text = materialListPreference.title.toString())
             .positiveButton(R.string.set)
             .listItemsSingleChoice(
                 items = entries,
                 initialSelection = position,
                 waitForPositiveButton = true
             ) { _, index, _ ->
                 entriesValues?.let {
                     materialListPreference.callChangeListener(it[index])
                     materialListPreference.setCustomValue(it[index])
                 }
                 entries?.let {
                     materialListPreference.summary = it[index]
                     val value = materialListPreference.entryValues[index].toString()
                     if (materialListPreference.callChangeListener(value)) {
                         materialListPreference.value = value
                     }
                 }
                 dismiss()
             }
         materialDialog.getActionButton(WhichButton.POSITIVE)
             .updateTextColor(ThemeStore.accentColor(requireContext()))
         return materialDialog*/
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        materialDialog.dismiss()
    }

    private lateinit var materialDialog: MaterialDialog

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            materialDialog.dismiss()
        }
    }

    companion object {

        private const val EXTRA_KEY = "key"
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_POSITION = "position"
        private const val EXTRA_ENTRIES = "extra_entries"
        private const val EXTRA_ENTRIES_VALUES = "extra_entries_values"

        fun newInstance(listPreference: ListPreference): MaterialListPreferenceDialog {
            val entries = listPreference.entries.toList() as ArrayList<String>
            val entriesValues = listPreference.entryValues.toList() as ArrayList<String>
            val position = listPreference.findIndexOfValue(listPreference.value)
            val args = Bundle()
            args.putString(ARG_KEY, listPreference.key)
            args.putString(EXTRA_TITLE, listPreference.title.toString())
            args.putInt(EXTRA_POSITION, position)
            args.putStringArrayList(EXTRA_ENTRIES, entries)
            args.putStringArrayList(EXTRA_ENTRIES_VALUES, entriesValues)
            val fragment = MaterialListPreferenceDialog()
            fragment.arguments = args
            return fragment
        }
    }
}