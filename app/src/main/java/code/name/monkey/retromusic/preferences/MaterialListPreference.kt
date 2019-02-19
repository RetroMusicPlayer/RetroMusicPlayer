package code.name.monkey.retromusic.preferences

import android.app.Dialog
import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.util.AttributeSet
import androidx.preference.ListPreference
import androidx.preference.PreferenceDialogFragmentCompat
import code.name.monkey.retromusic.R
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice


class MaterialListPreference : ListPreference {
    private val mLayoutRes = code.name.monkey.retromusic.R.layout.ate_preference_list

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun getDialogLayoutResource(): Int {
        return mLayoutRes;
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        // Default value from attribute. Fallback value is set to 0.
        return a.getString(index)
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
        position = materialListPreference.findIndexOfValue(materialListPreference.value)

        val entries = arguments?.getStringArrayList(EXTRA_ENTRIES)
        val entriesValues = arguments?.getStringArrayList(EXTRA_ENTRIES_VALUES)
        return MaterialDialog(activity!!).show {
            title(text = materialListPreference.title.toString())
            positiveButton(R.string.set)
            listItemsSingleChoice(items = entries, initialSelection = position) { dialog, index, text ->
                materialListPreference.callChangeListener(entriesValues!![index])
                materialListPreference.setCustomValue(entriesValues[index])
                materialListPreference.summary = entries!![index]
                dismiss()
            }
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            dismiss()
        }
    }

    companion object {
        var position = 0
        private const val EXTRA_KEY = "key"
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_ENTRIES = "extra_entries"
        private const val EXTRA_ENTRIES_VALUES = "extra_entries_values"

        fun newInstance(listPreference: ListPreference): MaterialListPreferenceDialog {
            val entries = listPreference.entries.toList() as ArrayList<String>
            val entriesValues = listPreference.entryValues.toList() as ArrayList<String>
            val args = Bundle()
            args.putString(EXTRA_KEY, listPreference.key)
            args.putString(EXTRA_TITLE, listPreference.title.toString())
            args.putStringArrayList(EXTRA_ENTRIES, entries)
            args.putStringArrayList(EXTRA_ENTRIES_VALUES, entriesValues)
            val fragment = MaterialListPreferenceDialog()
            fragment.arguments = args
            return fragment
        }
    }
}