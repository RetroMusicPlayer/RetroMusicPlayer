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

package code.name.monkey.retromusic.dialogs

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import code.name.monkey.retromusic.R
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class BlacklistFolderChooserDialog : DialogFragment() {

    private val initialPath = Environment.getExternalStorageDirectory().absolutePath
    private var parentFolder: File? = null
    private var parentContents: Array<File>? = null
    private var canGoUp = false
    private var callback: FolderCallback? = null


    private fun contentsArray(): List<String> {
        if (parentContents == null) {
            return if (canGoUp) {
                return listOf("..")
            } else listOf()
        }

        val results = arrayOfNulls<String>(parentContents!!.size + if (canGoUp) 1 else 0)
        if (canGoUp) {
            results[0] = ".."
        }
        for (i in parentContents!!.indices) {
            results[if (canGoUp) i + 1 else i] = parentContents!![i].name
        }

        val data = ArrayList<String>()
        for (i in results) {
            data.add(i!!)
        }
        return data
    }

    private fun listFiles(): Array<File>? {
        val contents = parentFolder!!.listFiles()
        val results = ArrayList<File>()
        if (contents != null) {
            for (fi in contents) {
                if (fi.isDirectory) {
                    results.add(fi)
                }
            }
            Collections.sort(results, FolderSorter())
            return results.toTypedArray()
        }
        return null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var savedInstanceStateFinal = savedInstanceState
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return MaterialDialog(requireActivity()).show {
                title(R.string.md_error_label)

                message(R.string.md_storage_perm_error)
                positiveButton(android.R.string.ok)
            }
        }
        if (savedInstanceStateFinal == null) {
            savedInstanceStateFinal = Bundle()
        }
        if (!savedInstanceStateFinal.containsKey("current_path")) {
            savedInstanceStateFinal.putString("current_path", initialPath)
        }
        parentFolder = File(savedInstanceStateFinal.getString("current_path", File.pathSeparator))
        checkIfCanGoUp()
        parentContents = listFiles()

        return MaterialDialog(requireContext()).show {
            title(text = parentFolder!!.absolutePath)

            listItems(items = contentsArray(), waitForPositiveButton = false) { _, index, _ ->
                onSelection(index)
            }
            noAutoDismiss()
            positiveButton(R.string.add_action) {
                dismiss()
                callback!!.onFolderSelection(this@BlacklistFolderChooserDialog, parentFolder!!)
            }
            negativeButton(android.R.string.cancel) {
                dismiss()
            }
        }
    }

    private fun onSelection(i: Int) {
        if (canGoUp && i == 0) {
            parentFolder = parentFolder!!.parentFile
            if (parentFolder!!.absolutePath == "/storage/emulated") {
                parentFolder = parentFolder!!.parentFile
            }
            checkIfCanGoUp()
        } else {
            parentFolder = parentContents!![if (canGoUp) i - 1 else i]
            canGoUp = true
            if (parentFolder!!.absolutePath == "/storage/emulated") {
                parentFolder = Environment.getExternalStorageDirectory()
            }
        }
        reload()
    }

    private fun checkIfCanGoUp() {
        canGoUp = parentFolder!!.parent != null
    }

    private fun reload() {
        parentContents = listFiles()
        val dialog = dialog as MaterialDialog?

        dialog?.apply {

            setTitle(parentFolder!!.absolutePath)
            listItems(items = contentsArray()) { _, index, _ ->
                onSelection(index)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("current_path", parentFolder!!.absolutePath)
    }

    fun setCallback(callback: FolderCallback) {
        this.callback = callback
    }

    interface FolderCallback {
        fun onFolderSelection(dialog: BlacklistFolderChooserDialog, folder: File)
    }

    private class FolderSorter : Comparator<File> {

        override fun compare(lhs: File, rhs: File): Int {
            return lhs.name.compareTo(rhs.name)
        }
    }

    companion object {

        fun create(): BlacklistFolderChooserDialog {
            return BlacklistFolderChooserDialog()
        }
    }
}