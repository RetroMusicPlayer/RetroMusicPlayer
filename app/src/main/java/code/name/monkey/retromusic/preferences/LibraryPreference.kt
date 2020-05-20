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
import android.util.AttributeSet
import android.widget.Toast
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat.SRC_IN
import androidx.preference.PreferenceDialogFragmentCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import code.name.monkey.appthemehelper.common.prefs.supportv7.ATEDialogPreference
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.adapter.CategoryInfoAdapter
import code.name.monkey.retromusic.extensions.colorControlNormal
import code.name.monkey.retromusic.model.CategoryInfo
import code.name.monkey.retromusic.util.PreferenceUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import java.util.*

class LibraryPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = -1,
    defStyleRes: Int = -1
) :
    ATEDialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    init {
        icon?.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                context.colorControlNormal(),
                SRC_IN
            )
    }
}

class LibraryPreferenceDialog : PreferenceDialogFragmentCompat() {

    override fun onDialogClosed(positiveResult: Boolean) {
    }

    lateinit var adapter: CategoryInfoAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.preference_dialog_library_categories,
            null
        )

        val categoryInfos: List<CategoryInfo> = if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList(PreferenceUtil.LIBRARY_CATEGORIES)!!
        } else {
            PreferenceUtil.getInstance(requireContext()).libraryCategoryInfos
        }
        adapter = CategoryInfoAdapter(categoryInfos)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter

        adapter.attachToRecyclerView(recyclerView)

        return MaterialDialog(requireContext())
            .title(R.string.library_categories)
            .cornerRadius(PreferenceUtil.getInstance(requireContext()).dialogCorner)
            .customView(view = view)
            .positiveButton(android.R.string.ok) {
                updateCategories(adapter.categoryInfos)
                dismiss()
            }
            .negativeButton(android.R.string.cancel) {
                dismiss()
            }
            .neutralButton(R.string.reset_action) {
                adapter.categoryInfos =
                    PreferenceUtil.getInstance(requireContext()).defaultLibraryCategoryInfos
            }
            .noAutoDismiss()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            PreferenceUtil.LIBRARY_CATEGORIES,
            ArrayList(adapter.categoryInfos)
        )
    }

    private fun updateCategories(categories: List<CategoryInfo>) {
        if (getSelected(categories) == 0) return
        if (getSelected(categories) > 5) {
            Toast.makeText(context, "Not more than 5 items", Toast.LENGTH_SHORT).show()
            return
        }
        PreferenceUtil.getInstance(requireContext()).libraryCategoryInfos = categories
    }

    private fun getSelected(categories: List<CategoryInfo>): Int {
        var selected = 0
        for (categoryInfo in categories) {
            if (categoryInfo.visible)
                selected++
        }
        return selected
    }

    companion object {

        fun newInstance(key: String): LibraryPreferenceDialog {
            val bundle = Bundle()
            bundle.putString(ARG_KEY, key)
            val fragment = LibraryPreferenceDialog()
            fragment.arguments = bundle
            return fragment
        }
    }
}