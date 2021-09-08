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
import com.afollestad.materialdialogs.list.updateListItems
import java.io.File
import java.util.*

class BlacklistFolderChooserDialog : DialogFragment() {
    private var initialPath: String = Environment.getExternalStorageDirectory().absolutePath
    private var parentFolder: File? = null
    private var parentContents: Array<File>? = null
    private var canGoUp = false
    private var callback: FolderCallback? = null
    private val contentsArray: Array<String?>
        get() {
            if (parentContents == null) {
                return if (canGoUp) {
                    arrayOf("..")
                } else arrayOf()
            }
            val results = arrayOfNulls<String>(parentContents!!.size + if (canGoUp) 1 else 0)
            if (canGoUp) {
                results[0] = ".."
            }
            for (i in parentContents!!.indices) {
                results[if (canGoUp) i + 1 else i] = parentContents!![i].name
            }
            return results
        }

    private fun listFiles(): Array<File>? {
        val contents = parentFolder!!.listFiles()
        val results: MutableList<File> = ArrayList()
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
        var mSavedInstanceState = savedInstanceState
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            return MaterialDialog(requireActivity()).show {
                title(res = R.string.md_error_label)
                message(res = R.string.md_storage_perm_error)
                positiveButton(res = android.R.string.ok)
            }
        }
        if (mSavedInstanceState == null) {
            mSavedInstanceState = Bundle()
        }
        if (!mSavedInstanceState.containsKey("current_path")) {
            mSavedInstanceState.putString("current_path", initialPath)
        }
        parentFolder = File(mSavedInstanceState.getString("current_path", File.pathSeparator))
        checkIfCanGoUp()
        parentContents = listFiles()
        return MaterialDialog(requireContext())
            .title(text = parentFolder!!.absolutePath)
            .listItems(
                items = contentsArray.toCharSequence(),
                waitForPositiveButton = false
            ) { _: MaterialDialog, i: Int, _: CharSequence ->
                onSelection(i)
            }
            .noAutoDismiss()
            .cornerRadius(literalDp = 20F)
            .positiveButton(res = R.string.add_action) {
                callback!!.onFolderSelection(this@BlacklistFolderChooserDialog, parentFolder!!)
                dismiss()
            }
            .negativeButton(res = android.R.string.cancel) { dismiss() }
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
        dialog!!.setTitle(parentFolder!!.absolutePath)
        dialog.updateListItems(items = contentsArray.toCharSequence())
    }

    private fun Array<String?>.toCharSequence(): List<CharSequence> {
        return map { it as CharSequence }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("current_path", parentFolder!!.absolutePath)
    }

    fun setCallback(callback: FolderCallback?) {
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