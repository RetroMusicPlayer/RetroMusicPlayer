package code.name.monkey.retromusic.fragments.base

import android.os.Bundle
import code.name.monkey.retromusic.fragments.mainactivity.LibraryFragment

open class AbsLibraryPagerFragment : AbsMusicServiceFragment() {


    val libraryFragment: LibraryFragment
        get() = parentFragment as LibraryFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }
}
