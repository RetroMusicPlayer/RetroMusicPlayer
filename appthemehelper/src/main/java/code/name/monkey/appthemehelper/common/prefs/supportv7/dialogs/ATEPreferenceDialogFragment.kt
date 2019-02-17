package code.name.monkey.appthemehelper.common.prefs.supportv7.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.preference.DialogPreference
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog

/**
 * @author Karim Abou Zeid (kabouzeid)
 */
open class ATEPreferenceDialogFragment : DialogFragment(), MaterialDialog.SingleButtonCallback {
    private var mWhichButtonClicked: DialogAction? = null
    var preference: DialogPreference? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rawFragment = this.targetFragment
        if (rawFragment !is DialogPreference.TargetFragment) {
            throw IllegalStateException("Target fragment must implement TargetFragment interface")
        } else {
            val fragment = rawFragment as DialogPreference.TargetFragment?
            val key = this.arguments!!.getString(ARG_KEY)
            this.preference = fragment!!.findPreference(key) as DialogPreference
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val context = this.activity
        val builder = MaterialDialog.Builder(context!!)
                .title(this.preference!!.dialogTitle)
                .icon(this.preference!!.dialogIcon)
                .onAny(this)
                .positiveText(this.preference!!.positiveButtonText)
                .negativeText(this.preference!!.negativeButtonText)

        builder.content(this.preference!!.dialogMessage)
        this.onPrepareDialogBuilder(builder)
        val dialog = builder.build()
        if (this.needInputMethod()) {
            this.requestInputMethod(dialog)
        }

        return dialog
    }

    protected open fun onPrepareDialogBuilder(builder: MaterialDialog.Builder) {}

    protected open fun needInputMethod(): Boolean {
        return false
    }

    private fun requestInputMethod(dialog: Dialog) {
        val window = dialog.window
        window!!.setSoftInputMode(5)
    }

    override fun onClick(dialog: MaterialDialog, which: DialogAction) {
        mWhichButtonClicked = which
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onDialogClosed(mWhichButtonClicked == DialogAction.POSITIVE)
    }

    open fun onDialogClosed(positiveResult: Boolean) {

    }

    companion object {
        const val ARG_KEY = "key"

        fun newInstance(key: String): ATEPreferenceDialogFragment {
            val fragment = ATEPreferenceDialogFragment()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            fragment.arguments = b
            return fragment
        }
    }
}
