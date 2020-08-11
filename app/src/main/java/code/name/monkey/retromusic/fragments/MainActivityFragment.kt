package code.name.monkey.retromusic.fragments

import androidx.annotation.LayoutRes
import code.name.monkey.retromusic.activities.MainActivity
import code.name.monkey.retromusic.fragments.base.AbsMusicServiceFragment

open class MainActivityFragment(@LayoutRes layoutRes: Int) : AbsMusicServiceFragment(layoutRes) {
    val mainActivity by lazy {
        requireActivity() as MainActivity
    }
}