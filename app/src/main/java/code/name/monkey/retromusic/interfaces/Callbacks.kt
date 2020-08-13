package code.name.monkey.retromusic.interfaces

import android.view.MenuItem
import android.view.View
import java.io.File

interface Callbacks {
    fun onFileSelected(file: File)

    fun onFileMenuClicked(file: File, view: View)

    fun onMultipleItemAction(item: MenuItem, files: ArrayList<File>)
}