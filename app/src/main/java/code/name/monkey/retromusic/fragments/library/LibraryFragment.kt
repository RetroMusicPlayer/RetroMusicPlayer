package code.name.monkey.retromusic.fragments.library

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import code.name.monkey.appthemehelper.common.ATHToolbarActivity.getToolbarBackgroundColor
import code.name.monkey.appthemehelper.util.ToolbarContentTintHelper
import code.name.monkey.retromusic.R
import code.name.monkey.retromusic.db.PlaylistDatabase
import code.name.monkey.retromusic.dialogs.AddToRetroPlaylist
import code.name.monkey.retromusic.extensions.findNavController
import code.name.monkey.retromusic.fragments.base.AbsMainActivityFragment
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.fragment_library.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class LibraryFragment : AbsMainActivityFragment(R.layout.fragment_library) {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainActivity.hideBottomBarVisibility(true)
        mainActivity.setSupportActionBar(toolbar)
        mainActivity.supportActionBar?.title = null
        toolbar.setNavigationOnClickListener {
            findNavController().navigate(
                R.id.searchFragment,
                null,
                navOptions
            )
        }
        setupNavigationController()
    }

    private fun setupNavigationController() {
        val navController = findNavController(R.id.fragment_container)
        NavigationUI.setupWithNavController(mainActivity.getBottomNavigationView(), navController)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        ToolbarContentTintHelper.handleOnPrepareOptionsMenu(requireActivity(), toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
        ToolbarContentTintHelper.handleOnCreateOptionsMenu(
            requireContext(),
            toolbar,
            menu,
            getToolbarBackgroundColor(toolbar)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings ->
                //CreateRetroPlaylist().show(childFragmentManager, "Dialog")
            lifecycleScope.launch {
                    val database = get<PlaylistDatabase>()
                    AddToRetroPlaylist.getInstance(database.playlistDao().playlists())
                        .show(childFragmentManager, "PlaylistDialog")
                }

            /*findNavController().navigate(
           R.id.settingsActivity,
           null,
           navOptions
       )*/
        }
        return super.onOptionsItemSelected(item)
    }

    fun addOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.addOnOffsetChangedListener(changedListener)
    }

    fun removeOnAppBarOffsetChangedListener(changedListener: AppBarLayout.OnOffsetChangedListener) {
        appBarLayout.removeOnOffsetChangedListener(changedListener)
    }

    fun getTotalAppBarScrollingRange(): Int {
        return 0
    }
}