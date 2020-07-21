package code.name.monkey.retromusic.dialogs

import android.content.Context
import android.widget.ArrayAdapter
import code.name.monkey.retromusic.R

class RetroSingleCheckedListAdapter(
    context: Context,
    resource: Int = R.layout.dialog_list_item,
    objects: MutableList<String>
) : ArrayAdapter<String>(context, resource, objects) {

}