package io.github.muntashirakon.music.dialogs

import android.content.Context
import android.widget.ArrayAdapter
import io.github.muntashirakon.music.R

class RetroSingleCheckedListAdapter(
    context: Context,
    resource: Int = R.layout.dialog_list_item,
    objects: MutableList<String>
) : ArrayAdapter<String>(context, resource, objects) {

}